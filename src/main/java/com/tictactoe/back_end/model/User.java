package com.tictactoe.back_end.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class User {
    private String userId;
    private String username;
    private String password;
    private Boolean isLogin;
    private List<Game> gameHistories;

    public User() {
        userId = UUID.randomUUID().toString();
        isLogin = false;
        gameHistories = new ArrayList<Game>();
    }
}
