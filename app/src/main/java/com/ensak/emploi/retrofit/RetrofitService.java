package com.ensak.emploi.retrofit;

import com.google.gson.Gson;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitService {
    private Retrofit retrofit;

    public RetrofitService() {
        initializeRetrofit();
    }

    private void initializeRetrofit() {
        //TODO: go to ipconfig in cmd and in the wireless lan wifi get the ipv4 address and for the port get the port for the springboot app
        retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.88:8080")
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .build();
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }
}
