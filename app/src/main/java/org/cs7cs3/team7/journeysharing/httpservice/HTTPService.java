package org.cs7cs3.team7.journeysharing.httpservice;

import org.cs7cs3.team7.journeysharing.Models.User;
import org.cs7cs3.team7.journeysharing.Models.UserRequest;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface HTTPService {

    @GET("/")
    Call<ResponseBody> test();

  //  @FormUrlEncoded
    @POST("user/add")
    Call<String> register(@Body UserRequest userRequest);
   // Call<User> register(@Field("userName") String userName, @Field("email") String email, @Field("gender") int gender, @Field("phoneNumber") String phoneNumber);

    @GET("controller/hello")
    Call<String> hello();
//
//    @POST("schedule/add")
//    Class<String>



}
