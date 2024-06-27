package com.example.teste.Validation.Model;

import java.util.List;

public class UserDataTransferModel {
    private int id;
    private String telefone;
    private String codigoValidacao;
    private ManifestoDataModel manifestoDataModel;
    private List<Nota> notas;

    public UserDataTransferModel(int id, String telefone, String codigoValidacao, ManifestoDataModel manifestoDataModel, List<Nota> notas) {
        this.id = id;
        this.telefone = telefone;
        this.codigoValidacao = codigoValidacao;
        this.manifestoDataModel = manifestoDataModel;
        this.notas = notas;
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

    public ManifestoDataModel getManifestoDataModel() {
        return manifestoDataModel;
    }

    public List<Nota> getNotas() {
        return notas;
    }
}
