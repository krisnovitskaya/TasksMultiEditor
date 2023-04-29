package ru.krisnovitskaya.TasksMultiEditor.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Builder
@Getter
public class DiffTaskDto {
    private Long id;
    private Long theirExecutor;
    private Long youExecutor;
    private Long theirController;
    private Long youController;
    private String theirTitle;
    private String youTitle;
    private String diffTitle;
    private String theirDescription;
    private String youDescription;
    private String diffDescription;
    private int theirVersion;
    private String lastModifiedBy;
    private LocalDateTime lastModifiedDate;
}
