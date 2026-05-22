package com.bugtracker.server.service;

import com.bugtracker.common.dto.TaskHistoryDTO;
import com.bugtracker.common.entity.Task;
import com.bugtracker.common.entity.TaskHistory;
import com.bugtracker.server.repository.TaskHistoryRepository;
import com.bugtracker.server.repository.TaskRepository;
import java.util.List;
import java.util.stream.Collectors;

public class TaskHistoryService {

    private final TaskHistoryRepository historyRepository = new TaskHistoryRepository();
    private final TaskRepository taskRepository = new TaskRepository();

    public List<TaskHistoryDTO> getTaskHistory(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Задача не найдена"));

        return historyRepository.findByTask(task).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public TaskHistoryDTO toDTO(TaskHistory history) {
        return TaskHistoryDTO.builder()
                .id(history.getId())
                .taskId(history.getTask().getId())
                .userId(history.getUser().getId())
                .userName(history.getUser().getFullName())
                .fieldName(history.getFieldName())
                .oldValue(history.getOldValue())
                .newValue(history.getNewValue())
                .changedAt(history.getChangedAt().toString())
                .build();
    }
}