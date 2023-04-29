package ru.krisnovitskaya.TasksMultiEditor.dtos;

import java.time.LocalDate;

public record UpdateTaskDto(Long id, Long executorId, Long controllerId, String title, String description, LocalDate deadline, int version){
}
