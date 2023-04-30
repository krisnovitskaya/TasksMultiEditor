package ru.krisnovitskaya.TasksMultiEditor.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.krisnovitskaya.TasksMultiEditor.dtos.DiffTaskDto;
import ru.krisnovitskaya.TasksMultiEditor.utils.SecurityUtils;

import java.sql.SQLException;

@ControllerAdvice
@Slf4j
public class GlobalExceptionsHandler {
    @ExceptionHandler
    public ResponseEntity<AppError> handleResourceNotFoundException(ResourceNotFoundException e) {
        e.printStackTrace();
        return new ResponseEntity<>(new AppError("RESOURCE_NOT_FOUND", e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<DiffTaskDto> handleMultiUpdateException(MultiUpdateException e) {
        log.info("handle MultiUpdateException: username={}, taskId={}", SecurityUtils.getCurrentUser(), e.getDiffTaskDto().getId());
        return new ResponseEntity<>(e.getDiffTaskDto(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<AppError> handleSQLException(SQLException e) {
        log.info("handle SQLException: username={}, message={}", SecurityUtils.getCurrentUser(), e.getMessage());
        e.printStackTrace();
        return new ResponseEntity<>(new AppError("BAD_REQUEST", e.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
