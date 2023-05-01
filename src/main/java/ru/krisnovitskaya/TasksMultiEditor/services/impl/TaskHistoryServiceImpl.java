package ru.krisnovitskaya.TasksMultiEditor.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.krisnovitskaya.TasksMultiEditor.dtos.TaskHistoryDto;
import ru.krisnovitskaya.TasksMultiEditor.entities.TaskHistory;
import ru.krisnovitskaya.TasksMultiEditor.exceptions.ResourceNotFoundException;
import ru.krisnovitskaya.TasksMultiEditor.repositories.TaskHistoryRepository;
import ru.krisnovitskaya.TasksMultiEditor.services.TaskHistoryService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskHistoryServiceImpl implements TaskHistoryService {
    private final TaskHistoryRepository repository;
    @Override
    public List<TaskHistoryDto> findHistoryByTaskId(Long id) {
        return repository.findHistoryByTaskId(id).stream().map(TaskHistoryDto::new).toList();
    }

    @Override
    public TaskHistoryDto findHistoryByTaskIdAndVersion(Long id, int version) {
        TaskHistory taskHistory = repository.findHistoryByTaskIdANdVersion(id, version).orElseThrow(() -> new ResourceNotFoundException(String.format("User with id=%d not exists", id)));
        return new TaskHistoryDto(taskHistory);
    }
}
