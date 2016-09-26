package com.example.heekun.a36timer;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by heekun on 2016/09/17.
 */
public class DateUtil {

    MainActivity main = new MainActivity();  //MainActivityをインスタンス化


    public String getNowDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR); // 年
        int month = cal.get(Calendar.MONTH); // 月
        final int day = cal.get(Calendar.DAY_OF_MONTH); // 日
        //現在の日時情報をYYYY/MM/DD形式で返却
        return year + "/" + (month + 1) + "/" + day;
    }

    public String changeDate(String date, Boolean flg) {
        String str = changeDate(date, flg, 1);
        return str;
    }

    public String changeDate(String date, Boolean flg, Integer value) {
        // Format形式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        // Date型変換
        Date formatDate;
        try {
            formatDate = sdf.parse(date);
        } catch (ParseException e) {
            main.toastMake("日付は次の形式で入力してください：YYYY/MM/DD");
            e.printStackTrace();
            return "";
        }
        Calendar now = Calendar.getInstance();  ////Calendarクラスのインスタンスを生成
        now.setTime(formatDate);    ////Date型をCalendar型に変換
        if (flg) {
            now.add(Calendar.DAY_OF_MONTH, +value);  //日付を＋１
        } else {
            now.add(Calendar.DAY_OF_MONTH, -value);  //日付を－１
        }

        String ret = now.get(Calendar.YEAR) + "/" + (now.get(Calendar.MONTH) + 1) + "/" + now.get(Calendar.DAY_OF_MONTH);  //計算した日付を日付ボックスにセット
        return ret;
    }

    public Date FormatStringToDate(String str) {
        // Format形式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        try {
            date = sdf.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //*
        // ParsePosition position = new ParsePosition(0);
        //Date date = sdf.parse(str,position);
        //ParseExceptionのエラー位置が返る（正常終了時はー１が返る）
        //if (position.getErrorIndex() != -1) {
        //    System.out.println("[FormatStringToDate][ParseException]" + position.getErrorIndex());
        //}*/
        return date;
    }

    public String FormatDateToString(Date date) {
        Calendar cal = Calendar.getInstance();  ////Calendarクラスのインスタンスを生成
        cal.setTime(date);    ////Date型をCalendar型に変換

        int year = cal.get(Calendar.YEAR); // 年
        int month = cal.get(Calendar.MONTH) + 1; // 月（定義域が0～11なので1を足す）
        final int day = cal.get(Calendar.DAY_OF_MONTH); // 日

        String ret = Integer.toString(year) + "/" + Integer.toString(month)
                + "/" + Integer.toString(day);
        return ret;
    }

}
