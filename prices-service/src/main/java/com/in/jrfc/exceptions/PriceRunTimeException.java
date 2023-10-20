package com.in.jrfc.exceptions;

import org.springframework.http.HttpStatus;

public class PriceRunTimeException extends RuntimeException {
    private static final String DESCRIPTION = "Price Service Exception";

    public PriceRunTimeException(HttpStatus runTime, String detail) {

        super(DESCRIPTION + ". " + detail);
    }

    public PriceRunTimeException() {
    }


}
