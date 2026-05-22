package com.bugtracker.common.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskHistoryDTO {
    private Long id;
    private Long taskId;
    private Long userId;
    private String userName;
    private String fieldName;
    private String oldValue;
    private String newValue;
    private String changedAt;
}