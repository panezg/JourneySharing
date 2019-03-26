package org.cs7cs3.team7.journeysharing.httpservice;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.cs7cs3.team7.journeysharing.Constants;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public enum HTTPClient {

    INSTANCE;
    private Retrofit retrofit;
    HTTPClient(){

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.HOST)
                .addConverterFactory(GsonConverterFactory.create(gson))
//                .addConverterFactory(ScalarsConverterFactory.create())//.addConverterFactory(GsonConverterFactory.create())
                .build();

    }


    public synchronized HTTPService getClient()  {
        return retrofit.create(HTTPService.class);
    }
}
