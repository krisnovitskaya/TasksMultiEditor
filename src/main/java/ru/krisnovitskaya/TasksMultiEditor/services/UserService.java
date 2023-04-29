package ru.krisnovitskaya.TasksMultiEditor.services;

import ru.krisnovitskaya.TasksMultiEditor.dtos.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> findAll();
}
