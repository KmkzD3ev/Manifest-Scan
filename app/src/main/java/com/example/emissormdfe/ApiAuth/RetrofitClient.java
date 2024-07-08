package com.example.emissormdfe.ApiAuth;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Classe RetrofitClient para configurar e fornecer uma instância de Retrofit.
 * Esta classe segue o padrão Singleton para garantir que apenas uma instância de Retrofit seja criada.
 */
public class RetrofitClient {
    private static final String BASE_URL = "https://siacphp8.jelastic.saveincloud.net/api_mdfes/api/";
    private static Retrofit retrofit = null;

    /**
     * Retorna uma instância única de AuthApi.
     * Inicializa Retrofit se ainda não foi inicializado.
     *
     * @return Instância de AuthApi para fazer chamadas de API.
     */
    public static AuthApi getInstance() {
        if (retrofit == null) {
            OkHttpClient client = new OkHttpClient.Builder().build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(AuthApi.class);
    }
}
