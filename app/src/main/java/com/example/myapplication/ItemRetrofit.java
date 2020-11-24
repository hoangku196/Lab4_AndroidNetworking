package com.example.myapplication;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

class ItemRetrofit {
    public static Retrofit retrofit;

    public static Retrofit getInstance(String link) {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(link)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit;
    }
}
