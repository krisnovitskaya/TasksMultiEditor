package ru.krisnovitskaya.TasksMultiEditor.dtos;


import java.time.LocalDate;
import java.time.LocalDateTime;

public record TaskDto(Long id, UserDto executor, UserDto controller, String title, String description, LocalDate deadline,
                      int version, String lastModifiedBy, LocalDateTime lastModifiedDate) {

}
