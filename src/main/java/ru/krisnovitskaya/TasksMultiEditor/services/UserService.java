package ru.krisnovitskaya.TasksMultiEditor.services;

import ru.krisnovitskaya.TasksMultiEditor.dtos.UserDto;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<UserDto> findAll();
    String findEmailById(Long id);
}
