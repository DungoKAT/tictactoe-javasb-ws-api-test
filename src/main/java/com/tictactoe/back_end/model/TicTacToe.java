package com.tictactoe.back_end.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TicTacToe {
    X("X"), O("O"), DRAW("DRAW");

    private String value;
}
