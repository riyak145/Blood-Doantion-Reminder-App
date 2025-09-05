package com.example.myapplication;

import java.util.Date;

public class DonationItem {
    private Date date;
    private String location;
    private String notes;

    // Required no-argument constructor for Firestore
    public DonationItem() {
    }

    public DonationItem(Date date, String location, String notes) {
        this.date = date;
        this.location = location;
        this.notes = notes;
    }

    public Date getDate() {
        return date;
    }

    public String getLocation() {
        return location;
    }

    public String getNotes() {
        return notes;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
