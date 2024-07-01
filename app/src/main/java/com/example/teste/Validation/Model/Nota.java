package com.example.teste.Validation.Model;

public class Nota {
    private int id;
    private String chave;
    private String peso;
    private String valor;
    private String chave_Contingencia;

    // Construtor padrão
    public Nota(String chave, String peso, String valor) {
        this.chave = chave;
        this.peso = peso;
        this.valor = valor;
    }

    // Construtor para contingência
    public Nota(String chave, String peso, String valor, String chave_Contingencia) {
        this.chave = chave;
        this.peso = peso;
        this.valor = valor;
        this.chave_Contingencia = chave_Contingencia;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getChave() {
        return chave;
    }

    public void setChave(String chave) {
        this.chave = chave;
    }

    public String getPeso() {
        return peso;
    }

    public void setPeso(String peso) {
        this.peso = peso;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getChave_Contingencia() {
        return chave_Contingencia;
    }

    public void setChave_Contingencia(String chave_Contingencia) {
        this.chave_Contingencia = chave_Contingencia;
    }
}


