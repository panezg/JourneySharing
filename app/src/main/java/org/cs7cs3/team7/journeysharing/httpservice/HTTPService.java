package org.cs7cs3.team7.journeysharing.httpservice;

import org.cs7cs3.team7.journeysharing.Models.UserInfo;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface HTTPService {

    @GET("/")
    Call<ResponseBody> test();

    @POST("/user/add")
    Call<UserInfo> save(@Body UserInfo user);



}
