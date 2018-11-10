package com.dnerd.dipty.retrofitexample.request_interface;

import com.dnerd.dipty.retrofitexample.model.GithubRepo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RetrofitClientInterface {
    @GET("/users/{user}/repos")
    Call<List<GithubRepo>> reposForUser(@Path("user")String user); // Call object makes it async
}
