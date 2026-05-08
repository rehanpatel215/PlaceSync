package backend.auth;

import backend.models.User;

public class AuthManager {
    private static AuthManager instance;
    private User currentUser;

    private AuthManager() {}

    public static AuthManager getInstance() {
        if (instance == null) {
            instance = new AuthManager();
        }
        return instance;
    }

    public void login(User user) {
        this.currentUser = user;
    }

    public void logout() {
        this.currentUser = null;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    public boolean isAdmin() {
        return isLoggedIn() && "Admin".equals(currentUser.getRole());
    }

    public boolean isStudent() {
        return isLoggedIn() && "Student".equals(currentUser.getRole());
    }
}
