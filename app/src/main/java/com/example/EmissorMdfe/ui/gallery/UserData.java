package com.example.EmissorMdfe.ui.gallery;

import com.example.EmissorMdfe.Validation.Model.Nota;

import java.util.List;

/**
 * Classe UserData para encapsular os dados do usuário.
 * Inclui informações de contato, código de validação, código de manifesto e uma lista de notas.
 */
public class UserData {
    private int userId;
    private String phoneNumber;
    private int validationCode;
    private String manifestoCode;
    private List<Nota> notes;

    /**
     * Construtor para inicializar todos os campos de UserData.
     *
     * @param userId ID do usuário.
     * @param phoneNumber Número de telefone do usuário.
     * @param validationCode Código de validação do usuário.
     * @param manifestoCode Código do manifesto associado ao usuário.
     * @param notes Lista de notas associadas ao manifesto.
     */
    public UserData(int userId, String phoneNumber, int validationCode, String manifestoCode, List<Nota> notes) {
        this.userId = userId;
        this.phoneNumber = phoneNumber;
        this.validationCode = validationCode;
        this.manifestoCode = manifestoCode;
        this.notes = notes;
    }

    /**
     * Obtém o ID do usuário.
     *
     * @return ID do usuário.
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Define o ID do usuário.
     *
     * @param userId Novo ID do usuário.
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * Obtém o número de telefone do usuário.
     *
     * @return Número de telefone do usuário.
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Define o número de telefone do usuário.
     *
     * @param phoneNumber Novo número de telefone do usuário.
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Obtém o código de validação do usuário.
     *
     * @return Código de validação do usuário.
     */
    public int getValidationCode() {
        return validationCode;
    }

    /**
     * Define o código de validação do usuário.
     *
     * @param validationCode Novo código de validação do usuário.
     */
    public void setValidationCode(int validationCode) {
        this.validationCode = validationCode;
    }

    /**
     * Obtém o código do manifesto.
     *
     * @return Código do manifesto.
     */
    public String getManifestoCode() {
        return manifestoCode;
    }

    /**
     * Define o código do manifesto.
     *
     * @param manifestoCode Novo código do manifesto.
     */
    public void setManifestoCode(String manifestoCode) {
        this.manifestoCode = manifestoCode;
    }

    /**
     * Obtém a lista de notas associadas ao manifesto.
     *
     * @return Lista de notas.
     */
    public List<Nota> getNotes() {
        return notes;
    }

    /**
     * Define a lista de notas associadas ao manifesto.
     *
     * @param notes Nova lista de notas.
     */
    public void setNotes(List<Nota> notes) {
        this.notes = notes;
    }
}
