package com.example.myapplication;

import java.util.Date;

public class NotificationLog {
    private String title;
    private String message;
    private Date timestamp;

    // Required no-argument constructor
    public NotificationLog() {}

    public NotificationLog(String title, String message, Date timestamp) {
        this.title = title;
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
