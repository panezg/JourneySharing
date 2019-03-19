package org.cs7cs3.team7.journeysharing;

import com.google.gson.Gson;

import org.cs7cs3.team7.wifidirect.Message;
import org.cs7cs3.team7.wifidirect.UserInfo;

import java.util.HashMap;

public class TestGson {
    public static void main(String[] args) {
        Message msg = new Message();

        msg.setFromIP("192.168.1.1");
        msg.setFromMAC("35:30:22:33");
        msg.setMessageText("Jia-test");
        msg.setTimeStamp("12:00");

        UserInfo me = new UserInfo("Mengxuan", "12345", "Hangzhou");
        HashMap<String, UserInfo> list = new HashMap<>();
        list.put("Jinchi", new UserInfo("Jinchi", "123", "Chongqing"));
        list.put("Paras", new UserInfo("Paras", "123", "Dublin"));

        msg.setSender(me);
        msg.setList(list);

        String jsonString = new Gson().toJson(msg);
        System.out.println(jsonString);

        Message newMsg = new Gson().fromJson(jsonString, Message.class);
        System.out.println(newMsg.toString());
    }
}

