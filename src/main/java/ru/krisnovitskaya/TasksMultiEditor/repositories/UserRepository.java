package ru.krisnovitskaya.TasksMultiEditor.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.krisnovitskaya.TasksMultiEditor.entities.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    @Query("select u.email from User u where u.id = :id")
    Optional<String> findEmailById(@Param("id") Long id);
}
