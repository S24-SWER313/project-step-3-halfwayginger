package edu.bethlehem.opinion_service.Error;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
public class RestExceptionHandler {

        @ExceptionHandler(GeneralException.class)
        ResponseEntity<GeneralErrorResponse> restExceptionHandler(GeneralException ex) {

                GeneralErrorResponse errorResponse = GeneralErrorResponse
                                .builder()
                                .status(ex.getHttpStatus().value())
                                .message("This is Form the General Exception : " + ex.getMessage())
                                .timestamp(new Date(System.currentTimeMillis()))
                                .throwable(ex.getCause())
                                .build();

                return new ResponseEntity<>(errorResponse, ex.getHttpStatus());

        }

        @ResponseStatus(HttpStatus.BAD_REQUEST)
        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<GeneralErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
                Map<String, String> errors = new HashMap<>();

                ex
                                .getBindingResult()
                                .getAllErrors()
                                .forEach((error) -> {
                                        String fieldName = ((FieldError) error).getField();
                                        String errorMessage = error.getDefaultMessage();
                                        errors.put(fieldName, errorMessage);
                                });
                GeneralErrorResponse errorResponse = GeneralErrorResponse
                                .builder()
                                .message("Vaildation Constraint Violation")
                                .status(HttpStatus.BAD_REQUEST.value())
                                .timestamp(new Date(System.currentTimeMillis()))
                                .validationError(errors)
                                .throwable(ex.getCause())
                                .build();

                return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        @ExceptionHandler(HttpMessageNotReadableException.class)
        public ResponseEntity<Object> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
                // You can customize the response entity as per your requirements
                GeneralErrorResponse errorResponse = GeneralErrorResponse.builder()
                                .message("This is happend From HttpMessageNotReadableException : The request body is invalid or unreadable."
                                                + "" + ex.getMessage() + " "
                                                + ex.getMostSpecificCause().toString())
                                .status(400)
                                .timestamp(new Date(System.currentTimeMillis()))
                                .throwable(ex.getCause())
                                .build();
                return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
        public ResponseEntity<Object> handleHttpRequestMethodNotSupportedException(
                        HttpRequestMethodNotSupportedException ex) {
                GeneralErrorResponse errorResponse = GeneralErrorResponse.builder()
                                .message("Http Request Method Not Supported. " + ex.getMessage() + " "
                                                + Arrays.toString(ex.getStackTrace()))
                                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                                .timestamp(new Date(System.currentTimeMillis()))
                                .throwable(ex.getCause())
                                .build();
                return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<Object> handleAll(Exception ex) throws Exception {

                GeneralErrorResponse errorResponse = GeneralErrorResponse.builder()
                                .message("This handling from Handle all : An error occurred while processing the request. "
                                                + ex.getMessage() + "  "
                                                + Arrays.toString(ex.getStackTrace()))
                                .status(500)
                                .timestamp(new Date(System.currentTimeMillis()))
                                .throwable(ex.getCause())
                                .build();

                return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);

        }

        @ExceptionHandler(TransactionSystemException.class)
        public ResponseEntity<Object> handleTransactionException(TransactionSystemException ex) {
                // Customize your response here, for example:
                GeneralErrorResponse errorResponse = GeneralErrorResponse.builder()
                                .message("This is happened from transitionSystem Exception An error occurred while processing the request. "
                                                + ex.getMostSpecificCause().getMessage()
                                                + "  "
                                                + ex.getStackTrace().toString())
                                .status(500)
                                .timestamp(new Date(System.currentTimeMillis()))
                                .throwable(ex.getCause())
                                .build();
                return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        @ExceptionHandler(HttpMessageConversionException.class)
        public ResponseEntity<Object> handleHttpMessageConversionException(HttpMessageConversionException ex) {
                // Customize your response here, for example:

                GeneralErrorResponse errorResponse = GeneralErrorResponse.builder()
                                .message(" This is happeend form httpMessageConversion An error occurred while processing the request. "
                                                + ex.getMostSpecificCause().getMessage()
                                                + "  "
                                                + Arrays.toString(ex.getStackTrace()))
                                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                                .timestamp(new Date(System.currentTimeMillis()))
                                .throwable(ex.getCause())
                                .build();
                return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);

        }

        @ExceptionHandler(NoResourceFoundException.class)
        public ResponseEntity<Object> handleResourceNotFoundException(NoResourceFoundException ex) {

                GeneralErrorResponse errorResponse = GeneralErrorResponse.builder()
                                .status(HttpStatus.NOT_FOUND.value())
                                .timestamp(new Date(System.currentTimeMillis()))
                                .message("this is from NO Resoucre Found Exception" + ex.getMessage() + " "
                                                + ex.getBody().toString())
                                .throwable(ex.getCause())
                                .build();
                return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

}
