package ru.krisnovitskaya.TasksMultiEditor.repositories;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.krisnovitskaya.TasksMultiEditor.entities.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {

    @Lock(LockModeType.PESSIMISTIC_READ)
    @Query("select t from Task t where t.id = :id")
    Task findByIdLock(@Param("id") Long id);
}
