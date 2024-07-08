package com.example.emissormdfe.ApiService;

import android.content.Context;
import android.util.Log;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//Api para validaçao de manifesto com o servidor
public class ApiTest {
    public interface ApiCallback {
        void onResponse(boolean authorized, String message);
    }
    /**
     * Método que faz a chamada de API para validar um manifesto.
     * @param context O contexto da aplicação.
     * @param id O ID do usuário.
     * @param phoneNumber O número de telefone do usuário.
     * @param barcodeResult O código de barras do manifesto.
     * @param callback O callback para processar a resposta da API.
     */

    public static void testApiCall(Context context, int id, String phoneNumber, String barcodeResult, ApiCallback callback) {
        ApiService apiService = RetrofitClient.getInstance();
        ManifestoRequest request = new ManifestoRequest(id, phoneNumber, barcodeResult);

        Call<ServerResponse> call = apiService.validateManifesto(request);
        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                if (response.isSuccessful()) {
                    ServerResponse serverResponse = response.body();
                    Log.d("ApiTest", "Response: " + serverResponse.getMessage() + " Code: " + serverResponse.getCode());

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
