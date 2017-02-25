package com.example;

public class IncomingMessage {

    private String message;

    public IncomingMessage() {
    }

    public IncomingMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
