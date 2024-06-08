package edu.bethlehem.user_service;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends RuntimeException {

    //TODO
    public UserNotFoundException(String string, HttpStatus notFound) {
    }

    public UserNotFoundException(String string) {
        super(string);

    }
}
