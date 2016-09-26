package com.example.heekun.a36timer;

import java.util.Date;

/**
 * Created by heekun on 2016/09/20.
 */
public class Record {
    private  Date date ;
    private String time;

    public void setDate(Date date) {
        this.date = date;
    }
    public  void setTime (String time) {
        this.time = time;
    }


    public Date getDate() {
        return date;
    }
    public  String getTime () {
        return time;
    }

}
