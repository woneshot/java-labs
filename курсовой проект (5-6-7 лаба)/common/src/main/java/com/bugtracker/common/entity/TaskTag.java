package com.bugtracker.common.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "task_tags")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskTag extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tag_id", nullable = false)
    private Tag tag;
}