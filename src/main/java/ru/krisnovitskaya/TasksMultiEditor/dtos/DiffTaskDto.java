package ru.krisnovitskaya.TasksMultiEditor.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@Builder
@Getter
@ToString
public class DiffTaskDto {
    private Long id;
    private Long theirExecutor;
    private Long yourExecutor;
    private Long theirController;
    private Long yourController;
    private String theirTitle;
    private String yourTitle;
    private String diffTitle;
    private String theirDescription;
    private String yourDescription;
    private String diffDescription;
    private LocalDate theirDeadline;
    private LocalDate yourDeadline;
    private int theirVersion;
    private String lastModifiedBy;
    private LocalDateTime lastModifiedDate;
}
