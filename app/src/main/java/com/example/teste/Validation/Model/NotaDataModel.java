package com.example.teste.Validation.Model;

public class NotaDataModel {
    private String chave;
    private String peso;
    private String valor;
    private String chave_Contingencia;

    public NotaDataModel(String chave, String peso, String valor, String chave_Contingencia) {
        this.chave = chave;
        this.peso = peso;
        this.valor = valor;
        this.chave_Contingencia = chave_Contingencia;
    }

    public String getChave() {
        return chave;
    }

    public String getPeso() {
        return peso;
    }

    public String getValor() {
        return valor;
    }

    public String getChave_Contingencia() {
        return chave_Contingencia;
    }
}
