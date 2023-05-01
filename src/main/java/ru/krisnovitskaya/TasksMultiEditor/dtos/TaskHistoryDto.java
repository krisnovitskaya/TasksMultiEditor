package ru.krisnovitskaya.TasksMultiEditor.dtos;

import ru.krisnovitskaya.TasksMultiEditor.entities.TaskHistory;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record TaskHistoryDto(Long id, String operation, Long taskId, Long executorId
        , Long controllerId, String title, String description, LocalDate deadline
        , int version, String modifiedBy, LocalDateTime modifiedDate) {


    public TaskHistoryDto(TaskHistory entity) {
        this(
        entity.getId(),
        entity.getOperation(),
        entity.getTaskId(),
        entity.getExecutorId(),
        entity.getControllerId(),
        entity.getTitle(),
        entity.getDescription(),
        entity.getDeadline(),
        entity.getVersion(),
        entity.getModifiedBy(),
        entity.getModifiedDate()
        );
    }
}
