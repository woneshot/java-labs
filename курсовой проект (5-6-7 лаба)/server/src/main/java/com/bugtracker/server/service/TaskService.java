package com.bugtracker.server.service;

import com.bugtracker.common.dto.TaskDTO;
import com.bugtracker.common.entity.*;
import com.bugtracker.common.enums.TaskPriority;
import com.bugtracker.common.enums.TaskStatus;
import com.bugtracker.common.enums.TaskType;
import com.bugtracker.server.repository.*;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class TaskService {

    private final TaskRepository taskRepository = new TaskRepository();
    private final ProjectRepository projectRepository = new ProjectRepository();
    private final UserRepository userRepository = new UserRepository();
    private final TaskHistoryRepository historyRepository = new TaskHistoryRepository();

    public Task createTask(String title, String description, Long projectId, Long creatorId,
                           Long assigneeId, String priority, String type, LocalDate deadline) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Проект не найден"));

        User creator = userRepository.findById(creatorId)
                .orElseThrow(() -> new RuntimeException("Создатель не найден"));

        Task task = new Task();
        task.setTitle(title);
        task.setDescription(description);
        task.setProject(project);
        task.setCreator(creator);
        task.setStatus(TaskStatus.OPEN);
        task.setPriority(TaskPriority.valueOf(priority));
        task.setType(TaskType.valueOf(type));
        task.setDeadline(deadline);

        if (assigneeId != null) {
            User assignee = userRepository.findById(assigneeId)
                    .orElseThrow(() -> new RuntimeException("Исполнитель не найден"));
            task.setAssignee(assignee);
        }

        return taskRepository.save(task);
    }

    public Task updateTask(Long id, String title, String description, LocalDate deadline) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Задача не найдена"));

        task.setTitle(title);
        task.setDescription(description);
        task.setDeadline(deadline);

        return taskRepository.update(task);
    }

    public Task changeStatus(Long taskId, String newStatus, Long userId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Задача не найдена"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        String oldStatus = task.getStatus().name();
        task.setStatus(TaskStatus.valueOf(newStatus));
        Task updated = taskRepository.save(task);

        saveHistory(task, user, "Status", oldStatus, newStatus);

        return updated;
    }

    public Task changePriority(Long taskId, String newPriority, Long userId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Задача не найдена"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        String oldPriority = task.getPriority().name();
        task.setPriority(TaskPriority.valueOf(newPriority));
        Task updated = taskRepository.save(task);

        saveHistory(task, user, "Priority", oldPriority, newPriority);

        return updated;
    }

    public Task assignTask(Long taskId, Long assigneeId, Long userId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Задача не найдена"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        String oldAssignee = task.getAssignee() != null ? task.getAssignee().getFullName() : "не назначен";

        User assignee = userRepository.findById(assigneeId)
                .orElseThrow(() -> new RuntimeException("Исполнитель не найден"));

        task.setAssignee(assignee);
        Task updated = taskRepository.save(task);

        saveHistory(task, user, "Assignee", oldAssignee, assignee.getFullName());

        return updated;
    }

    public List<TaskDTO> getTasksByProject(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Проект не найден"));

        return taskRepository.findByProject(project).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<TaskDTO> searchTasks(String keyword) {
        return taskRepository.searchByTitle(keyword).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private void saveHistory(Task task, User user, String field, String oldValue, String newValue) {
        TaskHistory history = new TaskHistory();
        history.setTask(task);
        history.setUser(user);
        history.setFieldName(field);
        history.setOldValue(oldValue);
        history.setNewValue(newValue);
        historyRepository.save(history);
    }

    public TaskDTO toDTO(Task task) {
        return TaskDTO.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus().name())
                .priority(task.getPriority().name())
                .type(task.getType().name())
                .deadline(task.getDeadline() != null ? task.getDeadline().toString() : null)
                .projectId(task.getProject().getId())
                .projectName(task.getProject().getName())
                .creatorId(task.getCreator().getId())
                .creatorName(task.getCreator().getFullName())
                .assigneeId(task.getAssignee() != null ? task.getAssignee().getId() : null)
                .assigneeName(task.getAssignee() != null ? task.getAssignee().getFullName() : null)
                .build();
    }
}