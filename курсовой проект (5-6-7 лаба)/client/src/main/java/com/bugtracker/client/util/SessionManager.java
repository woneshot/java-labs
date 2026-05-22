package com.bugtracker.client.util;

import com.bugtracker.common.dto.UserDTO;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SessionManager {

    private static SessionManager instance;

    private UserDTO currentUser;

    private SessionManager() {}

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public void clearSession() {
        this.currentUser = null;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    public String getCurrentRole() {
        return currentUser != null ? currentUser.getRole() : null;
    }

    public Long getCurrentUserId() {
        return currentUser != null ? currentUser.getId() : null;
    }
}