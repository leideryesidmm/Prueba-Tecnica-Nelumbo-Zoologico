package com.nelumbo.api_zoologico.Exceptions;

public class InvalidCommentException extends RuntimeException {
    public InvalidCommentException(String message) {
        super(message);
    }
}