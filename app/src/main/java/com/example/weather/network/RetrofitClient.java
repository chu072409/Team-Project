package com.example.weather.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
//test
public class RetrofitClient {

    private static final String BASE_URL = "https://api.openweathermap.org/";
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
