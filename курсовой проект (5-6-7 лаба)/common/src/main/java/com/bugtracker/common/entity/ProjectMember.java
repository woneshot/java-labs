package com.bugtracker.common.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "project_members")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectMember extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY) // ленивая загрузка: сразу НЕ подгружаем проект когда получаем участника
    @JoinColumn(name = "project_id", nullable = false) // внешний ключ
    private Project project;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "joined_at")
    private LocalDateTime joinedAt;

    @Override
    protected void onCreate() {
        super.onCreate();
        if (joinedAt == null) {
            joinedAt = LocalDateTime.now();
        }
    }
}