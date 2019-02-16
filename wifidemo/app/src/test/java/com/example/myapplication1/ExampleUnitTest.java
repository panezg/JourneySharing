package com.example.myapplication1;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);

    }

    @Test
    public void Msg_seri() {
        Message msg = new Message();
        msg.msg = "test";
        msg.port=1234;
        msg.ip="192.169.0.1";
        Gson gson = new GsonBuilder().create();
        System.out.println(gson.toJson(msg,Message.class));

        String incoming=gson.toJson(msg,Message.class);
        Message newMsg;
        newMsg=gson.fromJson(incoming,Message.class);
        System.out.println(newMsg.ip);


    }
}