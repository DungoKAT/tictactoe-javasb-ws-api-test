package com.tictactoe.back_end.exception;

public class InvalidParamException extends Exception {
    private String message;

    public InvalidParamException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
