package com.bugtracker.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskDTO {
    private Long id;
    private String title;
    private String description;
    private String status;      // строка ("OPEN")
    private String priority;    // строка ("HIGH")
    private String type;        // строка ("BUG")
    private String deadline;
    private Long projectId;
    private String projectName;
    private Long assigneeId;
    private String assigneeName; // имя исполнителя
    private Long creatorId;
    private String creatorName;  // имя автора
}