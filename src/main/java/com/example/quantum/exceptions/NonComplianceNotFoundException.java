package com.example.quantum.exceptions;

public class NonComplianceNotFoundException extends RuntimeException {
    public NonComplianceNotFoundException(String message) {
        super(message);
    }
}
