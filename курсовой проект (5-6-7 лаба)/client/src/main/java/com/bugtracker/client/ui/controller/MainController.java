package com.bugtracker.client.ui.controller;

import com.bugtracker.client.network.NetworkService;
import com.bugtracker.client.util.SessionManager;
import com.bugtracker.common.dto.Response;
import com.bugtracker.common.dto.UserDTO;
import com.bugtracker.common.enums.ActionType;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class MainController {

    private final NetworkService networkService = NetworkService.getInstance();
    private final SessionManager sessionManager = SessionManager.getInstance();
    private Runnable onLogout;

    @FXML
    private Label currentUserLabel;
    @FXML
    private TextArea logArea;

    // Users
    @FXML
    private TextField userCreateUsernameField;
    @FXML
    private TextField userCreatePasswordField;
    @FXML
    private TextField userCreateEmailField;
    @FXML
    private TextField userCreateFullNameField;
    @FXML
    private TextField userCreateRoleField;
    @FXML
    private TextField userUpdateIdField;
    @FXML
    private TextField userUpdateEmailField;
    @FXML
    private TextField userUpdateFullNameField;
    @FXML
    private TextField userUpdateRoleField;
    @FXML
    private TextField userBlockIdField;

    // Projects
    @FXML
    private TextField projectCreateNameField;
    @FXML
    private TextField projectCreateDescriptionField;
    @FXML
    private TextField projectCreateOwnerIdField;
    @FXML
    private TextField projectUpdateIdField;
    @FXML
    private TextField projectUpdateNameField;
    @FXML
    private TextField projectUpdateDescriptionField;
    @FXML
    private TextField projectUserProjectsUserIdField;
    @FXML
    private TextField projectMemberProjectIdField;
    @FXML
    private TextField projectMemberUserIdField;

    // Tasks
    @FXML
    private TextField taskCreateTitleField;
    @FXML
    private TextField taskCreateDescriptionField;
    @FXML
    private TextField taskCreateProjectIdField;
    @FXML
    private TextField taskCreateCreatorIdField;
    @FXML
    private TextField taskCreateAssigneeIdField;
    @FXML
    private TextField taskCreatePriorityField;
    @FXML
    private TextField taskCreateTypeField;
    @FXML
    private TextField taskCreateDeadlineField;
    @FXML
    private TextField taskUpdateIdField;
    @FXML
    private TextField taskUpdateTitleField;
    @FXML
    private TextField taskUpdateDescriptionField;
    @FXML
    private TextField taskUpdateDeadlineField;
    @FXML
    private TextField taskActionTaskIdField;
    @FXML
    private TextField taskActionUserIdField;
    @FXML
    private TextField taskActionStatusField;
    @FXML
    private TextField taskActionPriorityField;
    @FXML
    private TextField taskActionAssigneeIdField;
    @FXML
    private TextField taskQueryProjectIdField;
    @FXML
    private TextField taskQueryKeywordField;

    // Comments
    @FXML
    private TextField commentTaskIdField;
    @FXML
    private TextField commentAuthorIdField;
    @FXML
    private TextArea commentTextArea;

    // Tags
    @FXML
    private TextField tagTaskIdField;
    @FXML
    private TextField tagTagIdField;

    // Reports
    @FXML
    private TextField historyTaskIdField;
    @FXML
    private TextField statsProjectIdField;

    @FXML
    public void initialize() {
        refreshCurrentUserLabel();
    }

    public void setOnLogout(Runnable onLogout) {
        this.onLogout = onLogout;
    }

    @FXML
    private void onLogoutClicked() {
        if (networkService.isConnected()) {
            send(ActionType.LOGOUT, null);
        }
        sessionManager.clearSession();
        if (onLogout != null) {
            onLogout.run();
        }
    }

    @FXML
    private void onClearLogClicked() {
        logArea.clear();
    }

    // Users
    @FXML
    private void onCreateUserClicked() {
        JsonObject data = new JsonObject();
        data.addProperty("username", userCreateUsernameField.getText().trim());
        data.addProperty("password", userCreatePasswordField.getText());
        data.addProperty("email", userCreateEmailField.getText().trim());
        data.addProperty("fullName", userCreateFullNameField.getText().trim());
        data.addProperty("role", userCreateRoleField.getText().trim());
        send(ActionType.CREATE_USER, data.toString());
    }

    @FXML
    private void onGetAllUsersClicked() {
        send(ActionType.GET_ALL_USERS, null);
    }

    @FXML
    private void onUpdateUserClicked() {
        JsonObject data = new JsonObject();
        data.addProperty("id", parseLong(userUpdateIdField.getText()));
        data.addProperty("email", userUpdateEmailField.getText().trim());
        data.addProperty("fullName", userUpdateFullNameField.getText().trim());
        data.addProperty("role", userUpdateRoleField.getText().trim());
        send(ActionType.UPDATE_USER, data.toString());
    }

    @FXML
    private void onBlockUserClicked() {
        sendSimpleIdAction(ActionType.BLOCK_USER, userBlockIdField.getText());
    }

    @FXML
    private void onUnblockUserClicked() {
        sendSimpleIdAction(ActionType.UNBLOCK_USER, userBlockIdField.getText());
    }

    // Projects
    @FXML
    private void onUseCurrentUserAsOwnerClicked() {
        projectCreateOwnerIdField.setText(getCurrentUserIdAsText());
    }

    @FXML
    private void onCreateProjectClicked() {
        JsonObject data = new JsonObject();
        data.addProperty("name", projectCreateNameField.getText().trim());
        data.addProperty("description", projectCreateDescriptionField.getText().trim());
        data.addProperty("ownerId", parseLong(projectCreateOwnerIdField.getText()));
        send(ActionType.CREATE_PROJECT, data.toString());
    }

    @FXML
    private void onUpdateProjectClicked() {
        JsonObject data = new JsonObject();
        data.addProperty("id", parseLong(projectUpdateIdField.getText()));
        data.addProperty("name", projectUpdateNameField.getText().trim());
        data.addProperty("description", projectUpdateDescriptionField.getText().trim());
        send(ActionType.UPDATE_PROJECT, data.toString());
    }

    @FXML
    private void onGetAllProjectsClicked() {
        send(ActionType.GET_ALL_PROJECTS, null);
    }

    @FXML
    private void onGetUserProjectsClicked() {
        JsonObject data = new JsonObject();
        data.addProperty("userId", parseLong(projectUserProjectsUserIdField.getText()));
        send(ActionType.GET_USER_PROJECTS, data.toString());
    }

    @FXML
    private void onAddProjectMemberClicked() {
        sendProjectMemberAction(ActionType.ADD_PROJECT_MEMBER, projectMemberProjectIdField.getText(), projectMemberUserIdField.getText());
    }

    @FXML
    private void onRemoveProjectMemberClicked() {
        sendProjectMemberAction(ActionType.REMOVE_PROJECT_MEMBER, projectMemberProjectIdField.getText(), projectMemberUserIdField.getText());
    }

    @FXML
    private void onGetProjectMembersClicked() {
        JsonObject data = new JsonObject();
        data.addProperty("projectId", parseLong(projectMemberProjectIdField.getText()));
        send(ActionType.GET_PROJECT_MEMBERS, data.toString());
    }

    // Tasks
    @FXML
    private void onUseCurrentUserAsCreatorClicked() {
        taskCreateCreatorIdField.setText(getCurrentUserIdAsText());
    }

    @FXML
    private void onUseCurrentUserAsTaskActionUserClicked() {
        taskActionUserIdField.setText(getCurrentUserIdAsText());
    }

    @FXML
    private void onCreateTaskClicked() {
        JsonObject data = new JsonObject();
        data.addProperty("title", taskCreateTitleField.getText().trim());
        data.addProperty("description", taskCreateDescriptionField.getText().trim());
        data.addProperty("projectId", parseLong(taskCreateProjectIdField.getText()));
        data.addProperty("creatorId", parseLong(taskCreateCreatorIdField.getText()));
        addNullableLong(data, "assigneeId", taskCreateAssigneeIdField.getText());
        data.addProperty("priority", taskCreatePriorityField.getText().trim());
        data.addProperty("type", taskCreateTypeField.getText().trim());
        addNullableText(data, "deadline", taskCreateDeadlineField.getText());
        send(ActionType.CREATE_TASK, data.toString());
    }

    @FXML
    private void onUpdateTaskClicked() {
        JsonObject data = new JsonObject();
        data.addProperty("id", parseLong(taskUpdateIdField.getText()));
        data.addProperty("title", taskUpdateTitleField.getText().trim());
        data.addProperty("description", taskUpdateDescriptionField.getText().trim());
        addNullableText(data, "deadline", taskUpdateDeadlineField.getText());
        send(ActionType.UPDATE_TASK, data.toString());
    }

    @FXML
    private void onChangeTaskStatusClicked() {
        JsonObject data = new JsonObject();
        data.addProperty("taskId", parseLong(taskActionTaskIdField.getText()));
        data.addProperty("status", taskActionStatusField.getText().trim());
        data.addProperty("userId", parseLong(taskActionUserIdField.getText()));
        send(ActionType.CHANGE_TASK_STATUS, data.toString());
    }

    @FXML
    private void onChangeTaskPriorityClicked() {
        JsonObject data = new JsonObject();
        data.addProperty("taskId", parseLong(taskActionTaskIdField.getText()));
        data.addProperty("priority", taskActionPriorityField.getText().trim());
        data.addProperty("userId", parseLong(taskActionUserIdField.getText()));
        send(ActionType.CHANGE_TASK_PRIORITY, data.toString());
    }

    @FXML
    private void onAssignTaskClicked() {
        JsonObject data = new JsonObject();
        data.addProperty("taskId", parseLong(taskActionTaskIdField.getText()));
        data.addProperty("assigneeId", parseLong(taskActionAssigneeIdField.getText()));
        data.addProperty("userId", parseLong(taskActionUserIdField.getText()));
        send(ActionType.ASSIGN_TASK, data.toString());
    }

    @FXML
    private void onGetProjectTasksClicked() {
        JsonObject data = new JsonObject();
        data.addProperty("projectId", parseLong(taskQueryProjectIdField.getText()));
        send(ActionType.GET_PROJECT_TASKS, data.toString());
    }

    @FXML
    private void onSearchTasksClicked() {
        JsonObject data = new JsonObject();
        data.addProperty("keyword", taskQueryKeywordField.getText().trim());
        send(ActionType.SEARCH_TASKS, data.toString());
    }

    // Comments
    @FXML
    private void onUseCurrentUserAsCommentAuthorClicked() {
        commentAuthorIdField.setText(getCurrentUserIdAsText());
    }

    @FXML
    private void onAddCommentClicked() {
        JsonObject data = new JsonObject();
        data.addProperty("taskId", parseLong(commentTaskIdField.getText()));
        data.addProperty("authorId", parseLong(commentAuthorIdField.getText()));
        data.addProperty("text", commentTextArea.getText());
        send(ActionType.ADD_COMMENT, data.toString());
    }

    @FXML
    private void onGetTaskCommentsClicked() {
        JsonObject data = new JsonObject();
        data.addProperty("taskId", parseLong(commentTaskIdField.getText()));
        send(ActionType.GET_TASK_COMMENTS, data.toString());
    }

    // Tags
    @FXML
    private void onAddTagToTaskClicked() {
        sendTaskTagAction(ActionType.ADD_TAG_TO_TASK, tagTaskIdField.getText(), tagTagIdField.getText());
    }

    @FXML
    private void onRemoveTagFromTaskClicked() {
        sendTaskTagAction(ActionType.REMOVE_TAG_FROM_TASK, tagTaskIdField.getText(), tagTagIdField.getText());
    }

    @FXML
    private void onGetAllTagsClicked() {
        send(ActionType.GET_ALL_TAGS, null);
    }

    @FXML
    private void onGetTaskTagsClicked() {
        JsonObject data = new JsonObject();
        data.addProperty("taskId", parseLong(tagTaskIdField.getText()));
        send(ActionType.GET_TASK_TAGS, data.toString());
    }

    // Reports
    @FXML
    private void onGetTaskHistoryClicked() {
        JsonObject data = new JsonObject();
        data.addProperty("taskId", parseLong(historyTaskIdField.getText()));
        send(ActionType.GET_TASK_HISTORY, data.toString());
    }

    @FXML
    private void onGetProjectStatisticsClicked() {
        JsonObject data = new JsonObject();
        data.addProperty("projectId", parseLong(statsProjectIdField.getText()));
        send(ActionType.GET_PROJECT_STATISTICS, data.toString());
    }

    private void sendSimpleIdAction(ActionType actionType, String idText) {
        JsonObject data = new JsonObject();
        data.addProperty("id", parseLong(idText));
        send(actionType, data.toString());
    }

    private void sendProjectMemberAction(ActionType actionType, String projectIdText, String userIdText) {
        JsonObject data = new JsonObject();
        data.addProperty("projectId", parseLong(projectIdText));
        data.addProperty("userId", parseLong(userIdText));
        send(actionType, data.toString());
    }

    private void sendTaskTagAction(ActionType actionType, String taskIdText, String tagIdText) {
        JsonObject data = new JsonObject();
        data.addProperty("taskId", parseLong(taskIdText));
        data.addProperty("tagId", parseLong(tagIdText));
        send(actionType, data.toString());
    }

    private Response send(ActionType action, String data) {
        if (!networkService.isConnected()) {
            appendLog("Not connected. Action not sent: " + action);
            return null;
        }

        try {
            Response response = data == null
                    ? networkService.sendRequest(action)
                    : networkService.sendRequest(action, data);

            appendResponse(action, data, response);
            return response;
        } catch (Exception ex) {
            appendLog("Request failed (" + action + "): " + ex.getMessage());
            return null;
        }
    }

    private void appendResponse(ActionType action, String requestData, Response response) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== ").append(action).append(" ===\n");
        sb.append("Request data: ").append(requestData == null ? "null" : requestData).append("\n");

        if (response == null) {
            sb.append("Response: null\n\n");
            appendLog(sb.toString());
            return;
        }

        sb.append("Success: ").append(response.isSuccess()).append("\n");
        sb.append("Message: ").append(response.getMessage()).append("\n");
        sb.append("Data:\n").append(formatJsonSafe(response.getData())).append("\n\n");
        appendLog(sb.toString());
    }

    private String formatJsonSafe(String json) {
        if (json == null || json.isBlank()) {
            return "null";
        }
        try {
            return new GsonBuilder().setPrettyPrinting().create().toJson(JsonParser.parseString(json));
        } catch (JsonSyntaxException e) {
            return json;
        }
    }

    private void appendLog(String text) {
        logArea.appendText(text + "\n");
    }

    private Long parseLong(String text) {
        String trimmed = text == null ? "" : text.trim();
        if (trimmed.isEmpty()) {
            return null;
        }
        try {
            return Long.parseLong(trimmed);
        } catch (NumberFormatException e) {
            appendLog("Invalid number: " + trimmed);
            return null;
        }
    }

    private void addNullableLong(JsonObject object, String key, String text) {
        Long value = parseLong(text);
        if (value == null) {
            object.add(key, null);
        } else {
            object.addProperty(key, value);
        }
    }

    private void addNullableText(JsonObject object, String key, String text) {
        String value = text == null ? "" : text.trim();
        if (value.isEmpty()) {
            object.add(key, null);
        } else {
            object.addProperty(key, value);
        }
    }

    private String getCurrentUserIdAsText() {
        Long userId = sessionManager.getCurrentUserId();
        return userId == null ? "" : userId.toString();
    }

    private void refreshCurrentUserLabel() {
        UserDTO user = sessionManager.getCurrentUser();
        String username = user == null ? "unknown" : user.getUsername();
        String role = user == null ? "unknown" : user.getRole();
        currentUserLabel.setText("Current user: " + username + " (" + role + ")");
    }
}
