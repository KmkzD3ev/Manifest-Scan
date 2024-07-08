package com.example.emissormdfe.ApiAuth;

import com.example.emissormdfe.ApiService.ServerResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Interface AuthApi para definir chamadas de API relacionadas à autorização de manifestos.
 * Utiliza Retrofit para facilitar a comunicação HTTP.
 */
public interface AuthApi {
    /**
     * Faz uma solicitação POST para autorizar um manifesto.
     * A URL base é configurada em RetrofitClient.
     *
     * @param manifestoRequest Objeto contendo os dados do manifesto a ser autorizado.
     * @return Um objeto Call que pode ser usado para enviar a solicitação HTTP.
     */
    @POST("/api_mdfes/api/manifesto/autorizar")
    Call<ServerResponse> authorizeManifesto(@Body ManifestoRequest2 manifestoRequest);
}
