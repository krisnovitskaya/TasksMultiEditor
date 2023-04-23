package ru.krisnovitskaya.TasksMultiEditor.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.krisnovitskaya.TasksMultiEditor.repositories.RoleRepository;
import ru.krisnovitskaya.TasksMultiEditor.services.RoleService;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

}