package ru.krisnovitskaya.TasksMultiEditor.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.krisnovitskaya.TasksMultiEditor.entities.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
