package ru.krisnovitskaya.TasksMultiEditor.dtos;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskDto {
    private Long id;

    private UserDto executor;

    private UserDto controller;

    private String title;

    private String description;

    private int version;
}
