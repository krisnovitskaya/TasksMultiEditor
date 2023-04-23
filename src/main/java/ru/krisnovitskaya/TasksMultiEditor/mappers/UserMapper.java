package ru.krisnovitskaya.TasksMultiEditor.mappers;

import org.springframework.stereotype.Component;
import ru.krisnovitskaya.TasksMultiEditor.dtos.UserDto;
import ru.krisnovitskaya.TasksMultiEditor.entities.User;

@Component
public class UserMapper {

    public UserDto fromEntity(User entity){
        return new UserDto(entity.getId(), entity.getUsername());
    }
}
