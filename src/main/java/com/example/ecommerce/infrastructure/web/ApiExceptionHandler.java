package com.example.ecommerce.infrastructure.web;

import com.example.ecommerce.domain.exception.*;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

record ErrorResponse(String message) {}

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler({
            CartNotFoundException.class,
            OrderNotFoundException.class
    })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFound(DomainException ex) {
        return new ErrorResponse(ex.getMessage());
    }

    @ExceptionHandler({
            OrderAlreadyPaidException.class
    })
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleConflict(DomainException ex) {
        return new ErrorResponse(ex.getMessage());
    }

    @ExceptionHandler(DomainException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleDomainError(DomainException ex) {
        return new ErrorResponse(ex.getMessage());
    }
}
