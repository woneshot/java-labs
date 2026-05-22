package com.bugtracker.common.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectMemberDTO {
    private Long id;
    private Long projectId;
    private Long userId;
    private String userName;
    private String userRole;
    private String joinedAt;
}