package ru.krisnovitskaya.TasksMultiEditor.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.krisnovitskaya.TasksMultiEditor.dtos.NewTaskDto;
import ru.krisnovitskaya.TasksMultiEditor.dtos.TaskDto;
import ru.krisnovitskaya.TasksMultiEditor.entities.Task;
import ru.krisnovitskaya.TasksMultiEditor.exceptions.ResourceNotFoundException;
import ru.krisnovitskaya.TasksMultiEditor.mappers.TaskMapper;
import ru.krisnovitskaya.TasksMultiEditor.repositories.TaskRepository;
import ru.krisnovitskaya.TasksMultiEditor.repositories.UserRepository;
import ru.krisnovitskaya.TasksMultiEditor.services.TaskService;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    private final TaskMapper taskMapper;

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

    @Override
    @Transactional
    public TaskDto update(TaskDto updated) {
//        Task taskDb = taskRepository.findById(updated.getId()).orElseThrow(() -> new ResourceNotFoundException("not found task with id = " + updated.getId()));
        Task taskDb = taskRepository.findByIdLock(updated.getId());
        taskDb.setTitle(updated.getTitle());
        taskDb.setDescription(updated.getDescription());
        Task saved = taskRepository.save(taskDb);
        return taskMapper.fromEntity(saved);
    }
}
