package com.in.jrfc.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class PriceExceptionAdviceHandlerController {
    @ResponseBody
    @ExceptionHandler(PriceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String priceNotFoundHandler(PriceNotFoundException ex) {
        return ex.getMessage();
    }
    @ResponseBody
    @ExceptionHandler(PriceRunTimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    String priceRunTimeHandler(PriceRunTimeException ex) {
        return ex.getMessage();
    }
}
