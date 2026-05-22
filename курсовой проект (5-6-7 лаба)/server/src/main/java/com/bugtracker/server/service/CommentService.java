package com.bugtracker.server.service;

import com.bugtracker.common.dto.CommentDTO;
import com.bugtracker.common.entity.Comment;
import com.bugtracker.common.entity.Task;
import com.bugtracker.common.entity.User;
import com.bugtracker.server.repository.CommentRepository;
import com.bugtracker.server.repository.TaskRepository;
import com.bugtracker.server.repository.UserRepository;
import java.util.List;
import java.util.stream.Collectors;

public class CommentService {

    private final CommentRepository commentRepository = new CommentRepository();
    private final TaskRepository taskRepository = new TaskRepository();
    private final UserRepository userRepository = new UserRepository();

    public Comment addComment(Long taskId, Long authorId, String text) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Задача не найдена"));

        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new RuntimeException("Автор не найден"));

        Comment comment = new Comment();
        comment.setTask(task);
        comment.setAuthor(author);
        comment.setText(text);

        return commentRepository.save(comment);
    }

    public List<CommentDTO> getCommentsByTask(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Задача не найдена"));

        return commentRepository.findByTask(task).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public CommentDTO toDTO(Comment comment) {
        return CommentDTO.builder()
                .id(comment.getId())
                .taskId(comment.getTask().getId())
                .authorId(comment.getAuthor().getId())
                .authorName(comment.getAuthor().getFullName())
                .text(comment.getText())
                .createdAt(comment.getCreatedAt().toString())
                .build();
    }
}