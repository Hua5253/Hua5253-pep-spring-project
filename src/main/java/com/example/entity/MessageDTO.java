package com.example.entity;

public class MessageDTO {
    private String messageText;

    public MessageDTO() {}

    public MessageDTO(String messageText) {
        this.messageText = messageText;
    }

    public String getMessageText() {
        return this.messageText;
    }
}
