package edu.bethlehem.post_service.Interaction;

import org.springframework.http.HttpStatus;

import edu.bethlehem.post_service.Error.GeneralException;

public class InteractionAlreadyExistsException extends GeneralException {

  public InteractionAlreadyExistsException(long id) {
    super(
        "Interaction with user: " + id + " already exists",
        HttpStatus.CONFLICT);
  }

  public InteractionAlreadyExistsException(String message, HttpStatus httpStatus) {
    super(message, httpStatus);
  }
}
