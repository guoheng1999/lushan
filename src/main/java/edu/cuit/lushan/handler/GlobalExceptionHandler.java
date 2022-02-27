package edu.cuit.lushan.handler;

import edu.cuit.lushan.utils.ResponseMessage;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;

//@RestControllerAdvice
public class GlobalExceptionHandler {
    //    @ExceptionHandler
    public ResponseMessage defaultErrorHandler(HttpServletRequest request, Exception e) {

        ResponseMessage result;
        if (e instanceof NoHandlerFoundException) {
            result = ResponseMessage.errorMsg(2404, e.getMessage());
        } else {
            result = ResponseMessage.errorMsg(2500, e.getMessage());
        }
        return result;
    }
}