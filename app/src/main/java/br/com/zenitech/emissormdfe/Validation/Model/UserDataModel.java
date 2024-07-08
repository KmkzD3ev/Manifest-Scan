package br.com.zenitech.emissormdfe.Validation.Model;

public class UserDataModel {
    private int id;
    private String telefone;


    public UserDataModel(int id, String telefone) {
        this.id = id;
        this.telefone = telefone;

    }

    public int getId() {
        return id;
    }

    public String getTelefone() {
        return telefone;
    }


}
