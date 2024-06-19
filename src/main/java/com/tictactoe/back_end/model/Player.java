package com.tictactoe.back_end.model;

import lombok.Data;

@Data
public class Player {
    private String playerId;
    private String playerName;
    private TicTacToe markType;
}
