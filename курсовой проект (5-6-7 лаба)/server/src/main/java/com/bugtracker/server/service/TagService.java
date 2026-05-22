package com.bugtracker.server.service;

import com.bugtracker.common.dto.TagDTO;
import com.bugtracker.common.entity.Tag;
import com.bugtracker.common.entity.Task;
import com.bugtracker.common.entity.TaskTag;
import com.bugtracker.server.repository.TagRepository;
import com.bugtracker.server.repository.TaskRepository;
import com.bugtracker.server.repository.TaskTagRepository;
import java.util.List;
import java.util.stream.Collectors;

public class TagService {

    private final TagRepository tagRepository = new TagRepository();
    private final TaskRepository taskRepository = new TaskRepository();
    private final TaskTagRepository taskTagRepository = new TaskTagRepository();

    public void addTagToTask(Long taskId, Long tagId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("задача не найдена"));

        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new RuntimeException("тег не найден"));

        if (taskTagRepository.findByTaskAndTag(task, tag).isPresent()) {
            throw new RuntimeException("тег уже добавлен к задаче");
        }

        TaskTag taskTag = new TaskTag();
        taskTag.setTask(task);
        taskTag.setTag(tag);
        taskTagRepository.save(taskTag);
    }

    public void removeTagFromTask(Long taskId, Long tagId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Задача не найдена"));

        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new RuntimeException("Тег не найден"));

        TaskTag taskTag = taskTagRepository.findByTaskAndTag(task, tag)
                .orElseThrow(() -> new RuntimeException("Связь не найдена"));

        taskTagRepository.delete(taskTag.getId());
    }

    public List<TagDTO> getAllTags() {
        return tagRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<TagDTO> getTaskTags(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Задача не найдена"));

        return taskTagRepository.findByTask(task).stream()
                .map(tt -> toDTO(tt.getTag()))
                .collect(Collectors.toList());
    }

    public TagDTO toDTO(Tag tag) {
        return TagDTO.builder()
                .id(tag.getId())
                .name(tag.getName())
                .color(tag.getColor())
                .build();
    }
}