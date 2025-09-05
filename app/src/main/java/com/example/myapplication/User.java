package com.example.myapplication;

public class User {
    public String name, email, gender, bloodGroup;

    // Required empty constructor for Firestore
    public User() {}

    public User(String name, String email, String gender, String bloodGroup) {
        this.name = name;
        this.email = email;
        this.gender = gender;
        this.bloodGroup = bloodGroup;
    }
}
