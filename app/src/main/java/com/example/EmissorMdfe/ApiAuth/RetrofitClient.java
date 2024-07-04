package com.example.EmissorMdfe.ApiAuth;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String BASE_URL = "https://siacphp8.jelastic.saveincloud.net/api_mdfes/api/";
    private static Retrofit retrofit = null;

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
