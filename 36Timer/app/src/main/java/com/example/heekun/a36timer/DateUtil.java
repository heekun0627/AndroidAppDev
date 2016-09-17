package com.example.heekun.a36timer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
            formatDate = (Date) sdf.parse(date);
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

}
