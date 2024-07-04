package com.example.EmissorMdfe.Validation.Model;

public class User {
    private int id;
    private String phoneNumber;

    public User(int id, String phoneNumber) {
        this.id = id;
        this.phoneNumber = phoneNumber;
    }

    public int getId() {
        return id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}

