package org.cs7cs3.team7.journeysharing;

import com.google.gson.Gson;

import org.cs7cs3.team7.journeysharing.Models.UserInfo;
import org.cs7cs3.team7.wifidirect.Message;

import java.util.Date;
import java.util.HashMap;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.runner.AndroidJUnit4;

@RunWith(AndroidJUnit4.class)
public class TestGson {
//    @Test
//    public void testGson() {
//        Message msg = new Message();
//
//        msg.setFromIP("192.168.1.1");
//        msg.setFromMAC("35:30:22:33");
//        msg.setMessageText("Jia-test");
//        msg.setTimeStamp("12:00");
//
//        UserInfo me = new UserInfo();
//        me.setName("mengxuan");
//        me.setGender("Male");
//        me.setPhoneNum("123");
//        HashMap<String, UserInfo> list = new HashMap<>();
//        UserInfo jinchi = new UserInfo();
//        jinchi.setPhoneNum("456");
//        jinchi.setGender("Male");
//        jinchi.setName("jinchi");
//        UserInfo Paras = new UserInfo();
//        Paras.setName("paras");
//        Paras.setGender("Male");
//        Paras.setPhoneNum("123");
//        list.put("Jinchi", jinchi);
//        list.put("Paras", Paras);
//
//        msg.setSender(me);
//        //msg.setList(list);
//
//        String jsonString = new Gson().toJson(msg);
//        System.out.println(jsonString);
//
//        Message newMsg = new Gson().fromJson(jsonString, Message.class);
//        System.out.println(newMsg.toString());
//    }
//
//    @Test
//    public void testJsonfySchedule(){
//        Schedule schedule = new Schedule();
//        Date date = new Date();
//        System.out.print(date.toString());
//    }
}

