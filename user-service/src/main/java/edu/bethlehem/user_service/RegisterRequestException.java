package edu.bethlehem.user_service;

import org.springframework.http.HttpStatus;

public class RegisterRequestException extends Throwable {
    public RegisterRequestException(String localizedMessage, HttpStatus httpStatus) {
    }
}
