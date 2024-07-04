package com.example.EmissorMdfe.ui.gallery;

import com.example.EmissorMdfe.Validation.Model.Nota;

import java.util.List;

public class UserData {
    private int userId;
    private String phoneNumber;
    private int validationCode;
    private String manifestoCode;
    private List<Nota> notes;

    // Construtores, getters e setters
    public UserData(int userId, String phoneNumber, int validationCode, String manifestoCode, List<Nota> notes) {
        this.userId = userId;
        this.phoneNumber = phoneNumber;
        this.validationCode = validationCode;
        this.manifestoCode = manifestoCode;
        this.notes = notes;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getValidationCode() {
        return validationCode;
    }

    public void setValidationCode(int validationCode) {
        this.validationCode = validationCode;
    }

    public String getManifestoCode() {
        return manifestoCode;
    }

    public void setManifestoCode(String manifestoCode) {
        this.manifestoCode = manifestoCode;
    }

    public List<Nota> getNotes() {
        return notes;
    }

    public void setNotes(List<Nota> notes) {
        this.notes = notes;
    }
}