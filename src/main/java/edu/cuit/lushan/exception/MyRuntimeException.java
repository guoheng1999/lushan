package edu.cuit.lushan.exception;


public class MyRuntimeException extends RuntimeException {
    private Object data;

    public MyRuntimeException(Object message, Object data) {
        super(message.toString());
        this.data = data;
    }

    public MyRuntimeException(Object message) {
        super(message.toString());
    }
}
