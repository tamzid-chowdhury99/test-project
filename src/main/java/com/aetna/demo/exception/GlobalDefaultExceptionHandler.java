package com.aetna.demo.exception;

import com.cvs.digital.hc.common.error.model.Error;
import com.cvs.digital.hc.common.error.model.ResponseModel;
import com.cvs.digital.hc.common.error.model.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
@RestControllerAdvice
public class GlobalDefaultExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalDefaultExceptionHandler.class);

    private static void addError(List<Error> errors, String type, String title, String field) {
        Error error = new Error(type, title, field);
        errors.add(error);
    }

    // Handles unknown request method 404
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ResponseModel<Void>> getNotFoundErrorResponse(HttpRequestMethodNotSupportedException ex) {
        return new ResponseEntity<>(new ResponseModel<>(Status.NOT_FOUND_METHOD_NOT_ALLOWED), HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(UserCollectionException.class)
    public ResponseEntity<ResponseModel<String>> getUserResponse(UserCollectionException ex) {
        return new ResponseEntity<>(new ResponseModel<>(Status.BAD_REQUEST,ex.getMessage()), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ResponseModel<Void>> handleConstraintViolationException(ConstraintViolationException ex) {
        List<Error> errors = new ArrayList<>();

        ex.getConstraintViolations().forEach(e ->
                addError(errors, "Error", e.getMessage(), e.getPropertyPath().toString())
        );

        ResponseModel<Void> emptyHeaderErrorModel = ResponseModel.builder()
                .status(Status.BAD_REQUEST)
                .title(Status.BAD_REQUEST.getStatusDescription())
                .fault(
                        "Validation Error",
                        "Errors due to missing/incorrect fields",
                        errors)
                .build();

        return new ResponseEntity<>(emptyHeaderErrorModel, HttpStatus.BAD_REQUEST);
    }

}
