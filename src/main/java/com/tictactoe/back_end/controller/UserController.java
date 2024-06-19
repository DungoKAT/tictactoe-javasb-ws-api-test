package com.tictactoe.back_end.controller;

import lombok.extern.slf4j.Slf4j;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Collection;

import org.springframework.http.ResponseEntity;

import com.tictactoe.back_end.model.User;
import com.tictactoe.back_end.service.UserService;
import org.springframework.web.bind.annotation.PutMapping;


@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "https://tictactoe-react-javasb-ws-test.netlify.app")
public class UserController {
    private final UserService userService;

    @GetMapping("/all")
    public ResponseEntity<Collection<User>> getAllUsers() {
        log.info("get all users");
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUser(@PathVariable String userId) {
        log.info("get user: {}", userId);
        return ResponseEntity.ok(userService.getUser(userId));
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        log.info("register request: {}", user);
        return ResponseEntity.ok(userService.registerUser(user.getUsername(), user.getPassword()));
    }

    @PutMapping("/login")
    public ResponseEntity<User> login(@RequestBody User user) {
        log.info("login request: {}", user);
        return ResponseEntity.ok(userService.loginUser(user.getUsername(), user.getPassword()));
    }

    @PutMapping("/logout")
    public ResponseEntity<User> logout(@RequestBody User user) {
        log.info("login request: {}", user);
        return ResponseEntity.ok(userService.logoutUser(user.getUserId()));
    }

}
