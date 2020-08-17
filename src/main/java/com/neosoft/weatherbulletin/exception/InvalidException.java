package com.neosoft.weatherbulletin.exception;

/**
 * Custom Exception
 */
public class InvalidException extends RuntimeException{
    public InvalidException(String message){
        super(message);
    }
}
