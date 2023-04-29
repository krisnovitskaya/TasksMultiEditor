package ru.krisnovitskaya.TasksMultiEditor.dtos;

import java.time.LocalDate;

public record NewTaskDto(Long executorId, Long controllerId, String title, String description, LocalDate deadline) {
}
