package ru.krisnovitskaya.TasksMultiEditor.listeners;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import ru.krisnovitskaya.TasksMultiEditor.configs.JmsConfig;
import ru.krisnovitskaya.TasksMultiEditor.diff.DiffComputeHelper;
import ru.krisnovitskaya.TasksMultiEditor.dtos.HistoryDiffTaskDto;
import ru.krisnovitskaya.TasksMultiEditor.dtos.TaskDto;
import ru.krisnovitskaya.TasksMultiEditor.dtos.TaskHistoryDto;
import ru.krisnovitskaya.TasksMultiEditor.services.MailSenderService;
import ru.krisnovitskaya.TasksMultiEditor.services.TaskHistoryService;

@Component
@RequiredArgsConstructor
@Slf4j
public class TaskChangeListener {
    private final MailSenderService mailSenderService;

    private final ObjectMapper objectMapper;

    private final DiffComputeHelper diffComputeHelper;

    private final TaskHistoryService historyTaskService;

    @JmsListener(destination = JmsConfig.TASK_CREATE)
    public void listenCreate(@Payload TaskDto newTask) throws JsonProcessingException {
        log.debug("listenCreate {}", newTask);
        mailSenderService.sendMail(newTask.executor().email(), "New Task Assign", objectMapper.writeValueAsString(newTask));
        if (newTask.controller() != null && !newTask.controller().id().equals(newTask.executor().id())) {
            mailSenderService.sendMail(newTask.controller().email(), "New Task to Control", objectMapper.writeValueAsString(newTask));
        }
    }

    @JmsListener(destination = JmsConfig.TASK_CHANGE)
    public void listenChange(@Payload TaskDto updatedTask) throws JsonProcessingException {
        log.debug("update {}", updatedTask);
        TaskHistoryDto historyTaskVersionBefore = historyTaskService.findHistoryByTaskIdAndVersion(updatedTask.id(), updatedTask.version() - 1);
        HistoryDiffTaskDto historyDiffTaskDto = diffComputeHelper.computeHistoryDiff(historyTaskVersionBefore, updatedTask);
        mailSenderService.sendMail(updatedTask.executor().email(), "Task Updated", objectMapper.writeValueAsString(historyDiffTaskDto));
        if (updatedTask.controller() != null && !updatedTask.controller().id().equals(updatedTask.executor().id())) {
            mailSenderService.sendMail(updatedTask.controller().email(), "Task Updated", objectMapper.writeValueAsString(historyDiffTaskDto));
        }
    }
}
