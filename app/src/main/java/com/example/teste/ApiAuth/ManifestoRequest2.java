package com.example.teste.ApiAuth;


import com.example.teste.Validation.Model.NotaDataModel;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ManifestoRequest2 {
    @SerializedName("id")
    private int id;

    @SerializedName("telefone")
    private String telefone;

    @SerializedName("manifesto")
    private String manifesto;

    @SerializedName("notas")
    private List<NotaDataModel> notas;

    public ManifestoRequest2(int id, String telefone, String manifesto, List<NotaDataModel> notas) {
        this.id = id;
        this.telefone = telefone;
        this.manifesto = manifesto;
        this.notas = notas;
    }

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

    public String getManifesto() {
        return manifesto;
    }

    public void setManifesto(String manifesto) {
        this.manifesto = manifesto;
    }

    public List<NotaDataModel> getNotas() {
        return notas;
    }

    public void setNotas(List<NotaDataModel> notas) {
        this.notas = notas;
    }

    @Override
    public String toString() {
        return "ManifestoRequest2{" +
                "id=" + id +
                ", telefone='" + telefone + '\'' +
                ", manifesto='" + manifesto + '\'' +
                ", notas=" + notas +
                '}';
    }
}