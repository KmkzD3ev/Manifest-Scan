package com.example.emissormdfe.ApiAuth;

import com.example.emissormdfe.Validation.Model.NotaDataModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Classe de modelo para a solicitação de autorização de manifesto.
 * Utilizada para enviar os dados do manifesto na chamada de API.
 */
public class ManifestoRequest2 {

    @SerializedName("id")
    private int id;

    @SerializedName("telefone")
    private String telefone;

    @SerializedName("manifesto")
    private String manifesto;

    @SerializedName("notas")
    private List<NotaDataModel> notas;

    /**
     * Construtor para inicializar todos os campos da solicitação de manifesto.
     *
     * @param id        ID do usuário.
     * @param telefone  Número de telefone do usuário.
     * @param manifesto Dados do manifesto.
     * @param notas     Lista de notas associadas ao manifesto.
     */
    public ManifestoRequest2(int id, String telefone, String manifesto, List<NotaDataModel> notas) {
        this.id = id;
        this.telefone = telefone;
        this.manifesto = manifesto;
        this.notas = notas;
    }

    /**
     * Obtém o ID do usuário.
     *
     * @return ID do usuário.
     */
    public int getId() {
        return id;
    }

    /**
     * Define o ID do usuário.
     *
     * @param id Novo ID do usuário.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Obtém o número de telefone do usuário.
     *
     * @return Número de telefone do usuário.
     */
    public String getTelefone() {
        return telefone;
    }

    /**
     * Define o número de telefone do usuário.
     *
     * @param telefone Novo número de telefone do usuário.
     */
    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    /**
     * Obtém os dados do manifesto.
     *
     * @return Dados do manifesto.
     */
    public String getManifesto() {
        return manifesto;
    }

    /**
     * Define os dados do manifesto.
     *
     * @param manifesto Novos dados do manifesto.
     */
    public void setManifesto(String manifesto) {
        this.manifesto = manifesto;
    }

    /**
     * Obtém a lista de notas associadas ao manifesto.
     *
     * @return Lista de notas.
     */
    public List<NotaDataModel> getNotas() {
        return notas;
    }

    /**
     * Define a lista de notas associadas ao manifesto.
     *
     * @param notas Nova lista de notas.
     */
    public void setNotas(List<NotaDataModel> notas) {
        this.notas = notas;
    }

    /**
     * Retorna uma representação em string da solicitação de manifesto.
     *
     * @return String representando a solicitação de manifesto.
     */
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
