package edu.bethlehem.opinion_service.Opinion;

import org.springframework.http.HttpStatus;

import edu.bethlehem.post_service.Error.GeneralException;

public class OpinionNotFoundException extends GeneralException {

  public OpinionNotFoundException(long id, HttpStatus httpStatus) {
    super("Opinion With Id : " + id + ", is Not Found ", httpStatus);
  }

  public OpinionNotFoundException(String id, HttpStatus httpStatus) {
    super("Opinion With Id : " + id + ", is Not Found ", httpStatus);
  }
}
