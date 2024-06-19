package com.tictactoe.back_end.model;

import lombok.Data;

@Data
public class Move {
    private String markType;
    private int[] position;
}
