package com.dnerd.dipty.retrofitexample.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {
    private  static Retrofit mRetrofit;
    private static final  String BASE_URL = "https://api.github.com/";

    public static Retrofit getRetrofitInstance() {
        if(mRetrofit == null)
        {
            mRetrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
        }
        return mRetrofit;
    }
}
