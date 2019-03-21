package org.cs7cs3.team7.journeysharing;

import com.google.gson.Gson;

import java.util.Date;

public class JSONfyModelTest {
    public static void main(String[] args) {
        Schedule schedule = new Schedule();
        schedule.setStartPosition("tcd");
        schedule.setEndPosition("ucd");
        schedule.setUserId(12);
        Date date = new Date();
        Gson gson = new Gson();
        schedule.setWhen(date);
        String res = gson.toJson(schedule, Schedule.class);
        System.out.print(res);
    }
}
