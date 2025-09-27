package com.ensak.emploi.retrofit;

import com.ensak.emploi.model.Prof;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ProfApi {
    //TODO: in the spring boot we should have the function returning the list of profs
    @GET("/api/professors")
    Call<List<Prof>> getAllProfs();

    //TODO: change the prof model
    @POST("/api/professors")
    Call<Prof> save(@Body Prof prof);

    @GET("/api/professors/{id}")
    Call<Prof> getById(@Body Long id);
}
