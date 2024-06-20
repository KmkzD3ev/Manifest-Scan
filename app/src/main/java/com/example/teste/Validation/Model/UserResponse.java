package com.example.teste.Validation.Model;

public class UserResponse {
    private int codigo_condutor;
    private String nome_condutor;
    private String cpf_condutor;
    private String fone_condutor;
    private int usa_app;
    private int ativo_condutor;
    private int codigo_validacao;

    // Getters and Setters
    public int getCodigo_condutor() {
        return codigo_condutor;
    }

    public void setCodigo_condutor(int codigo_condutor) {
        this.codigo_condutor = codigo_condutor;
    }

    public String getNome_condutor() {
        return nome_condutor;
    }

    public void setNome_condutor(String nome_condutor) {
        this.nome_condutor = nome_condutor;
    }

    public String getCpf_condutor() {
        return cpf_condutor;
    }

    public void setCpf_condutor(String cpf_condutor) {
        this.cpf_condutor = cpf_condutor;
    }

    public String getFone_condutor() {
        return fone_condutor;
    }

    public void setFone_condutor(String fone_condutor) {
        this.fone_condutor = fone_condutor;
    }

    public int getUsa_app() {
        return usa_app;
    }

    public void setUsa_app(int usa_app) {
        this.usa_app = usa_app;
    }

    public int getAtivo_condutor() {
        return ativo_condutor;
    }

    public void setAtivo_condutor(int ativo_condutor) {
        this.ativo_condutor = ativo_condutor;
    }

    public int getCodigo_validacao() {
        return codigo_validacao;
    }

    public void setCodigo_validacao(int codigo_validacao) {
        this.codigo_validacao = codigo_validacao;
    }
}
