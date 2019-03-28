package org.cs7cs3.team7.journeysharing;

import org.cs7cs3.team7.journeysharing.Models.HTTPResponse;
import org.cs7cs3.team7.journeysharing.Models.UserRequest;
import org.cs7cs3.team7.journeysharing.httpservice.HTTPClient;
import org.cs7cs3.team7.journeysharing.httpservice.HTTPService;
import org.junit.Test;

import java.io.IOException;

import retrofit2.Call;
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
        UserRequest userRequest = new UserRequest("Bao lei", 1, "110");


        Call<String> res = client.save(userRequest);
        Response<String> response =  res.execute();
        if(response.isSuccessful()){
            System.out.println("succ");
            String user = response.body();

            System.out.println(user);

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

        Call<HTTPResponse> res = client.hello();
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
}
