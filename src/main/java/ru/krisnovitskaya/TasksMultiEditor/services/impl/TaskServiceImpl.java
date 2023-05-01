package ru.krisnovitskaya.TasksMultiEditor.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.krisnovitskaya.TasksMultiEditor.configs.JmsConfig;
import ru.krisnovitskaya.TasksMultiEditor.diff.DiffComputeHelper;
import ru.krisnovitskaya.TasksMultiEditor.dtos.NewTaskDto;
import ru.krisnovitskaya.TasksMultiEditor.dtos.TaskDto;
import ru.krisnovitskaya.TasksMultiEditor.dtos.UpdateTaskDto;
import ru.krisnovitskaya.TasksMultiEditor.entities.Task;
import ru.krisnovitskaya.TasksMultiEditor.exceptions.MultiUpdateException;
import ru.krisnovitskaya.TasksMultiEditor.exceptions.ResourceNotFoundException;
import ru.krisnovitskaya.TasksMultiEditor.mappers.TaskMapper;
import ru.krisnovitskaya.TasksMultiEditor.repositories.TaskRepository;
import ru.krisnovitskaya.TasksMultiEditor.repositories.UserRepository;
import ru.krisnovitskaya.TasksMultiEditor.services.TaskService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TaskMapper taskMapper;

    private final JmsTemplate jmsTemplate;
    private final DiffComputeHelper diffComputeHelper;

    @Transactional
    @Override
    public TaskDto addNew(NewTaskDto newDto) {
        Task taskNew = new Task();
        taskNew.setTitle(newDto.title());
        taskNew.setDescription(newDto.description());
        taskNew.setDeadline(newDto.deadline() == null ? LocalDate.now() : newDto.deadline());
        userRepository.findById(newDto.controllerId()).ifPresent(taskNew::setController);
        userRepository.findById(newDto.executorId()).ifPresent(taskNew::setExecutor);
        TaskDto insertedDto = taskMapper.fromEntity(taskRepository.saveAndFlush(taskNew));
        jmsTemplate.convertAndSend(JmsConfig.TASK_CREATE, insertedDto);
        return insertedDto;
    }

    public List<TaskDto> getAll() {
        return taskRepository.findAll().stream().map(taskMapper::fromEntity).collect(Collectors.toList());
    }

    @Transactional
    public TaskDto update(UpdateTaskDto updated) {
        Task taskDb = taskRepository.findByIdLock(updated.id()).orElseThrow(() -> new ResourceNotFoundException(String.format("Task with id=%d not exists", updated.id())));
        if (taskDb.getVersion() != updated.version()) {
            throw new MultiUpdateException(diffComputeHelper.computeDiff(taskMapper.fromEntity(taskDb), updated));
        }
        updateEntityByDtoValue(taskDb, updated);
        TaskDto savedDto = taskMapper.fromEntity(taskRepository.saveAndFlush(taskDb));
        if(savedDto.version() > updated.version()) jmsTemplate.convertAndSend(JmsConfig.TASK_CHANGE, savedDto);
        return savedDto;
    }

    private void updateEntityByDtoValue(Task task, UpdateTaskDto dto) {
        if (!task.getController().getId().equals(dto.controllerId())) {
            task.setController(userRepository.findById(dto.controllerId()).orElseThrow(() -> new ResourceNotFoundException(String.format("User with id=%d not exists", dto.controllerId()))));
        }
        if (!task.getExecutor().getId().equals(dto.executorId())) {
            task.setExecutor(userRepository.findById(dto.executorId()).orElseThrow(() -> new ResourceNotFoundException(String.format("User with id=%d not exists", dto.executorId()))));
        }
        task.setTitle(dto.title());
        task.setDescription(dto.description());
        task.setDeadline(dto.deadline());
    }



    //for test lock only
    @Transactional
    @SneakyThrows
    public long lockTaskRowByTime(Long rowId, long time){
        long start = System.currentTimeMillis();

        Optional<Task> byIdLock = taskRepository.findByIdLock(rowId);
        log.info(byIdLock.get().toString());

        Thread.sleep(time);

        long end = System.currentTimeMillis();

        return end - start;
    }
}
