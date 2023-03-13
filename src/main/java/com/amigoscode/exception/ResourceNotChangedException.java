package com.amigoscode.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ResourceNotChangedException extends RuntimeException {
    public ResourceNotChangedException(String message) {
        super(message);
    }
}
