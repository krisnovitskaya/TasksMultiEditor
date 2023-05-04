package ru.krisnovitskaya.TasksMultiEditor.services;

import ru.krisnovitskaya.TasksMultiEditor.dtos.NewTaskDto;
import ru.krisnovitskaya.TasksMultiEditor.dtos.TaskDto;
import ru.krisnovitskaya.TasksMultiEditor.dtos.UpdateTaskDto;

import java.util.List;

public interface TaskService {
    TaskDto addNew(NewTaskDto newDto);

    TaskDto update(UpdateTaskDto updated);

    List<TaskDto> getAll();
}
