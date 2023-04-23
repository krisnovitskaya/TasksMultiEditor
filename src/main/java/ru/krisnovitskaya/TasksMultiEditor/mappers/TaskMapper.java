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
        return TaskDto.builder()
                .id(entity.getId())
                .executor(userMapper.fromEntity(entity.getExecutor()))
                .controller(userMapper.fromEntity(entity.getController()))
                .title(entity.getTitle())
                .description(entity.getDescription())
                .version(entity.getVersion())
                .build();
    }
}
