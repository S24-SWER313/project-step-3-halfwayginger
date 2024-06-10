package edu.bethlehem.interaction_service.Interaction;

import org.springframework.http.HttpStatus;

import edu.bethlehem.interaction_service.Error.GeneralException;

public class InteractionNotFoundException extends GeneralException {

  public InteractionNotFoundException(long id, HttpStatus httpStatus) {
    super("Interaction With Id : " + id + ", is Not Found ", httpStatus);
  }

  public InteractionNotFoundException(String message, HttpStatus httpStatus) {
    super(message, httpStatus);
  }
}
