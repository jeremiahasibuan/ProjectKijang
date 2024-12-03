package com.example.repeatit.Api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String BASE_URL = "http://10.0.2.2:3000/";  // Ganti dengan URL server Anda
    private static Retrofit retrofit = null;

    // Mengambil instance Retrofit
    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)  // URL server
                    .addConverterFactory(GsonConverterFactory.create())  // Konversi response JSON ke object
                    .build();
        }
        return retrofit;
    }
}

