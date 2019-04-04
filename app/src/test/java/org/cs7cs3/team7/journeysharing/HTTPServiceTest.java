package org.cs7cs3.team7.journeysharing;

import com.google.gson.JsonElement;

import org.cs7cs3.team7.journeysharing.Models.HTTPResponse;
import org.cs7cs3.team7.journeysharing.Models.ScheduleRequest;
import org.cs7cs3.team7.journeysharing.Models.UserRequest;
import org.cs7cs3.team7.journeysharing.httpservice.HTTPClient;
import org.cs7cs3.team7.journeysharing.httpservice.HTTPService;
import org.junit.Test;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HTTPServiceTest {
    @Test
    public void addUser() throws IOException {
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(Constants.HOST)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//        HTTPService client = retrofit.create(HTTPService.class);

        HTTPService client = HTTPClient.INSTANCE.getClient();
//        User user = new User();
//        user.setEmail("bachen@tcd.ie");
//        user.setGender(0);
//        user.setPhoneNumber("121231");
//        user.setUserName("bao lei");
        UserRequest userRequest = new UserRequest("Bao lei", 0, "11000000");


        Call<HTTPResponse> res = client.save(userRequest);
        Response<HTTPResponse> response =  res.execute();
        System.out.println(res.request().toString());
        if(response.isSuccessful()){
            System.out.println("succ");
            JsonElement user = response.body().getData();
            System.out.println(user.getAsJsonObject().get("id").toString());

            System.out.println(user.toString());

        }else {
            System.out.println("fail");
            System.out.println(response.errorBody().string());
        }
    }

    @Test
    public void  testHello() throws IOException{
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(Constants.HOST)
//                .addConverterFactory(ScalarsConverterFactory.create())//.addConverterFactory(GsonConverterFactory.create())
//                .build();
//        HTTPService client = retrofit.create(HTTPService.class);
        HTTPService client = HTTPClient.INSTANCE.getClient();
        //System.out.print(client.);
        Call<HTTPResponse> res = client.hello();
        System.out.println(res.request().url());
        Response<HTTPResponse> response =  res.execute();
        if(response.isSuccessful()){
            System.out.println("succ");
            HTTPResponse body = response.body();

            System.out.println(body.getStatus());
            System.out.print(body.getData().toString());

        }else {
            System.out.println("fail");
            System.out.println(response.errorBody().string());
        }
    }

    @Test
    public void asyn_testHello() throws IOException{
        HTTPService client = HTTPClient.INSTANCE.getClient();

        Call<HTTPResponse> res = client.hello();
        res.enqueue(new Callback<HTTPResponse>() {
            @Override
            public void onResponse(Call<HTTPResponse> call, Response<HTTPResponse> response) {
                if(response.isSuccessful()){
                    System.out.println("succ");
                    HTTPResponse httpResponse = response.body();
                    System.out.println(httpResponse.getData().toString());
                }
            }

            @Override
            public void onFailure(Call<HTTPResponse> call, Throwable t) {

            }
        });
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSchedule() throws IOException{
        HTTPService client = HTTPClient.INSTANCE.getClient();
        ScheduleRequest scheduleRequest = new ScheduleRequest();
        scheduleRequest.setCommuteType(0);
        scheduleRequest.setEndPosition("end");
        scheduleRequest.setEndPositionLatitude("123");
        scheduleRequest.setEndPositionLongitude("123");
        scheduleRequest.setGenderPreference(1);
        scheduleRequest.setStartPosition("start");
        scheduleRequest.setStartPositionLatitude("1234");
        scheduleRequest.setStartPositionLongitude("1234");
        scheduleRequest.setScheduleDateTime("201904021603");
        scheduleRequest.setUserId(4);
        Call<HTTPResponse> res = client.addSchedule(scheduleRequest);
        Response<HTTPResponse> response = res.execute();
        System.out.println(res.request().toString());
        if(response.isSuccessful()){
            System.out.println("succ");
            HTTPResponse httpResponse = response.body();
            System.out.println(httpResponse.getStatus());

        }else {
            System.out.println("fail");
            System.out.println(response.errorBody().string());
        }

    }
}
