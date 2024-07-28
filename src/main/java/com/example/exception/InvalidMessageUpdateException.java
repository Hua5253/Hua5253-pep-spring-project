package com.example.exception;

public class InvalidMessageUpdateException extends RuntimeException {
    public InvalidMessageUpdateException(String message) {
        super(message);
    }
}
