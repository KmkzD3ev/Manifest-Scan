package com.example.teste.Validation.api;

import androidx.lifecycle.MutableLiveData;

import com.example.teste.Validation.Model.UserResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {
    private ApiService apiService;

    public UserRepository() {
        apiService = RetrofitClient.getInstance();
    }

    public void authenticateUser(String id, String phoneNumber, MutableLiveData<UserResponse> userResponseLiveData) {
        apiService.authenticateUser(id, phoneNumber).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful()) {
                    userResponseLiveData.setValue(response.body());
                } else {
                    userResponseLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                userResponseLiveData.setValue(null);
            }
        });
    }
}
