package com.glowinski;

import java.io.Serializable;

public class Message implements Serializable {
    private String message;

    public Message(){

    }
    public Message(String s){
        message = s;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
