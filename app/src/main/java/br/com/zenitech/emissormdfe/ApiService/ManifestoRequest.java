package br.com.zenitech.emissormdfe.ApiService;

import com.google.gson.annotations.SerializedName;

public class ManifestoRequest {
    @SerializedName("id")
    private int id;

    @SerializedName("telefone")
    private String telefone;

    @SerializedName("manifesto")
    private String manifesto;

    public ManifestoRequest(int id, String telefone, String manifesto) {
        this.id = id;
        this.telefone = telefone;
        this.manifesto = manifesto;
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
}
