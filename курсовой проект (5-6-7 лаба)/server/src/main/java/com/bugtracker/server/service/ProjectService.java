package com.bugtracker.server.service;

import com.bugtracker.common.dto.ProjectDTO;
import com.bugtracker.common.entity.Project;
import com.bugtracker.common.entity.ProjectMember;
import com.bugtracker.common.entity.User;
import com.bugtracker.server.repository.ProjectMemberRepository;
import com.bugtracker.server.repository.ProjectRepository;
import com.bugtracker.server.repository.UserRepository;
import java.util.List;
import java.util.stream.Collectors;

public class ProjectService {

    private final ProjectRepository projectRepository = new ProjectRepository();
    private final ProjectMemberRepository memberRepository = new ProjectMemberRepository();
    private final UserRepository userRepository = new UserRepository();

    public Project createProject(String name, String description, Long ownerId) {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("Владелец не найден"));

        Project project = new Project();
        project.setName(name);
        project.setDescription(description);
        project.setOwner(owner);

        Project saved = projectRepository.save(project);

        ProjectMember member = new ProjectMember();
        member.setProject(saved);
        member.setUser(owner);
        memberRepository.save(member);

        return saved;
    }

    public Project updateProject(Long id, String name, String description) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Проект не найден"));

        project.setName(name);
        project.setDescription(description);

        return projectRepository.update(project);
    }

    public void addMember(Long projectId, Long userId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Проект не найден"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        if (memberRepository.findByProjectAndUser(project, user).isPresent()) {
            throw new RuntimeException("Пользователь уже участник проекта");
        }

        ProjectMember member = new ProjectMember();
        member.setProject(project);
        member.setUser(user);
        memberRepository.save(member);
    }

    public void removeMember(Long projectId, Long userId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Проект не найден"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        ProjectMember member = memberRepository.findByProjectAndUser(project, user)
                .orElseThrow(() -> new RuntimeException("Участник не найден"));

        memberRepository.delete(member.getId());
    }

    public List<ProjectDTO> getAllProjects() {
        return projectRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<ProjectDTO> getProjectsByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        return memberRepository.findByUser(user).stream()
                .map(member -> toDTO(member.getProject()))
                .collect(Collectors.toList());
    }

    public List<User> getProjectMembers(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Проект не найден"));

        return memberRepository.findByProject(project).stream()
                .map(ProjectMember::getUser)
                .collect(Collectors.toList());
    }

    public ProjectDTO toDTO(Project project) {
        int membersCount = memberRepository.findByProject(project).size();

        return ProjectDTO.builder()
                .id(project.getId())
                .name(project.getName())
                .description(project.getDescription())
                .ownerId(project.getOwner().getId())
                .ownerName(project.getOwner().getFullName())
                .membersCount(membersCount)
                .build();
    }
}