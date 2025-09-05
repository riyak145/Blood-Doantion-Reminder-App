package com.example.myapplication;

import java.util.List;

public class DonationCentre {
    private String name;
    private String location;
    private List<String> phone;

    // Required no-arg constructor for Firestore
    public DonationCentre() {}

    public DonationCentre(String name, String location, List<String> phone) {
        this.name = name;
        this.location = location;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public List<String> getPhone() {
        return phone;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setPhone(List<String> phone) {
        this.phone = phone;
    }
}
