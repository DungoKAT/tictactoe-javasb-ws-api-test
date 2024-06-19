package com.tictactoe.back_end.model;

import java.util.ArrayList;
import java.util.UUID;

import lombok.Data;

@Data
public class Game {
    private String gameId;
    Player playerX;
    Player playerO;
    String turn;
    GameBoard gameBoard;
    private GameStatus status;
    private TicTacToe winner;
    private GameHistory gameHistory;

    public Game() {
        gameId = UUID.randomUUID().toString();
        playerX = new Player();
        playerO = new Player();
        gameHistory = new GameHistory();

        playerX.setMarkType(TicTacToe.X);
        playerO.setMarkType(TicTacToe.O);
        turn = "X";
        status = GameStatus.NEW;
        gameHistory.setAllMoves(new ArrayList<>());
    }

    
    public Boolean checkDraw(GameBoard gameBoard) {
        for (int row = 0; row < gameBoard.getSize(); row++) {
            for (int col = 0; col < gameBoard.getSize(); col++) {
                if (gameBoard.getBoard()[row][col] == null) {
                    return false;
                }
            }
        }
        return true;
    }

    public Boolean checkWinner(GameBoard gameBoard, TicTacToe ticTacToe) {
        return checkRows(gameBoard, ticTacToe) 
            || checkCols(gameBoard, ticTacToe)
            || checkDiagonals(gameBoard, ticTacToe);
    }

    private Boolean checkRows(GameBoard gameBoard, TicTacToe ticTacToe) {
        int size = gameBoard.getSize();
        String [][] board = gameBoard.getBoard();
        String markType = ticTacToe.getValue();

        if(size == 3) {
            for (int row = 0; row < size; row++) {
                if (board[row][0] == markType && board[row][1] == markType && board[row][2] == markType) {
                    return true;
                }
            }
            return false;
        }
        for (int row = 0; row < size; row++) {
            for (int col = 0; col <= size - 4; col++) {
                if (board[row][col] == markType && board[row][col + 1] == markType && 
                    board[row][col + 2] == markType && board[row][col + 3] == markType) {
                    return true;
                }
            }
        }
        return false;
    }

    private Boolean checkCols(GameBoard gameBoard, TicTacToe ticTacToe) {
        int size = gameBoard.getSize();
        String [][] board = gameBoard.getBoard();
        String markType = ticTacToe.getValue();

        if(size == 3) {
            for (int col = 0; col < size; col++) {
                if (board[0][col] == markType && board[1][col] == markType && board[2][col] == markType) {
                    return true;
                }
            }
            return false;
        }
        for (int col = 0; col < size; col++) {
            for (int row = 0; row <= size - 4; row++) {
                if (board[row][col] == markType && board[row + 1][col] == markType &&
                    board[row + 2][col] == markType && board[row + 3][col] == markType) {
                    return true;
                }
            }
        }
        return false;
    }

    private Boolean checkDiagonals(GameBoard gameBoard, TicTacToe ticTacToe) {
        int size = gameBoard.getSize();
        String [][] board = gameBoard.getBoard();
        String markType = ticTacToe.getValue();

        if(size == 3) {
            if (board[0][0] == markType && board[1][1] == markType && board[2][2] == markType) {
                return true;
            }
            if (board[0][2] == markType && board[1][1] == markType && board[2][0] == markType) {
                return true;
            }
            return false;
        }
        for (int row = 0; row <= size - 4; row++) {
            for (int col = 0; col <= size - 4; col++) {
                if (board[row][col] == markType && board[row + 1][col + 1] == markType &&
                    board[row + 2][col + 2] == markType && board[row + 3][col + 3] == markType) {
                    return true;
                }
            }
        }
        for (int row = 0; row <= size - 4; row++) {
            for (int col = 3; col < size; col++) {
                if (board[row][col] == markType && board[row + 1][col - 1] == markType &&
                    board[row + 2][col - 2] == markType && board[row + 3][col - 3] == markType) {
                    return true;
                }
            }
        }
        return false;
    }
}
