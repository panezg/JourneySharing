package org.cs7cs3.team7.journeysharing;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

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

    @Test
    public void checkSchedule() throws IOException{
        HTTPService client = HTTPClient.INSTANCE.getClient();
        int id = 6;
        Call<HTTPResponse> res = client.checkSchedule(id);
        Response<HTTPResponse> response = res.execute();
        System.out.println(res.request().toString());
        if(response.isSuccessful()){
            System.out.println("succ");
            HTTPResponse httpResponse = response.body();
            System.out.println(httpResponse.getData().toString());
            JsonElement element = httpResponse.getData();
            JsonArray schedules =  element.getAsJsonArray();
            for(JsonElement schedule : schedules){
                System.out.println(schedule.toString());
                System.out.println("ID:"+schedule.getAsJsonObject().get("id"));
                System.out.println("Date:"+schedule.getAsJsonObject().get("scheduleDateTime"));
                System.out.println("StartPos:"+schedule.getAsJsonObject().get("startPosition"));
                System.out.println("endPos:"+schedule.getAsJsonObject().get("endPosition"));
                JsonArray users = schedule.getAsJsonObject().getAsJsonArray("users");
                for(JsonElement user : users){
                    System.out.println("name: "+ user.getAsJsonObject().get("userName"));
                    System.out.println("userId: " + user.getAsJsonObject().get("id"));
                    System.out.println("phoneNumber: " + user.getAsJsonObject().get("phoneNumber"));
                    System.out.println("gender: " + user.getAsJsonObject().get("gender"));
                    System.out.println();
                }
                System.out.println();
                System.out.println();
                System.out.println();
            }

        }else {
            System.out.println("fail");
            System.out.println(response.errorBody().string());
        }

    }

    /*
    needed field
        -schedule id
        -date
        -startposition
        -endPosition
        -users
            -userName
            -userId
            -phoneNumber
            -gender
     */

    public final String dummySchedule = "{\"data\":[{\"commuteType\":1,\"createBy\":\"SYS\",\"createDate\":1552492800000,\"endDuration\":57600000,\"endPosition\":\"howth\",\"endPositionLatitude\":\"200\",\"endPositionLongitude\":\"200\",\"engageTime\":1552572536000,\"genderPreference\":1,\"id\":1,\"ratingPreference\":3,\"startDuration\":-28800000,\"startPosition\":\"tcd\",\"startPositionLatitude\":\"100\",\"startPositionLongitude\":\"100\",\"status\":0,\"updateBy\":\"SYS\",\"updateDate\":1552492800000,\"userId\":1,\"users\":[{\"createBy\":\"sys\",\"createDate\":1550764800000,\"email\":\"1111@gmail.com\",\"gender\":\"1\",\"id\":1,\"phoneNumber\":993883838,\"updateBy\":\"sys\",\"updateDate\":1550764800000,\"userName\":\"John\"},{\"createBy\":\"sys\",\"createDate\":1552233600000,\"email\":\"1111@gmail.com\",\"gender\":\"1\",\"id\":2,\"phoneNumber\":993883839,\"updateBy\":\"sys\",\"updateDate\":1552233600000,\"userName\":\"John\"}],\"weekday\":1},{\"commuteType\":1,\"createBy\":\"SYS\",\"createDate\":1552924800000,\"endDuration\":57600000,\"endPosition\":\"howth\",\"endPositionLatitude\":\"200\",\"endPositionLongitude\":\"200\",\"engageTime\":1553001532000,\"genderPreference\":1,\"id\":3,\"ratingPreference\":3,\"startDuration\":-28800000,\"startPosition\":\"tcd\",\"startPositionLatitude\":\"100\",\"startPositionLongitude\":\"100\",\"status\":0,\"updateBy\":\"SYS\",\"updateDate\":1552924800000,\"userId\":1,\"users\":[],\"weekday\":2},{\"commuteType\":1,\"createBy\":\"SYS\",\"createDate\":1553702400000,\"endDuration\":57600000,\"endPositionLatitude\":\"200\",\"endPositionLongitude\":\"200\",\"engageTime\":1553788298000,\"genderPreference\":1,\"id\":6,\"ratingPreference\":3,\"startDuration\":-28800000,\"startPositionLatitude\":\"100\",\"startPositionLongitude\":\"100\",\"status\":0,\"updateBy\":\"SYS\",\"updateDate\":1553702400000,\"userId\":1,\"users\":[],\"weekday\":1},{\"commuteType\":1,\"createBy\":\"SYS\",\"createDate\":1554134400000,\"currentServer\":\"1\",\"endDuration\":1554134400000,\"endDuration2\":1554134400000,\"endPosition\":\"Dublin 6\",\"endPositionLatitude\":\"200\",\"endPositionLongitude\":\"200\",\"genderPreference\":1,\"id\":7,\"ratingPreference\":4,\"startDuration\":1554134400000,\"startDuration2\":1554134400000,\"startPosition\":\"tcd\",\"startPositionLatitude\":\"100\",\"startPositionLongitude\":\"100\",\"status\":0,\"updateBy\":\"SYS\",\"updateDate\":1554134400000,\"userId\":1,\"users\":[],\"weekday\":1},{\"commuteType\":1,\"createBy\":\"SYS\",\"createDate\":1554134400000,\"currentServer\":\"1\",\"endDuration\":1554134400000,\"endDuration2\":1554134400000,\"endPosition\":\"Dublin 6\",\"endPositionLatitude\":\"200\",\"endPositionLongitude\":\"200\",\"genderPreference\":1,\"id\":8,\"scheduleDateTime\":\"201904011200\",\"startDuration\":1554134400000,\"startDuration2\":1554134400000,\"startPosition\":\"tcd\",\"startPositionLatitude\":\"100\",\"startPositionLongitude\":\"100\",\"status\":0,\"updateBy\":\"SYS\",\"updateDate\":1554134400000,\"userId\":1,\"users\":[],\"weekday\":1}],\"status\":\"sucuess\"}";
    @Test
    public void parseSchedule(){
        JsonElement element = new JsonParser().parse(dummySchedule).getAsJsonObject().get("data");
        JsonArray schedules =  element.getAsJsonArray();
        for(JsonElement schedule : schedules){
            System.out.println(schedule);
            System.out.println("ID:"+schedule.getAsJsonObject().get("id"));
            System.out.println("Date:"+schedule.getAsJsonObject().get("scheduleDateTime"));
        }
    }
}
