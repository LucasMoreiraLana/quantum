package com.example.quantum.exceptions;

public class WarningNotFoundException extends RuntimeException {
    public WarningNotFoundException(String message) {
        super(message);
    }
}
