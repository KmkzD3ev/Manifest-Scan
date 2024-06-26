package com.example.teste.Validation.api;

import com.example.teste.Validation.Model.UserResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiService {
    @GET("condutor/autenticar/{id}/{phoneNumber}")
    Call<UserResponse> authenticateUser(
            @Path("id") String id,
            @Path("phoneNumber") String phoneNumber
    );
}
