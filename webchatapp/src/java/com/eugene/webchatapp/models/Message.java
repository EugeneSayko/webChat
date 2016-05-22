package com.eugene.webchatapp.models;

/**
 * Created by eugene on 16.05.16.
 */
public class Message {

    private String id;
    private String author;
    private long timestamp;
    private String text;

    public Message(){

    }

    public Message(String id, String author, long timestamp, String text) {
        this.id = id;
        this.author = author;
        this.timestamp = timestamp;
        this.text = text;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id='" + id + '\'' +
                ", author='" + author + '\'' +
                ", timestamp=" + timestamp +
                ", text='" + text + '\'' +
                '}';
    }

}
