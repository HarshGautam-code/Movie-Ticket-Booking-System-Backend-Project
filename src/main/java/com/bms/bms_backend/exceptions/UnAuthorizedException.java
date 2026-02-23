package com.bms.bms_backend.exceptions;

public class UnAuthorizedException extends RuntimeException {

    public UnAuthorizedException( String message) {
        super(message);

    }
}
