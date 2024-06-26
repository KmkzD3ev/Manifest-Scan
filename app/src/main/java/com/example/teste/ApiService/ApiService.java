package com.example.teste.ApiService;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("/api_mdfes/api/manifesto/validar")
    Call<ServerResponse> validateManifesto(@Body ManifestoRequest manifestoRequest);
}
