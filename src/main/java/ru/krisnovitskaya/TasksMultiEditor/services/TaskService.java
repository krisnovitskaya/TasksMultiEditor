package ru.krisnovitskaya.TasksMultiEditor.services;

import ru.krisnovitskaya.TasksMultiEditor.dtos.NewTaskDto;
import ru.krisnovitskaya.TasksMultiEditor.dtos.TaskDto;

public interface TaskService {
    TaskDto addNew(NewTaskDto newDto);
    TaskDto update(TaskDto updated);
}
