package com.tictactoe.back_end.model;

import lombok.Data;

@Data
public class GameBoard {
    private String [][] board;
    private Integer size;
}
