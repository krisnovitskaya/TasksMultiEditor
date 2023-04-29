package ru.krisnovitskaya.TasksMultiEditor.diff;

import com.github.difflib.text.DiffRowGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.krisnovitskaya.TasksMultiEditor.dtos.DiffTaskDto;
import ru.krisnovitskaya.TasksMultiEditor.dtos.TaskDto;
import ru.krisnovitskaya.TasksMultiEditor.dtos.UpdateTaskDto;

import java.util.Arrays;
import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
public class DiffComputeHelper {

    private final DiffRowGenerator generator;

    public DiffTaskDto computeDiff(TaskDto old, UpdateTaskDto wanted) {
        return DiffTaskDto.builder()
                .id(old.id())
                .theirExecutor(old.executor().id())
                .theirController(old.controller().id())
                .youController(wanted.controllerId())
                .youExecutor(wanted.executorId())
                .theirTitle(old.title())
                .youTitle(wanted.title())
                .diffTitle(diff(old.title(), wanted.title()))
                .theirDescription(old.description())
                .youDescription(wanted.description())
                .diffDescription(diff(old.description(), wanted.description()))
                .theirVersion(old.version())
                .lastModifiedBy(old.lastModifiedBy())
                .lastModifiedDate(old.lastModifiedDate())
                .build();
    }


    private String diff(String origin, String update) {
        return generator.generateDiffRows(
                Arrays.asList(origin.split("\n")),
                Arrays.asList(update.split("\n"))
        ).stream().map(row -> row.getOldLine()).collect(Collectors.joining("\n"));
    }

}
