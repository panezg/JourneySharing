package org.cs7cs3.team7.journeysharing.httpservice;


import org.cs7cs3.team7.journeysharing.Models.HTTPResponse;
import org.cs7cs3.team7.journeysharing.Models.JourneyRequest;
import org.cs7cs3.team7.journeysharing.Models.ScheduleRequest;
import org.cs7cs3.team7.journeysharing.Models.UserRequest;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface HTTPService {

    @GET("/")
    Call<ResponseBody> test();

    @GET("hello")
    Call<HTTPResponse> hello();

    @POST("user/add")
    Call<HTTPResponse> save(@Body UserRequest user);

    @POST("schedule/add")
    Call<HTTPResponse> addSchedule(@Body ScheduleRequest schedule);

    @GET("schedule/users")
    Call<HTTPResponse> checkSchedule(@Query("userId") int userId);

}
