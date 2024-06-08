package edu.bethlehem.user_service;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends RuntimeException {
    private final HttpStatus status;
    public UserNotFoundException(String message) {
        super(message);
        this.status = HttpStatus.NOT_FOUND;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
