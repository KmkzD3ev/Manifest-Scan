package com.example.EmissorMdfe.Validation.api;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.EmissorMdfe.Bd.DatabaseHelper;
import com.example.EmissorMdfe.MyApplication;
import com.example.EmissorMdfe.Validation.Model.UserResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// Classe responsável por gerenciar as operações de autenticação de usuário e acesso ao banco de dados
public class UserRepository {
    private ApiService apiService;
    private DatabaseHelper databaseHelper;

    public UserRepository() {
        apiService = RetrofitClient.getInstance();
        databaseHelper = new DatabaseHelper(MyApplication.getContext());
    }
    /**
     * Autentica o usuário chamando a API de autenticação.
     * @param id O ID do usuário.
     * @param phoneNumber O número de telefone do usuário.
     * @param userResponseLiveData O LiveData que será atualizado com a resposta da API.
     */
    public void authenticateUser(String id, String phoneNumber, MutableLiveData<UserResponse> userResponseLiveData) {
        Log.d("UserRepository", "Chamando API de autenticação para ID: " + id + " e Telefone: " + phoneNumber);
        apiService.authenticateUser(id, phoneNumber).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful()) {
                    UserResponse userResponse = response.body();
                    Log.d("UserRepository", "Resposta da API recebida: " + userResponse.toString());
                    userResponseLiveData.setValue(userResponse);
                } else {
                    try {
                        String errorBody = response.errorBody().string();
                        Log.e("UserRepository", "Erro na resposta da API: " + errorBody);
                    } catch (Exception e) {
                        Log.e("UserRepository", "Erro ao processar o corpo de erro da resposta", e);
                    }
                    userResponseLiveData.setValue(null);
                }
            }



            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Log.e("UserRepository", "Falha na chamada da API", t);
                userResponseLiveData.setValue(null);
            }
        });
    }
    /**
     * Obtém o código de validação armazenado para um usuário específico.
     * @param username O nome de usuário (ID do usuário em formato String).
     * @return O código de validação armazenado.
     */

    public String getStoredValidationCode(String username) {
        // Converte o ID de String para int antes de passar para getValidationCode
        int userId = Integer.parseInt(username);
        return databaseHelper.getValidationCode(userId);
    }
}
