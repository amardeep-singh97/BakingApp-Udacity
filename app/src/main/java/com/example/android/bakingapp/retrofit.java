package com.example.android.bakingapp;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Amardeep on 10/22/2017.
 */

public class retrofit {

    public static bakingClient requestclient(){
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofitt = builder
                .build();

        bakingClient client = retrofitt.create(bakingClient.class);
        return client;
    }
}
