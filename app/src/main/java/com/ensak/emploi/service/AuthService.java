package com.ensak.emploi.service;

import com.ensak.emploi.model.AuthResponse;
import com.ensak.emploi.model.IdTokenRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthService {
    @POST("api/google")
    Call<AuthResponse> authenticateWithGoogle(@Body IdTokenRequest request);

}