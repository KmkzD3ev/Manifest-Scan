package com.example.EmissorMdfe.Validation.Model;

import com.google.gson.annotations.SerializedName;

public class UserResponse {
    @SerializedName("codigo_condutor")
    private int codigoCondutor;

    @SerializedName("nome_condutor")
    private String nomeCondutor;

    @SerializedName("cpf_condutor")
    private String cpfCondutor;

    @SerializedName("fone_condutor")
    private String foneCondutor;

    @SerializedName("usa_app")
    private int usaApp;

    @SerializedName("ativo_condutor")
    private int ativoCondutor;

    @SerializedName("codigo_validacao")
    private int codigoValidacao;

    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;
    private String code;  // Adicione este campo

    // Getters and Setters
    public int getCodigoCondutor() {
        return codigoCondutor;
    }

    public void setCodigoCondutor(int codigoCondutor) {
        this.codigoCondutor = codigoCondutor;
    }

    public String getNomeCondutor() {
        return nomeCondutor;
    }

    public void setNomeCondutor(String nomeCondutor) {
        this.nomeCondutor = nomeCondutor;
    }

    public String getCpfCondutor() {
        return cpfCondutor;
    }

    public void setCpfCondutor(String cpfCondutor) {
        this.cpfCondutor = cpfCondutor;
    }

    public String getFoneCondutor() {
        return foneCondutor;
    }

    public void setFoneCondutor(String foneCondutor) {
        this.foneCondutor = foneCondutor;
    }

    public int getUsaApp() {
        return usaApp;
    }

    public void setUsaApp(int usaApp) {
        this.usaApp = usaApp;
    }

    public int getAtivoCondutor() {
        return ativoCondutor;
    }

    public void setAtivoCondutor(int ativoCondutor) {
        this.ativoCondutor = ativoCondutor;
    }

    public int getCodigoValidacao() {
        return codigoValidacao;
    }

    public void setCodigoValidacao(int codigoValidacao) {
        this.codigoValidacao = codigoValidacao;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


    @Override
    public String toString() {
        return "UserResponse{" +
                "codigoCondutor=" + codigoCondutor +
                ", nomeCondutor='" + nomeCondutor + '\'' +
                ", cpfCondutor='" + cpfCondutor + '\'' +
                ", foneCondutor='" + foneCondutor + '\'' +
                ", usaApp=" + usaApp +
                ", ativoCondutor=" + ativoCondutor +
                ", codigoValidacao=" + codigoValidacao +
                ", status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}