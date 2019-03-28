package org.cs7cs3.team7.journeysharing.httpservice;


import org.cs7cs3.team7.journeysharing.Models.HTTPResponse;
import org.cs7cs3.team7.journeysharing.Models.UserRequest;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface HTTPService {

    @GET("/")
    Call<ResponseBody> test();

    @GET("controller/hello")
    Call<HTTPResponse> hello();

    @POST("user/add")
    Call<String> save(@Body UserRequest user);

    @POST("schedule/add")
    Call<String> addSchedule(@Body Schedule schedule);



}
