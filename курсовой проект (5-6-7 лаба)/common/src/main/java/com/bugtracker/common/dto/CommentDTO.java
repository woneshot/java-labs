package com.bugtracker.common.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {
    private Long id;
    private Long taskId;
    private Long authorId;
    private String authorName;
    private String text;
    private String createdAt;
}