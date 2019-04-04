package org.cs7cs3.team7.journeysharing;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.cs7cs3.team7.journeysharing.Models.HTTPResponse;
import org.cs7cs3.team7.journeysharing.Models.JourneyRequest;
import org.cs7cs3.team7.journeysharing.Models.ScheduleRequest;
import org.cs7cs3.team7.journeysharing.Models.UserRequest;
import org.cs7cs3.team7.journeysharing.database.entity.User;
import org.cs7cs3.team7.journeysharing.httpservice.HTTPClient;
import org.cs7cs3.team7.journeysharing.httpservice.HTTPService;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    /*
     private MutableLiveData<Map<String, List<User>>> resultsOfOnlineModel;
      private MutableLiveData<Map<String, JourneyRequest> resultsOfOnlineModel;
     */
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
//            System.out.println(httpResponse.getData().toString());
            JsonElement element = httpResponse.getData();
            JsonArray schedules =  element.getAsJsonArray();
            for(JsonElement schedule : schedules){
                System.out.println(schedule.toString());
                System.out.println("ID:"+schedule.getAsJsonObject().get("id"));
                System.out.println("Date:"+schedule.getAsJsonObject().get("scheduleDateTime"));
                System.out.println("StartPos:"+schedule.getAsJsonObject().get("startPosition"));
                System.out.println("endPos:"+schedule.getAsJsonObject().get("endPosition"));
                System.out.println("status: "+schedule.getAsJsonObject().get("status"));
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
        -status
        -users
            -userName
            -userId
            -phoneNumber
            -gender
            -status (if 1 succ other fail)
     */

    @Test
    public void testMapper() throws IOException{
        HTTPService client = HTTPClient.INSTANCE.getClient();
        int id = 6;
        Call<HTTPResponse> res = client.checkSchedule(id);
        Response<HTTPResponse> response = res.execute();
        System.out.println(res.request().toString());
        if(response.isSuccessful()){
           JsonElement data = response.body().getData();
           Map<String, JourneyRequest> schedules = parseSchedule(data, new User(1,"","dummy", "112", "1"));
           Map<String, List<User>> membersMap = parseMembers(data);
           for(Map.Entry<String, JourneyRequest> journey : schedules.entrySet()) {
               System.out.println("id: "+ journey.getKey());
               System.out.println("info: "+ journey.getValue().toString());
            }

            System.out.println();
           System.out.println();
           for(Map.Entry<String, List<User>> members : membersMap.entrySet()){
               System.out.println("id" + members.getKey());
               for(User u : members.getValue()){
                   System.out.println(u);
                   System.out.println();
               }
           }

        }else {

        }
    }


    private Map<String, JourneyRequest> parseSchedule(JsonElement element, User currentUser){
        JsonArray schedules =  element.getAsJsonArray();
        Map<String, JourneyRequest> res = new HashMap<>();
        for(JsonElement schedule : schedules){
            String gender = schedule.getAsJsonObject().get("genderPreference").toString();
            String method = schedule.getAsJsonObject().get("commuteType").toString();
            String endPos = schedule.getAsJsonObject().get("endPosition").toString();
            String startPos = schedule.getAsJsonObject().get("startPosition").toString();
            String status = schedule.getAsJsonObject().get("status").toString();
            String time = schedule.getAsJsonObject().get("scheduleDateTime").toString();
            String id = schedule.getAsJsonObject().get("id").toString();
            JourneyRequest journeyRequest = new JourneyRequest(currentUser, gender, method, endPos, false);
            journeyRequest.setStartPoint(startPos);
            journeyRequest.setTime(time);

            if(status.equals("1")){
                journeyRequest.setState(JourneyRequest.JourneyRequestStatus.FINISHED);
            }else {
                journeyRequest.setState(JourneyRequest.JourneyRequestStatus.SCHEDULED);
            }
            res.put(id, journeyRequest);
        }
        return res;
    }

    private Map<String, List<User>> parseMembers(JsonElement element){
        JsonArray schedules =  element.getAsJsonArray();
        Map<String, List<User>> res = new HashMap<>();
        for(JsonElement schedule : schedules){
            JsonArray users = schedule.getAsJsonObject().getAsJsonArray("users");
            String id = schedule.getAsJsonObject().get("id").toString();
            List<User> userInfos = new ArrayList<>();
            for(JsonElement user : users){
                String userName = user.getAsJsonObject().get("userName").toString();
                String userId = user.getAsJsonObject().get("id").toString();
                String phoneNumber = user.getAsJsonObject().get("phoneNumber").toString();
                String gender = user.getAsJsonObject().get("gender").toString();
                User userInfo = new User(Integer.parseInt(userId), "",  userName, phoneNumber, gender);
                userInfos.add(userInfo);
            }
            res.put(id, userInfos);
        }
        return res;
    }
}
