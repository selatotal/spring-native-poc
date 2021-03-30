package br.com.selat.springnativepoc.model;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class ErrorException extends RuntimeException {

    public ErrorException(String message) {
        super(message);
    }

}
