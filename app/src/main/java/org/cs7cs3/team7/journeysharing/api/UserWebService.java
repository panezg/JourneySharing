package org.cs7cs3.team7.journeysharing.api;

import org.cs7cs3.team7.journeysharing.Models.HTTPResponse;
import org.cs7cs3.team7.journeysharing.database.entity.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserWebService {
    @GET("hello")
    Call<HTTPResponse> hello();

    @GET("/users/{user}")
    Call<User> getUser(@Path("user") String login);

    //Should be users/add
    @POST("user/add")
    Call<HTTPResponse> save(@Body User user);
}
