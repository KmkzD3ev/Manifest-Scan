package com.example.teste.Validation.api;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.teste.Bd.DatabaseHelper;
import com.example.teste.MyApplication;
import com.example.teste.Validation.Model.UserResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {
    private ApiService apiService;
    private DatabaseHelper databaseHelper;

    public UserRepository() {
        apiService = RetrofitClient.getInstance();
        databaseHelper = new DatabaseHelper(MyApplication.getContext());
    }

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

    public String getStoredValidationCode(String username) {
        // Converte o ID de String para int antes de passar para getValidationCode
        int userId = Integer.parseInt(username);
        return databaseHelper.getValidationCode(userId);
    }
}
