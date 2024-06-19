package com.tictactoe.back_end.model;

import lombok.Data;

@Data
public class GamePlay {
    private String gameId;
    private TicTacToe markType;
    private Integer coordinateX;
    private Integer coordinateY;
}
