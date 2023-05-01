package ru.krisnovitskaya.TasksMultiEditor.services;

import ru.krisnovitskaya.TasksMultiEditor.dtos.TaskHistoryDto;

import java.util.List;

public interface TaskHistoryService {
    List<TaskHistoryDto> findHistoryByTaskId(Long id);
    TaskHistoryDto findHistoryByTaskIdAndVersion(Long id, int version);
}
