package ru.krisnovitskaya.TasksMultiEditor.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.krisnovitskaya.TasksMultiEditor.dtos.DiffTaskDto;

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
        e.printStackTrace();
        return new ResponseEntity<>(e.getDiffTaskDto(), HttpStatus.BAD_REQUEST);
    }
}
