package com.dnerd.dipty.appusingretrofit.network;

import com.dnerd.dipty.appusingretrofit.ServerResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {
    @GET("/json") //Here, `json` is the PATH PARAMETER
    Call<ServerResponse> getMyIp();
}
