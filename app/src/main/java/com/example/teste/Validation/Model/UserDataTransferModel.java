package com.example.teste.Validation.Model;

import java.util.List;

public class UserDataTransferModel {
    private int id;
    private String telefone;
    private ManifestoDataModel manifestoDataModel;
    private List<NotaDataModel> notas;

    public UserDataTransferModel(int id, String telefone, ManifestoDataModel manifestoDataModel, List<NotaDataModel> notas) {
        this.id = id;
        this.telefone = telefone;
        this.manifestoDataModel = manifestoDataModel;
        this.notas = notas;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public ManifestoDataModel getManifestoDataModel() {
        return manifestoDataModel;
    }

    public void setManifestoDataModel(ManifestoDataModel manifestoDataModel) {
        this.manifestoDataModel = manifestoDataModel;
    }

    public List<NotaDataModel> getNotas() {
        return notas;
    }

    public void setNotas(List<NotaDataModel> notas) {
        this.notas = notas;
    }
}
