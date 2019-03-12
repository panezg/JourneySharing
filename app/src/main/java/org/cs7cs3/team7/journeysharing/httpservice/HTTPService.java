package org.cs7cs3.team7.journeysharing.httpservice;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface HTTPService {
    @GET("users/{user}/repos")
    Call<ResponseBody> listRepos(@Path("user") String user);
    @GET("/")
    Call<ResponseBody> test();
}
