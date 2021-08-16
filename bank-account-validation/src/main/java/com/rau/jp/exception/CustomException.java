package com.rau.jp.exception;


import org.springframework.http.HttpStatus;

public class CustomException extends Exception {
    private String message;
    private HttpStatus status;

    public CustomException(String message, HttpStatus status){
        this.message = message;
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "CustomException{" +
                "message='" + message + '\'' +
                ", status=" + status +
                '}';
    }
}
