package ru.krisnovitskaya.TasksMultiEditor.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.krisnovitskaya.TasksMultiEditor.dtos.UserDto;
import ru.krisnovitskaya.TasksMultiEditor.entities.Role;
import ru.krisnovitskaya.TasksMultiEditor.entities.User;
import ru.krisnovitskaya.TasksMultiEditor.exceptions.ResourceNotFoundException;
import ru.krisnovitskaya.TasksMultiEditor.mappers.UserMapper;
import ru.krisnovitskaya.TasksMultiEditor.repositories.UserRepository;
import ru.krisnovitskaya.TasksMultiEditor.services.UserService;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserDetailsService, UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(String.format("User '%s' not found", username)));
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), mapRolesToAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }

    public List<UserDto> findAll() {
        return userRepository.findAll().stream().map(userMapper::fromEntity).collect(Collectors.toList());
    }

    @Override
    public String findEmailById(Long id) {
        return userRepository.findEmailById(id).orElseThrow(() -> new ResourceNotFoundException(String.format("User with id=%d not exists", id)));
    }
}