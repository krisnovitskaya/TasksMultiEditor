package ru.krisnovitskaya.TasksMultiEditor.dtos;

public record NewTaskDto(Long executorId, Long controllerId, String title, String description) {
}
