package com.bugtracker.client.ui.controller;

import com.bugtracker.client.network.NetworkService;
import com.bugtracker.common.dto.Response;
import com.bugtracker.common.dto.UserDTO;
import com.bugtracker.common.enums.ActionType;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.util.function.Consumer;

public class LoginController {

    private final NetworkService networkService = NetworkService.getInstance();
    private final Gson gson = new Gson();

    private Consumer<UserDTO> onLoginSuccess;

    @FXML
    private Label connectionStatusLabel;
    @FXML
    private Label loginStatusLabel;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;

    @FXML
    public void initialize() {
        connectToServer();
    }

    public void setOnLoginSuccess(Consumer<UserDTO> onLoginSuccess) {
        this.onLoginSuccess = onLoginSuccess;
    }

    @FXML
    private void onReconnectClicked() {
        connectToServer();
    }

    @FXML
    private void onLoginClicked() {
        if (!networkService.isConnected()) {
            loginStatusLabel.setText("Server is not connected");
            return;
        }

        JsonObject data = new JsonObject();
        data.addProperty("username", usernameField.getText().trim());
        data.addProperty("password", passwordField.getText());

        Response response = networkService.sendRequest(ActionType.LOGIN, data.toString());
        if (response == null) {
            loginStatusLabel.setText("Empty response");
            return;
        }
        if (!response.isSuccess()) {
            loginStatusLabel.setText("Login failed: " + response.getMessage());
            return;
        }
        if (response.getData() == null || response.getData().isBlank()) {
            loginStatusLabel.setText("Login ok, but no user data");
            return;
        }

        try {
            UserDTO user = gson.fromJson(response.getData(), UserDTO.class);
            if (onLoginSuccess != null) {
                onLoginSuccess.accept(user);
            }
        } catch (Exception e) {
            loginStatusLabel.setText("User parse error: " + e.getMessage());
        }
    }

    private void connectToServer() {
        try {
            if (!networkService.isConnected()) {
                networkService.connect("localhost", 8080);
            }
            connectionStatusLabel.setText("Connected to localhost:8080");
            loginStatusLabel.setText("Enter credentials");
        } catch (Exception e) {
            connectionStatusLabel.setText("Not connected");
            loginStatusLabel.setText("Connection error: " + e.getMessage());
        }
    }
}
