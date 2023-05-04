package ru.krisnovitskaya.TasksMultiEditor.services.impl;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.krisnovitskaya.TasksMultiEditor.dtos.DiffTaskDto;
import ru.krisnovitskaya.TasksMultiEditor.dtos.NewTaskDto;
import ru.krisnovitskaya.TasksMultiEditor.dtos.TaskDto;
import ru.krisnovitskaya.TasksMultiEditor.dtos.UpdateTaskDto;
import ru.krisnovitskaya.TasksMultiEditor.exceptions.MultiUpdateException;
import ru.krisnovitskaya.TasksMultiEditor.repositories.TaskRepository;
import ru.krisnovitskaya.TasksMultiEditor.repositories.UserRepository;
import ru.krisnovitskaya.TasksMultiEditor.services.MailSenderService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
class TaskServiceImplTest {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskServiceImpl taskService;

    @MockBean
    private JmsTemplate jmsTemplate;


    @Test
    void shouldCreateOneTask() {
        int sizeBeforeInsertOne = taskRepository.findAll().size();

        var dto = new NewTaskDto(2L, 3L, "First Title", "First task description", LocalDate.of(2023, 6, 15));
        TaskDto taskDtoFromDB = taskService.addNew(dto);
        System.out.println(taskDtoFromDB);
        assertNotNull(taskDtoFromDB);

        int sizeAfterInsertOne = taskRepository.findAll().size();

        assertEquals(1, sizeAfterInsertOne - sizeBeforeInsertOne);
    }

    @Test
    void shouldUpdateVersionAfterUpdate() {
        var dto = new NewTaskDto(2L, 3L, "First Title", "First task description", LocalDate.of(2023, 6, 15));
        TaskDto taskDtoFromDB = taskService.addNew(dto);

        var forUpdate = new UpdateTaskDto(taskDtoFromDB.id(), taskDtoFromDB.executor().id(), taskDtoFromDB.controller().id(), "Changed Title", taskDtoFromDB.description(), taskDtoFromDB.deadline(), taskDtoFromDB.version());

        TaskDto updated = taskService.update(forUpdate);
        assertEquals("Changed Title", updated.title());
        assertEquals(forUpdate.version() + 1, updated.version());

        var forUpdateOldVersion = new UpdateTaskDto(taskDtoFromDB.id(), taskDtoFromDB.executor().id(), taskDtoFromDB.controller().id(), "Changed Title1", taskDtoFromDB.description(), taskDtoFromDB.deadline(), taskDtoFromDB.version());

        assertThrowsExactly(MultiUpdateException.class, () -> taskService.update(forUpdateOldVersion));
    }

    @RepeatedTest(50)
    void shouldWaitAnotherLockEndBeforeUpdate() throws ExecutionException, InterruptedException, TimeoutException {
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        var dto = new NewTaskDto(2L, 3L, "Wait Title", "First task description", LocalDate.of(2023, 6, 15));
        TaskDto taskDtoFromDB = taskService.addNew(dto);
        var forUpdate = new UpdateTaskDto(taskDtoFromDB.id(), taskDtoFromDB.executor().id(), taskDtoFromDB.controller().id(), "Changed Title", taskDtoFromDB.description(), taskDtoFromDB.deadline(), taskDtoFromDB.version());

        final CountDownLatch latch = new CountDownLatch(2);

        Future<Long> waitFuture = executorService.submit(() -> {
            latch.countDown();
            latch.await();
            return taskService.lockTaskRowByTime(taskDtoFromDB.id(), 200);
        });


        Future<Long> insertFuture = executorService.submit(() -> {
            latch.countDown();
            latch.await();
            long start = System.currentTimeMillis();
            Thread.sleep(50);
            TaskDto update = taskService.update(forUpdate);
            long end = System.currentTimeMillis();
            return end - start;
        });


        Long timeWaiting = waitFuture.get(300, TimeUnit.MILLISECONDS);
        Long insertWaiting = insertFuture.get(300, TimeUnit.MILLISECONDS);

        System.out.println("timeWaiting=" + timeWaiting);
        System.out.println("insertWaiting=" + insertWaiting);

        Assertions.assertThat(insertWaiting).isGreaterThan(timeWaiting);
        executorService.shutdown();
    }

    @RepeatedTest(50)
    void shouldUpdateOnceAndThrowExceptionInOther() throws InterruptedException {

        var dto = new NewTaskDto(2L, 3L, "Title", "First task description", LocalDate.of(2023, 6, 15));
        TaskDto taskDtoFromDB = taskService.addNew(dto);

        CountDownLatch latch = new CountDownLatch(5);
        Queue<TaskDto> accessedUpdate = new ConcurrentLinkedQueue<>();
        Queue<DiffTaskDto> excUpdate = new ConcurrentLinkedQueue<>();

        List<Thread> threads = new ArrayList<>(5);
        for (int i = 0; i < 5; i++) {
            threads.add(new Thread(() -> {
                try {
                    latch.countDown();
                    latch.await();
                    var forUpdate = new UpdateTaskDto(taskDtoFromDB.id(), taskDtoFromDB.executor().id(), taskDtoFromDB.controller().id(), taskDtoFromDB.title().concat(Thread.currentThread().getName()), taskDtoFromDB.description(), taskDtoFromDB.deadline(), taskDtoFromDB.version());
                    TaskDto update = taskService.update(forUpdate);
                    accessedUpdate.add(update);
                }catch (MultiUpdateException e){
                    excUpdate.add(e.getDiffTaskDto());
                }catch (InterruptedException e){
                    throw new RuntimeException(e.getMessage());
                }
            }));
        }

        threads.forEach(Thread::start);
        for (Thread thread : threads) {
            thread.join();
        }

        assertEquals(1, accessedUpdate.size());
        assertEquals(4, excUpdate.size());
    }
}