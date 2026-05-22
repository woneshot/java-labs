package com.bugtracker.common.entity;

import com.bugtracker.common.enums.*;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Task extends BaseEntity {

    @Column(nullable = false, length = 255)
    private String title;

    @Column(columnDefinition = "TEXT") // много текста
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "assignee_id")
    private User assignee;

    @Enumerated(EnumType.STRING) // если не поставить STRING, Hibernate запишет число (порядковый номер 0, 1, 2), а так будет слово капсом
    @Column(nullable = false)
    private TaskStatus status = TaskStatus.OPEN;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskPriority priority = TaskPriority.MEDIUM;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskType type = TaskType.TASK;

    @Column
    private LocalDate deadline;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Override
    protected void onCreate() {
        super.onCreate();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate // выполнится сразу перед обновлением
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}