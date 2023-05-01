package ru.krisnovitskaya.TasksMultiEditor.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@Builder
@Getter
public class HistoryDiffTaskDto {
    private Long id;
    private Long beforeExecutor;
    private Long nowExecutor;
    private Long beforeController;
    private Long nowController;
    private String beforeTitle;
    private String nowTitle;
    private String diffTitle;
    private String beforeDescription;
    private String nowDescription;
    private String diffDescription;
    private LocalDate beforeDeadline;
    private LocalDate nowDeadline;
    private int nowVersion;
    private String beforeModifiedBy;
    private String nowModifiedBy;
    private LocalDateTime beforeModifiedDate;
    private LocalDateTime nowModifiedDate;
}
