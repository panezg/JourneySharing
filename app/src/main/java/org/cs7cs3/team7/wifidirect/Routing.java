package org.cs7cs3.team7.wifidirect;

import android.util.Log;

import org.cs7cs3.team7.journeysharing.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Routing {
    private Map<String,UserInfo> userInfoList;
    private boolean groupsReady;

    public Map<String, UserInfo> getUserInfoList() {
        return userInfoList;
    }
    public void setUserInfoList(Map<String, UserInfo> userInfoList) {
        this.userInfoList = userInfoList;
    }
    public boolean isGroupsReady() {
        return groupsReady;
    }
    public void setGroupsReady(boolean groupsReady) {
        this.groupsReady = groupsReady;
    }

    public Routing(){
        Log.d("ROUTING", "Routing object initialized");
        userInfoList=new HashMap<>();
    }
    public void recordPeerMessage(Message message){
        //as we dont have many devices, let's make some data
        if(userInfoList.size()==0){
            Log.d("ROUTING", "Adding dummy data to simulate people around");
//            letsFakeSomeData();
        }
        Log.d("ROUTING", "Recording peer trip info");
        userInfoList.put(message.getSender()==null? "dummyName":message.getSender().getName(),message.getSender());
        if (userInfoList.size()> Constants.THRESHOLD_TO_START_ROUTING){
            Log.d("ROUTING", "Enough people to run routing logic. Calling find Matches");
            findMatches();
        }
        Log.d("ROUTING", "No. Peers' request recorded "+userInfoList.size());
    }

    private void findMatches(){
        Iterator iterator=userInfoList.keySet().iterator();
        int i=1;
        for (String key : userInfoList.keySet()) {
            UserInfo userInfo=userInfoList.get(key);
            userInfo.setGroupId( i++%2==0? 1: 2);
        }
        Log.d("ROUTING", "Peple matching is finished and trips are ready!");
        groupsReady=true;
    }

//    private void letsFakeSomeData() {
//        UserInfo pankaj=new UserInfo("pankaj","8982300456","dublin city center");
//        userInfoList.put("pankaj",pankaj);
//
//        UserInfo shashank=new UserInfo("shashank","8982210466","dublin city center");
//        userInfoList.put("shashank",shashank);
//
//        UserInfo vishal=new UserInfo("vishal","8982210173","dublin castle");
//        userInfoList.put("vishal",vishal);
//
//        UserInfo gunjan=new UserInfo("gunjan","8982210581","dublin castle");
//        userInfoList.put("gunjan",gunjan);
//
//        UserInfo swara=new UserInfo("swara","8982167149","dublin city center");
//        userInfoList.put("swara",swara);
//    }
}
