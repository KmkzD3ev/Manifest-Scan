package com.example.teste.Validation.Model;

public class Nota {
    private int id; // Adicionado para identificar a nota
    private String barcode;
    private String weight;
    private String value;
    private String chaveContingencia; // Adicionado para contingência

    // Construtor padrão
    public Nota(String barcode, String weight, String value) {
        this.barcode = barcode;
        this.weight = weight;
        this.value = value;
    }

    // Construtor para contingência
    public Nota(String barcode, String weight, String value, String chaveContingencia) {
        this.barcode = barcode;
        this.weight = weight;
        this.value = value;
        this.chaveContingencia = chaveContingencia;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getChaveContingencia() {
        return chaveContingencia;
    }

    public void setChaveContingencia(String chaveContingencia) {
        this.chaveContingencia = chaveContingencia;
    }
}
