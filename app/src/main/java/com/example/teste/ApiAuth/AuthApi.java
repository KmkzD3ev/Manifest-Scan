package com.example.teste.ApiAuth;

import com.example.teste.ApiService.ServerResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthApi {
    @POST("/api_mdfes/api/manifesto/autorizar")
    Call<ServerResponse> authorizeManifesto(@Body ManifestoRequest2 manifestoRequest);
}
