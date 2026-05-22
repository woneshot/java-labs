package com.bugtracker.server.network;

import com.bugtracker.common.dto.*;
import com.bugtracker.common.entity.User;
import com.bugtracker.common.enums.ActionType;
import com.bugtracker.server.service.*;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.*;
import java.net.Socket;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ClientHandler implements Runnable {

    private final Socket socket;
    private final Gson gson = new Gson();

    private final AuthService authService = new AuthService();
    private final UserService userService = new UserService();
    private final ProjectService projectService = new ProjectService();
    private final TaskService taskService = new TaskService();
    private final CommentService commentService = new CommentService();
    private final TagService tagService = new TagService();
    private final TaskHistoryService historyService = new TaskHistoryService();
    private final StatisticsService statisticsService = new StatisticsService();

    private User currentUser;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
        ) {
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println("Получено: " + line);

                Request request = gson.fromJson(line, Request.class);
                Response response = processRequest(request);

                String jsonResponse = gson.toJson(response);
                out.println(jsonResponse);
                System.out.println("Отправлено: " + jsonResponse);
            }
        } catch (IOException e) {
            System.err.println("Ошибка клиента: " + e.getMessage());
        } finally {
            try {
                socket.close();
                System.out.println("Клиент отключился");
            } catch (IOException e) {
                System.err.println("Ошибка при закрытии сокета: " + e.getMessage());
            }
        }
    }

    private Response processRequest(Request request) {
        try {
            ActionType action = request.getAction();
            String data = request.getData();

            return switch (action) {
                // аутентификация
                case LOGIN -> handleLogin(data);
                case LOGOUT -> handleLogout();

                // пользователи
                case CREATE_USER -> handleCreateUser(data);
                case GET_ALL_USERS -> handleGetAllUsers();
                case UPDATE_USER -> handleUpdateUser(data);
                case BLOCK_USER -> handleBlockUser(data);
                case UNBLOCK_USER -> handleUnblockUser(data);

                // проекты
                case CREATE_PROJECT -> handleCreateProject(data);
                case UPDATE_PROJECT -> handleUpdateProject(data);
                case GET_ALL_PROJECTS -> handleGetAllProjects();
                case GET_USER_PROJECTS -> handleGetUserProjects(data);
                case ADD_PROJECT_MEMBER -> handleAddProjectMember(data);
                case REMOVE_PROJECT_MEMBER -> handleRemoveProjectMember(data);
                case GET_PROJECT_MEMBERS -> handleGetProjectMembers(data);

                // задачи
                case CREATE_TASK -> handleCreateTask(data);
                case UPDATE_TASK -> handleUpdateTask(data);
                case CHANGE_TASK_STATUS -> handleChangeTaskStatus(data);
                case CHANGE_TASK_PRIORITY -> handleChangeTaskPriority(data);
                case ASSIGN_TASK -> handleAssignTask(data);
                case GET_PROJECT_TASKS -> handleGetProjectTasks(data);
                case SEARCH_TASKS -> handleSearchTasks(data);

                // комментарии
                case ADD_COMMENT -> handleAddComment(data);
                case GET_TASK_COMMENTS -> handleGetTaskComments(data);

                // теги
                case ADD_TAG_TO_TASK -> handleAddTagToTask(data);
                case REMOVE_TAG_FROM_TASK -> handleRemoveTagFromTask(data);
                case GET_ALL_TAGS -> handleGetAllTags();
                case GET_TASK_TAGS -> handleGetTaskTags(data);

                // история
                case GET_TASK_HISTORY -> handleGetTaskHistory(data);

                // статистика
                case GET_PROJECT_STATISTICS -> handleGetProjectStatistics(data);
                default -> new Response(false, "Неизвестная команда", null);
            };
        } catch (Exception e) {
            return new Response(false, "Ошибка: " + e.getMessage(), null);
        }
    }

    // === аутентификация ===

    private Response handleLogin(String data) {
        JsonObject json = JsonParser.parseString(data).getAsJsonObject();
        String username = json.get("username").getAsString();
        String password = json.get("password").getAsString();

        Optional<User> userOpt = authService.login(username, password);
        if (userOpt.isEmpty()) {
            return new Response(false, "Неверный логин или пароль", null);
        }

        currentUser = userOpt.get();
        UserDTO dto = userService.toDTO(currentUser);
        return new Response(true, "Успешный вход", gson.toJson(dto));
    }

    private Response handleLogout() {
        currentUser = null;
        return new Response(true, "Выход выполнен", null);
    }

    // === пользователи ===

    private Response handleCreateUser(String data) {
        JsonObject json = JsonParser.parseString(data).getAsJsonObject();
        String username = json.get("username").getAsString();
        String password = json.get("password").getAsString();
        String email = json.get("email").getAsString();
        String fullName = json.get("fullName").getAsString();
        String role = json.get("role").getAsString();

        User user = userService.createUser(username, password, email, fullName, role);
        UserDTO dto = userService.toDTO(user);
        return new Response(true, "Пользователь создан", gson.toJson(dto));
    }

    private Response handleGetAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return new Response(true, "Список пользователей", gson.toJson(users));
    }

    private Response handleUpdateUser(String data) {
        JsonObject json = JsonParser.parseString(data).getAsJsonObject();
        Long id = json.get("id").getAsLong();
        String email = json.get("email").getAsString();
        String fullName = json.get("fullName").getAsString();
        String role = json.get("role").getAsString();

        User user = userService.updateUser(id, email, fullName, role);
        UserDTO dto = userService.toDTO(user);
        return new Response(true, "Пользователь обновлён", gson.toJson(dto));
    }

    private Response handleBlockUser(String data) {
        JsonObject json = JsonParser.parseString(data).getAsJsonObject();
        Long id = json.get("id").getAsLong();
        userService.blockUser(id);
        return new Response(true, "Пользователь заблокирован", null);
    }

    private Response handleUnblockUser(String data) {
        JsonObject json = JsonParser.parseString(data).getAsJsonObject();
        Long id = json.get("id").getAsLong();
        userService.unblockUser(id);
        return new Response(true, "Пользователь разблокирован", null);
    }

    // === проекты ===

    private Response handleCreateProject(String data) {
        JsonObject json = JsonParser.parseString(data).getAsJsonObject();
        String name = json.get("name").getAsString();
        String description = json.has("description") ? json.get("description").getAsString() : "";
        Long ownerId = json.get("ownerId").getAsLong();

        projectService.createProject(name, description, ownerId);
        return new Response(true, "Проект создан", null);
    }

    private Response handleUpdateProject(String data) {
        JsonObject json = JsonParser.parseString(data).getAsJsonObject();
        Long id = json.get("id").getAsLong();
        String name = json.get("name").getAsString();
        String description = json.has("description") ? json.get("description").getAsString() : "";

        projectService.updateProject(id, name, description);
        return new Response(true, "Проект обновлён", null);
    }

    private Response handleGetAllProjects() {
        List<ProjectDTO> projects = projectService.getAllProjects();
        return new Response(true, "Список проектов", gson.toJson(projects));
    }

    private Response handleGetUserProjects(String data) {
        JsonObject json = JsonParser.parseString(data).getAsJsonObject();
        Long userId = json.get("userId").getAsLong();

        List<ProjectDTO> projects = projectService.getProjectsByUser(userId);
        return new Response(true, "Проекты пользователя", gson.toJson(projects));
    }

    private Response handleAddProjectMember(String data) {
        JsonObject json = JsonParser.parseString(data).getAsJsonObject();
        Long projectId = json.get("projectId").getAsLong();
        Long userId = json.get("userId").getAsLong();

        projectService.addMember(projectId, userId);
        return new Response(true, "Участник добавлен", null);
    }

    private Response handleRemoveProjectMember(String data) {
        JsonObject json = JsonParser.parseString(data).getAsJsonObject();
        Long projectId = json.get("projectId").getAsLong();
        Long userId = json.get("userId").getAsLong();

        projectService.removeMember(projectId, userId);
        return new Response(true, "Участник удалён", null);
    }

    private Response handleGetProjectMembers(String data) {
        JsonObject json = JsonParser.parseString(data).getAsJsonObject();
        Long projectId = json.get("projectId").getAsLong();

        List<User> members = projectService.getProjectMembers(projectId);
        List<UserDTO> dtos = members.stream().map(userService::toDTO).toList();
        return new Response(true, "Участники проекта", gson.toJson(dtos));
    }

    // === задачи ===

    private Response handleCreateTask(String data) {
        JsonObject json = JsonParser.parseString(data).getAsJsonObject();
        String title = json.get("title").getAsString();
        String description = json.has("description") ? json.get("description").getAsString() : "";
        Long projectId = json.get("projectId").getAsLong();
        Long creatorId = json.get("creatorId").getAsLong();
        Long assigneeId = json.has("assigneeId") && !json.get("assigneeId").isJsonNull()
                ? json.get("assigneeId").getAsLong() : null;
        String priority = json.get("priority").getAsString();
        String type = json.get("type").getAsString();
        LocalDate deadline = json.has("deadline") && !json.get("deadline").isJsonNull()
                ? LocalDate.parse(json.get("deadline").getAsString()) : null;

        taskService.createTask(title, description, projectId, creatorId, assigneeId, priority, type, deadline);
        return new Response(true, "Задача создана", null);
    }

    private Response handleUpdateTask(String data) {
        JsonObject json = JsonParser.parseString(data).getAsJsonObject();
        Long id = json.get("id").getAsLong();
        String title = json.get("title").getAsString();
        String description = json.has("description") ? json.get("description").getAsString() : "";
        LocalDate deadline = json.has("deadline") && !json.get("deadline").isJsonNull()
                ? LocalDate.parse(json.get("deadline").getAsString()) : null;

        taskService.updateTask(id, title, description, deadline);
        return new Response(true, "Задача обновлена", null);
    }

    private Response handleChangeTaskStatus(String data) {
        JsonObject json = JsonParser.parseString(data).getAsJsonObject();
        Long taskId = json.get("taskId").getAsLong();
        String status = json.get("status").getAsString();
        Long userId = json.get("userId").getAsLong();

        taskService.changeStatus(taskId, status, userId);
        return new Response(true, "Статус изменён", null);
    }

    private Response handleChangeTaskPriority(String data) {
        JsonObject json = JsonParser.parseString(data).getAsJsonObject();
        Long taskId = json.get("taskId").getAsLong();
        String priority = json.get("priority").getAsString();
        Long userId = json.get("userId").getAsLong();

        taskService.changePriority(taskId, priority, userId);
        return new Response(true, "Приоритет изменён", null);
    }

    private Response handleAssignTask(String data) {
        JsonObject json = JsonParser.parseString(data).getAsJsonObject();
        Long taskId = json.get("taskId").getAsLong();
        Long assigneeId = json.get("assigneeId").getAsLong();
        Long userId = json.get("userId").getAsLong();

        taskService.assignTask(taskId, assigneeId, userId);
        return new Response(true, "Исполнитель назначен", null);
    }

    private Response handleGetProjectTasks(String data) {
        JsonObject json = JsonParser.parseString(data).getAsJsonObject();
        Long projectId = json.get("projectId").getAsLong();

        List<TaskDTO> tasks = taskService.getTasksByProject(projectId);
        return new Response(true, "Задачи проекта", gson.toJson(tasks));
    }

    private Response handleSearchTasks(String data) {
        JsonObject json = JsonParser.parseString(data).getAsJsonObject();
        String keyword = json.get("keyword").getAsString();

        List<TaskDTO> tasks = taskService.searchTasks(keyword);
        return new Response(true, "Результаты поиска", gson.toJson(tasks));
    }

    // === комментарии ===

    private Response handleAddComment(String data) {
        JsonObject json = JsonParser.parseString(data).getAsJsonObject();
        Long taskId = json.get("taskId").getAsLong();
        Long authorId = json.get("authorId").getAsLong();
        String text = json.get("text").getAsString();

        commentService.addComment(taskId, authorId, text);
        return new Response(true, "Комментарий добавлен", null);
    }

    private Response handleGetTaskComments(String data) {
        JsonObject json = JsonParser.parseString(data).getAsJsonObject();
        Long taskId = json.get("taskId").getAsLong();

        List<CommentDTO> comments = commentService.getCommentsByTask(taskId);
        return new Response(true, "Комментарии задачи", gson.toJson(comments));
    }

    // === теги ===

    private Response handleAddTagToTask(String data) {
        JsonObject json = JsonParser.parseString(data).getAsJsonObject();
        Long taskId = json.get("taskId").getAsLong();
        Long tagId = json.get("tagId").getAsLong();

        tagService.addTagToTask(taskId, tagId);
        return new Response(true, "Тег добавлен", null);
    }

    private Response handleRemoveTagFromTask(String data) {
        JsonObject json = JsonParser.parseString(data).getAsJsonObject();
        Long taskId = json.get("taskId").getAsLong();
        Long tagId = json.get("tagId").getAsLong();

        tagService.removeTagFromTask(taskId, tagId);
        return new Response(true, "Тег удалён", null);
    }

    private Response handleGetAllTags() {
        List<TagDTO> tags = tagService.getAllTags();
        return new Response(true, "Список тегов", gson.toJson(tags));
    }

    private Response handleGetTaskTags(String data) {
        JsonObject json = JsonParser.parseString(data).getAsJsonObject();
        Long taskId = json.get("taskId").getAsLong();

        List<TagDTO> tags = tagService.getTaskTags(taskId);
        return new Response(true, "Теги задачи", gson.toJson(tags));
    }

    // === история ===

    private Response handleGetTaskHistory(String data) {
        JsonObject json = JsonParser.parseString(data).getAsJsonObject();
        Long taskId = json.get("taskId").getAsLong();

        List<TaskHistoryDTO> history = historyService.getTaskHistory(taskId);
        return new Response(true, "История задачи", gson.toJson(history));
    }

    // === статистика ===

    private Response handleGetProjectStatistics(String data) {
        JsonObject json = JsonParser.parseString(data).getAsJsonObject();
        Long projectId = json.get("projectId").getAsLong();

        Map<String, Object> stats = statisticsService.getProjectStatistics(projectId);
        return new Response(true, "Статистика проекта", gson.toJson(stats));
    }
}