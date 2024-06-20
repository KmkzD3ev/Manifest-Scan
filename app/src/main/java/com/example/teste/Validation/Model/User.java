package com.example.teste.Validation.Model;

public class User {
    private String id;
    private String phoneNumber;

    public User(String id, String phoneNumber) {
        this.id = id;
        this.phoneNumber = phoneNumber;
    }

    public String getId() {
        return id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
