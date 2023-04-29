package ru.krisnovitskaya.TasksMultiEditor.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.krisnovitskaya.TasksMultiEditor.dtos.TaskDto;
import ru.krisnovitskaya.TasksMultiEditor.entities.Task;

@Component
@RequiredArgsConstructor
public class TaskMapper {
    private final UserMapper userMapper;

    public TaskDto fromEntity(Task entity) {
        return new TaskDto(
                entity.getId()
                , userMapper.fromEntity(entity.getExecutor())
                , userMapper.fromEntity(entity.getController())
                , entity.getTitle()
                , entity.getDescription()
                , entity.getVersion()
                , entity.getLastModifiedBy()
                , entity.getLastModifiedDate()
                );
    }
}
