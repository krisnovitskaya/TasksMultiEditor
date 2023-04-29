package ru.krisnovitskaya.TasksMultiEditor.exceptions;

import ru.krisnovitskaya.TasksMultiEditor.dtos.DiffTaskDto;

public class MultiUpdateException extends RuntimeException {

    private final DiffTaskDto diffTaskDto;
    public MultiUpdateException(DiffTaskDto diffTaskDto) {
        super();
        this.diffTaskDto = diffTaskDto;
    }

    public DiffTaskDto getDiffTaskDto() {
        return diffTaskDto;
    }
}
