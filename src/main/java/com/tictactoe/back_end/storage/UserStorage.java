package com.tictactoe.back_end.storage;

import com.tictactoe.back_end.model.User;

import java.util.Map;
import java.util.HashMap;
import java.util.Collection;

public class UserStorage {
    private static UserStorage instance;
    private static Map<String, User> users;
    
    private UserStorage() {
        users = new HashMap<>();
    }

    public static synchronized UserStorage getInstance() {
        if (instance == null) {
            instance = new UserStorage();
        }
        return instance;
    }

    public void addUser(User user) {
        users.put(user.getUserId(), user);
    }

    public Collection<User> getAllUsers() {
        return users.values();
    }

    public User getUserById(String userId) {
        return users.get(userId);
    }

    public User getUserByUsername(String username) {
        User foundUser = users.values().stream()
                            .filter(user -> user.getUsername().equals(username))
                            .findFirst().orElse(null);
        return foundUser;
    }

    public boolean userExists(String username) {
        Boolean userExists = users.values().stream()
                                .anyMatch(user -> user.getUsername().equals(username));
        return userExists;
    }
}
