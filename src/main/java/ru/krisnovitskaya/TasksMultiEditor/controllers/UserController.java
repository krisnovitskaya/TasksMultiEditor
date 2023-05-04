package ru.krisnovitskaya.TasksMultiEditor.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.krisnovitskaya.TasksMultiEditor.dtos.UserDto;
import ru.krisnovitskaya.TasksMultiEditor.services.UserService;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<UserDto> getAll() {
        log.debug("find all user");
        return userService.findAll();
    }
}
