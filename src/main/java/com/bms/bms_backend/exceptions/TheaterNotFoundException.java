package com.bms.bms_backend.exceptions;


public class TheaterNotFoundException extends RuntimeException{

    public TheaterNotFoundException(String message) {
        super(message);
    }
}
