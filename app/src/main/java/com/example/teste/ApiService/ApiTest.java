package com.example.teste.ApiService;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiTest {
    public interface ApiCallback {
        void onResponse(boolean authorized, String message);
    }

    public static void testApiCall(Context context, int id, String phoneNumber, String barcodeResult, ApiCallback callback) {
        ApiService apiService = RetrofitClient.getInstance();
        ManifestoRequest request = new ManifestoRequest(id, phoneNumber, barcodeResult);

        Call<ServerResponse> call = apiService.validateManifesto(request);
        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                if (response.isSuccessful()) {
                    ServerResponse serverResponse = response.body();
                    Log.d("ApiTest", "Response: " + serverResponse.getMessage());

                    if ("SUCCESS".equals(serverResponse.getStatus()) && "000".equals(serverResponse.getCode())) {
                        callback.onResponse(true, serverResponse.getMessage());
                    } else {
                        callback.onResponse(false, serverResponse.getMessage());
                    }
                } else {
                    try {
                        if (response.errorBody() != null) {
                            String errorResponse = response.errorBody().string();
                            Log.e("ApiTest", "Error response: " + errorResponse);
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
                Log.e("ApiTest", "API call failed", t);
                callback.onResponse(false, "Falha na chamada da API");
            }
        });
    }
}
