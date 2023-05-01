package ru.krisnovitskaya.TasksMultiEditor.repositories;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.krisnovitskaya.TasksMultiEditor.entities.Task;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select t from Task t where t.id = :id")
    Optional<Task> findByIdLock(@Param("id") Long id);

    @Query("select t from Task t join fetch t.controller join fetch t.executor where t.id = :id")
    Optional<Task> findById(@Param("id") Long id);

    @Query("select t from Task t join fetch t.controller join fetch t.executor")
    List<Task> findAll();
}
