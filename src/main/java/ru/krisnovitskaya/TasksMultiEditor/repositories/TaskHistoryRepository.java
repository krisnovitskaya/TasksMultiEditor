package ru.krisnovitskaya.TasksMultiEditor.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.krisnovitskaya.TasksMultiEditor.entities.TaskHistory;

import java.util.List;
import java.util.Optional;

public interface TaskHistoryRepository extends JpaRepository<TaskHistory, Long> {

    @Query("select t from TaskHistory t where t.taskId = :taskId")
    List<TaskHistory> findHistoryByTaskId(@Param("taskId") Long taskId);

    Optional<TaskHistory> findOneByTaskIdAndVersion(@Param("taskId") Long taskId, @Param("version") int version);
}
