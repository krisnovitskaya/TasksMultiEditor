package ru.krisnovitskaya.TasksMultiEditor.diff;

import com.github.difflib.text.DiffRowGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.krisnovitskaya.TasksMultiEditor.dtos.*;

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
                .yourController(wanted.controllerId())
                .yourExecutor(wanted.executorId())
                .theirTitle(old.title())
                .yourTitle(wanted.title())
                .diffTitle(diff(old.title(), wanted.title()))
                .theirDescription(old.description())
                .yourDescription(wanted.description())
                .diffDescription(diff(old.description(), wanted.description()))
                .theirDeadline(old.deadline())
                .yourDeadline(wanted.deadline())
                .theirVersion(old.version())
                .lastModifiedBy(old.lastModifiedBy())
                .lastModifiedDate(old.lastModifiedDate())
                .build();
    }

    public HistoryDiffTaskDto computeHistoryDiff(TaskHistoryDto old, TaskDto updated) {
        return HistoryDiffTaskDto.builder()
                .id(old.id())
                .beforeExecutor(old.executorId())
                .beforeController(old.controllerId())
                .nowController(updated.controller() != null ? updated.controller().id() : null)
                .nowExecutor(updated.executor().id())
                .beforeTitle(old.title())
                .nowTitle(updated.title())
                .diffTitle(diff(old.title(), updated.title()))
                .beforeDescription(old.description())
                .nowDescription(updated.description())
                .diffDescription(diff(old.description(), updated.description()))
                .beforeDeadline(old.deadline())
                .nowDeadline(updated.deadline())
                .nowVersion(old.version())
                .beforeModifiedBy(old.modifiedBy())
                .beforeModifiedDate(old.modifiedDate())
                .nowModifiedBy(updated.lastModifiedBy())
                .nowModifiedDate(updated.lastModifiedDate())
                .build();
    }


    private String diff(String origin, String update) {
        return generator.generateDiffRows(
                Arrays.asList(origin.split("\n")),
                Arrays.asList(update.split("\n"))
        ).stream().map(row -> row.getOldLine()).collect(Collectors.joining("\n"));
    }

}
