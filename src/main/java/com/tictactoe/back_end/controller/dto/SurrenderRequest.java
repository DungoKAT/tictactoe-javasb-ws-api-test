package com.tictactoe.back_end.controller.dto;

import lombok.Data;

@Data
public class SurrenderRequest {
    private String gameId;
    private String playerSurrender;
}
