package com.dnerd.dipty.appusingretrofit.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String BASE_URL = "https://ifconfig.co<a data-mce-href=";//https://ifconfig.co" href="https://ifconfig.co"></a>";
    private static Retrofit retrofit = null;

    private static Gson gson = new GsonBuilder()
            .setLenient()
            .create();

    private RetrofitClient() {} // So that nobody can create an object with constructor

    public static Retrofit getClient() {
        if (retrofit == null) {
            synchronized (RetrofitClient.class) { //thread safe Singleton implementation
                if (retrofit == null) {
                    retrofit = new Retrofit.Builder()
                            .baseUrl(BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create(gson))
                            .build();
                }
            }
        }

        return retrofit;
    }

}
