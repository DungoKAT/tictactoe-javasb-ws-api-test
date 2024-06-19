package com.tictactoe.back_end.controller.dto;

import lombok.Data;

@Data
public class StartRequest {
    private String usernameX;
    private Integer size;
}
