package edu.cuit.lushan.exception;

import lombok.Data;

@Data
public class AuthorizationException extends RuntimeException {
    private Object data;

    public AuthorizationException(Object message, Object data) {
        super(message.toString());
        this.data = data;
    }

    public AuthorizationException(Object message) {
        super(message.toString());
    }

}
