package ru.krisnovitskaya.TasksMultiEditor.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.krisnovitskaya.TasksMultiEditor.repositories.TaskRepository;
import ru.krisnovitskaya.TasksMultiEditor.services.TaskService;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
}
