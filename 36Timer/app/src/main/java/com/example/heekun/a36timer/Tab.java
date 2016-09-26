package com.example.heekun.a36timer;

/**
 * Created by heekun on 2016/09/23.
 */
public class Tab {
    public int number;
    public String title;

    public  void setNumber(int number){
        if (number == 1 || number == 2 || number == 3){
            this.number = number;
        } else {
            return ;
        }
    }
    public void setTitle(String title){
        //TODO:１～１２までの整数のみを受け取るようにしたい
        this.title = title;
    }
    public int getNumber(){
        return number;
    }
    public String getTitle(){
        return title;
    }
}
