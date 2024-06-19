package com.tictactoe.back_end.model;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;


@Data
public class GameHistory {
    private List<Move> allMoves;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
