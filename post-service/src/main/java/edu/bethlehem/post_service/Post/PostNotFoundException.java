package edu.bethlehem.post_service.Post;

import org.springframework.http.HttpStatus;

import edu.bethlehem.post_service.Error.GeneralException;

public class PostNotFoundException extends GeneralException {

  public PostNotFoundException(long id, HttpStatus httpStatus) {
    super("Post With Id : " + id + ", is Not Found ", httpStatus);
  }

  public PostNotFoundException(long id) {
    super("Post With Id : " + id + ", is Not Found ", HttpStatus.NOT_FOUND);
  }
}
