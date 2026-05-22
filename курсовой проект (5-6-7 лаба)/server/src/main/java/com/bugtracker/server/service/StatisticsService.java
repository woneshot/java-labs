package com.bugtracker.server.service;

import com.bugtracker.common.entity.Project;
import com.bugtracker.common.entity.Task;
import com.bugtracker.common.enums.TaskStatus;
import com.bugtracker.server.repository.ProjectRepository;
import com.bugtracker.server.repository.TaskRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatisticsService {

    private final ProjectRepository projectRepository = new ProjectRepository();
    private final TaskRepository taskRepository = new TaskRepository();

    public Map<String, Object> getProjectStatistics(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Проект не найден"));

        List<Task> tasks = taskRepository.findByProject(project);

        Map<String, Object> stats = new HashMap<>();
        stats.put("TotalTasks", tasks.size());
        stats.put("OpenTasks", countByStatus(tasks, TaskStatus.OPEN));
        stats.put("InProgressTasks", countByStatus(tasks, TaskStatus.IN_PROGRESS));
        stats.put("ReviewTasks", countByStatus(tasks, TaskStatus.REVIEW));
        stats.put("TestingTasks", countByStatus(tasks, TaskStatus.TESTING));
        stats.put("ClosedTasks", countByStatus(tasks, TaskStatus.CLOSED));

        return stats;
    }

    private long countByStatus(List<Task> tasks, TaskStatus status) {
        return tasks.stream()
                .filter(t -> t.getStatus() == status)
                .count();
    }
}