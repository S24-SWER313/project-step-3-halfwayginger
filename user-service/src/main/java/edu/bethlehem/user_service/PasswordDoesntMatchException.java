package edu.bethlehem.user_service;

public class PasswordDoesntMatchException extends RuntimeException {
    public PasswordDoesntMatchException() {
        super("Password does not match.");
    }
}
