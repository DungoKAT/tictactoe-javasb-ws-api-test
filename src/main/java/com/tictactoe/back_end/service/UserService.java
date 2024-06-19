package com.tictactoe.back_end.service;

import org.springframework.stereotype.Service;
import lombok.AllArgsConstructor;

import java.util.Collection;
import java.util.logging.Logger;

import com.tictactoe.back_end.model.User;
import com.tictactoe.back_end.storage.UserStorage;

@Service
@AllArgsConstructor
public class UserService {
    private static final Logger logger = Logger.getLogger(GameService.class.getName());
    private UserStorage userStorage;

    public UserService() {
        userStorage = UserStorage.getInstance();
    }

    public Collection<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public User getUser(String userId) {
        User user = userStorage.getUserById(userId);
        return user;
    }

    public User registerUser(String username, String password) {
        if(userStorage.userExists(username)) {
            throw new IllegalArgumentException("Username already exists.");
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        userStorage.addUser(user);

        return user;
    }

    public User loginUser(String username, String password) {
        User user = userStorage.getUserByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("Invalid username");
        } else if (!user.getPassword().equals(password)) {
            logger.info("User password: " + password);
            logger.info("Available user password: " + user.getPassword());
            throw new IllegalArgumentException("Invalid password");
        }
        if (user.getIsLogin()) {
            throw new IllegalArgumentException("This user is already logged in");
        }
        user.setIsLogin(true);

        return user;
    }

    public User logoutUser(String userId) {
        User user = userStorage.getUserById(userId);
        if(user == null) {
            throw new IllegalArgumentException("Invalid username");
        }
        user.setIsLogin(false);

        return user;
    }
}
