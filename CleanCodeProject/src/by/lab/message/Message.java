package by.lab.message;

import java.io.Serializable;

public class Message implements Serializable {
    private String id;
    private String message;
    private String author;
    private String timestamp;

    public Message(String id, String message, String author, String timestamp) {
        this.id = id;
        this.message = new String(message);
        this.author = new String(author);
        this.timestamp = new String(timestamp);
    }

    public String getId() {
        return this.id;
    }

    public String getMessage() {
        return this.message;
    }

    public String getAuthor() {
        return this.author;
    }

    public String getTimestamp() {
        return this.timestamp;
    }

}