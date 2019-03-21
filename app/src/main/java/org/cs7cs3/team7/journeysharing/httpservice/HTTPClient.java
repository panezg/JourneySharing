package org.cs7cs3.team7.journeysharing.httpservice;

import org.cs7cs3.team7.journeysharing.Constants;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public enum HTTPClient {

    INSTANCE;
    private Retrofit retrofit;
    HTTPClient(){
        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.HOST)
                .addConverterFactory(GsonConverterFactory.create())   //.addConverterFactory(ScalarsConverterFactory.create())//.addConverterFactory(GsonConverterFactory.create())
                .build();

    }


    public synchronized HTTPService getClient()  {
        return retrofit.create(HTTPService.class);
    }
}
