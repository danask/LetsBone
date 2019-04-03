package com.mobile.letsbone.Entities;

public class ChatBoxData {
    private String message;
    private Boolean currentUser;

    //Constructor
    public ChatBoxData(String message, Boolean currentUser) {
        this.message = message;
        this.currentUser = currentUser;
    }

    //Getters and Setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(Boolean currentUser) {
        this.currentUser = currentUser;
    }
}
