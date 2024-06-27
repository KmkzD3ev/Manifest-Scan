package com.example.teste.Validation.Model;

public class UserDataModel {
    private int id;
    private String telefone;
    private String codigoValidacao;

    public UserDataModel(int id, String telefone, String codigoValidacao) {
        this.id = id;
        this.telefone = telefone;
        this.codigoValidacao = codigoValidacao;
    }

    public int getId() {
        return id;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getCodigoValidacao() {
        return codigoValidacao;
    }
}
