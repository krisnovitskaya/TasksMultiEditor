package ru.krisnovitskaya.TasksMultiEditor.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "tasks_history_view")
public class TaskHistory {
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "operation")
    private String operation;

    @Column(name = "task_id")
    private Long taskId;

    @Column(name = "executor_id_new")
    private Long executorId;

    @Column(name = "controller_id_new")
    private Long controllerId;

    @Column(name = "title_new")
    private String title;

    @Column(name = "description_new")
    private String description;

    @Column(name = "deadline_new")
    private LocalDate deadline;

    @Column(name = "version")
    private int version;

    @Column(name = "modified_by")
    private String modifiedBy;

    @Column(name = "modified_date")
    private LocalDateTime modifiedDate;
}
