package edu.bethlehem.interaction_service.Error;

import org.springframework.http.HttpStatus;

public class GeneralException extends RuntimeException {

    private HttpStatus httpStatus;

    public GeneralException(String message, HttpStatus httpStatus) {
        super(message);

        this.httpStatus = httpStatus;
    }

    public GeneralException(String message) {

        super(message);
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

}
