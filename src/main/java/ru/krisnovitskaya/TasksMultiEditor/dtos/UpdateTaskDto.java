package ru.krisnovitskaya.TasksMultiEditor.dtos;

public record UpdateTaskDto(Long id, Long executorId, Long controllerId, String title, String description, int version){
}
