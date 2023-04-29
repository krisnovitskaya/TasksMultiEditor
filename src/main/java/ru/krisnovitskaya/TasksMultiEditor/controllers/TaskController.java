package ru.krisnovitskaya.TasksMultiEditor.controllers;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.krisnovitskaya.TasksMultiEditor.dtos.NewTaskDto;
import ru.krisnovitskaya.TasksMultiEditor.dtos.TaskDto;
import ru.krisnovitskaya.TasksMultiEditor.dtos.UpdateTaskDto;
import ru.krisnovitskaya.TasksMultiEditor.services.TaskService;

import java.util.List;

@RestController
@RequestMapping("/task")
@RequiredArgsConstructor
@Slf4j
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public TaskDto addNew(@RequestBody NewTaskDto newTask) {
        log.debug("newTask from client {}", newTask);
        return taskService.addNew(newTask);
    }

    @PutMapping
    public TaskDto update(@RequestBody UpdateTaskDto updated) {
        log.debug("updated from client {}", updated);
        return taskService.update(updated);
    }

    @GetMapping
    public List<TaskDto> getAll() {
        log.debug("get all");
        return taskService.getAll();
    }
}
