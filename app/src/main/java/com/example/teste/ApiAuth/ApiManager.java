package com.example.teste.ApiAuth;

import android.content.Context;
import android.util.Log;

import com.example.teste.ApiService.ServerResponse;
import com.example.teste.Bd.DatabaseHelper;
import com.example.teste.Validation.Model.UserDataTransferModel;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiManager {
    public interface ApiCallback {
        void onResponse(boolean authorized, String message);
    }

    public static void testApiCall(Context context, ApiCallback callback) {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        UserDataTransferModel userDataTransferModel = dbHelper.getUserDataTransferModel();

        if (userDataTransferModel == null) {
            callback.onResponse(false, "Usuário não está logado ou dados insuficientes.");
            return;
        }

        AuthApi authApi = RetrofitClient.getInstance();
        ManifestoRequest2 request = new ManifestoRequest2(
                userDataTransferModel.getId(),
                userDataTransferModel.getTelefone(),
                userDataTransferModel.getManifestoDataModel().getManifesto(),
                userDataTransferModel.getNotas()
        );

        Log.d("ApiManager", "Request: " + request.toString());

        Call<ServerResponse> call = authApi.authorizeManifesto(request);
        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                if (response.isSuccessful()) {
                    ServerResponse serverResponse = response.body();
                    Log.d("ApiManager", "Response: " + serverResponse.getMessage() + " Code: " + serverResponse.getCode());

                    if ("SUCCESS".equals(serverResponse.getStatus()) && "000".equals(serverResponse.getCode())) {
                        callback.onResponse(true, serverResponse.getMessage());
                    } else {
                        callback.onResponse(false, serverResponse.getMessage());
                    }
                } else {
                    try {
                        if (response.errorBody() != null) {
                            String errorResponse = response.errorBody().string();
                            Log.e("ApiManager", "Error response: " + errorResponse);
                            callback.onResponse(false, errorResponse);
                        } else {
                            callback.onResponse(false, "Erro desconhecido");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        callback.onResponse(false, "Erro ao processar a resposta");
                    }
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                Log.e("ApiManager", "API call failed", t);
                callback.onResponse(false, "Falha na chamada da API");
            }
        });
    }
}
