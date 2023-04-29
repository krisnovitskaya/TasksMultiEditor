package ru.krisnovitskaya.TasksMultiEditor.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TaskMapper taskMapper;

    private final DiffComputeHelper diffComputeHelper;

    @Transactional
    @Override
    public TaskDto addNew(NewTaskDto newDto) {
        Task taskNew = new Task();
        taskNew.setTitle(newDto.title());
        taskNew.setDescription(newDto.description());
        userRepository.findById(newDto.controllerId()).ifPresent(taskNew::setController);
        userRepository.findById(newDto.executorId()).ifPresent(taskNew::setExecutor);
        Task taskFromDB = taskRepository.save(taskNew);
        return taskMapper.fromEntity(taskFromDB);
    }

    public List<TaskDto> getAll() {
        return taskRepository.findAll().stream().map(taskMapper::fromEntity).collect(Collectors.toList());
    }

    @Transactional
    public TaskDto update(UpdateTaskDto updated) {
        log.info("update start. thread {} {}", Thread.currentThread().getName(), updated);
        Task taskDb = taskRepository.findByIdLock(updated.id()).orElseThrow(() -> new ResourceNotFoundException(String.format("Task with id=%d not exists", updated.id())));
        if(taskDb.getVersion() > updated.version()){
            throw new MultiUpdateException(diffComputeHelper.computeDiff(taskMapper.fromEntity(taskDb), updated));
        }
        log.info("update lock and sleep. thread {} taskDB vers {}", Thread.currentThread().getName(), taskDb.getVersion());
        sleep(8000);
        log.info("update after sleep. thread {}", Thread.currentThread().getName());

        updateEntityByDtoValue(taskDb, updated);
        Task saved = taskRepository.saveAndFlush(taskDb);
        log.info("update saved entity. thread {}. saved version {}", Thread.currentThread().getName(), saved.getVersion());
        return taskMapper.fromEntity(saved);
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
    }


    private void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
