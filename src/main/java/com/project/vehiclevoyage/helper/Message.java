package com.project.vehiclevoyage.helper;

public class Message {

    private String text;
    private String type;

    public Message(String message, String type) {
        this.text = message;
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Message{" +
                "text='" + text + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
