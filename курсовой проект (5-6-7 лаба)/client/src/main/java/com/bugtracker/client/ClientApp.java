package com.bugtracker.client;

import com.bugtracker.client.ui.controller.LoginController;
import com.bugtracker.client.ui.controller.MainController;
import com.bugtracker.client.util.SessionManager;
import com.bugtracker.common.dto.UserDTO;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ClientApp extends Application {

    private final SessionManager sessionManager = SessionManager.getInstance();

    @Override
    public void start(Stage loginStage) {
        showLoginWindow(loginStage);
    }

    private void showLoginWindow(Stage loginStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/bugtracker/client/login-view.fxml"));
            Parent root = loader.load();

            LoginController controller = loader.getController();
            controller.setOnLoginSuccess(user -> onLoginSuccess(user, loginStage));

            Scene scene = new Scene(root, 440, 260);
            loginStage.setTitle("BugTracker Login");
            loginStage.setScene(scene);
            loginStage.show();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load login view", e);
        }
    }

    private void onLoginSuccess(UserDTO user, Stage loginStage) {
        sessionManager.setCurrentUser(user);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/bugtracker/client/main-view.fxml"));
            Parent root = loader.load();

            MainController controller = loader.getController();
            Stage mainStage = new Stage();
            controller.setOnLogout(() -> {
                mainStage.close();
                Stage newLoginStage = new Stage();
                showLoginWindow(newLoginStage);
            });

            Scene mainScene = new Scene(root, 1200, 900);
            mainStage.setTitle("BugTracker Work Window");
            mainStage.setScene(mainScene);
            mainStage.show();
            loginStage.close();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load main view", e);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}