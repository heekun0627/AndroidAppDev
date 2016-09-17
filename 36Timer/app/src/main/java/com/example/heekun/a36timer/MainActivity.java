package com.example.heekun.a36timer;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.Gravity;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.lang.Exception;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.text.format.Time;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //----------------------------------
        //各種オブジェクトをインスタンス化
        //----------------------------------
        //計算ボタン
        final Button calculationButton;
        calculationButton = (Button) findViewById(R.id.calculation_button);
        //入力テキストボックス
        final EditText inputBox1;
        inputBox1 = (EditText) findViewById(R.id.edit_text_1);
        //出力テキストボックス
        final TextView limitTimeBox;
        limitTimeBox = (TextView) findViewById(R.id.limit_time);
        //入力日付テキストボックス
        final EditText dateBox1;
        dateBox1 = (EditText) findViewById(R.id.date_text_1);
        //リストビュー
        final ListView listView1 = (ListView) findViewById(R.id.listView1);

        final ArrayList<String> items = new ArrayList<String>();

        //日付の初期値のセット
        DateUtil dateUtil = new DateUtil();
        String now = dateUtil.getNowDate();
        //日付ボックスへ初期値をセット
        dateBox1.setText(now);

        //－/＋ボタンをインスタンス化
        final Button dateMinusButton = (Button) findViewById(R.id.date_minus_button);
        final Button datePlusButton = (Button) findViewById(R.id.date_plus_button);
        //--------------------------------------
        //日付－/＋ボタンが押された際の処理
        //--------------------------------------
        dateMinusButton.setOnClickListener(new View.OnClickListener() {
            // ボタンがクリックされた時のハンドラ
            @Override
            public void onClick(View v) {
                DateUtil dateUtil = new DateUtil();
                //日付ボックスに入力されている値を取得
                EditText dateBox = (EditText) findViewById(R.id.date_text_1);
                String dateStr = dateBox.getText().toString();
                String resultDate = dateUtil.changeDate(dateStr,false);  //変更する日付を取得
                dateBox.setText(resultDate);  //計算した日付を日付ボックスにセット
            }
        });
        datePlusButton.setOnClickListener(new View.OnClickListener() {
            // ボタンがクリックされた時のハンドラ
            @Override
            public void onClick(View v) {
                DateUtil dateUtil = new DateUtil();
                //日付ボックスに入力されている値を取得
                EditText dateBox = (EditText) findViewById(R.id.date_text_1);
                String dateStr = dateBox.getText().toString();
                String resultDate = dateUtil.changeDate(dateStr,true);  //変更する日付を取得
                dateBox.setText(resultDate);  //計算した日付を日付ボックスにセット
            }
        });



        //------------------------------
        //計算ボタンが押された際の処理
        //------------------------------
        calculationButton.setOnClickListener(new View.OnClickListener() {
            // ボタンがクリックされた時のハンドラ
            @Override
            public void onClick(View v) {
                //-------------------------
                //  入力内容の取得
                //-------------------------
                //残業時間
                String inTimeStr = inputBox1.getText().toString();
                if (inTimeStr.equals("")) {
                    toastMake("残業時間を入力してください");
                    return;
                }
                //日付
                String inDateStr = dateBox1.getText().toString();
                if (inDateStr.equals("")) {
                    toastMake("残業日付を入力してください");
                    return;
                }
                //リストビューにデータを追加
                items.add("時間：" + inTimeStr + "　日付：" + inDateStr);
                // Adapter - ArrayAdapter
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                        MainActivity.this,
                        R.layout.list_item,
                        items
                );
                listView1.setAdapter(adapter);

                //-------------------------------------------------------------
                //  リストビューの要素の時間リストを取得
                //    →リストビューに追加されている時間の合計値を算出
                //-------------------------------------------------------------
                ArrayList<Integer> timeList = getTimeListByListView();
                int listCount = timeList.size();
                int timeSum = 0;
                //時間リストのサイズが０であれば合計値を算出しない
                if (listCount != 0) {
                    for (int i = 0;i<listCount;i++){
                        timeSum = timeSum + timeList.get(i);
                    }
                }

                //返却値計算処理の呼び出し
                int result = calculation(timeSum);
                //計算結果を文字列型に変換（TextViewに格納できるよう）
                String resStr = Integer.toString(result);
                //出力ボックスへ格納（計算結果の返却）
                limitTimeBox.setText(resStr);
            }
        });


        //------------------------------
        //  日付ボタンが押された際の処理
        //------------------------------
        //日付ボタン
        final Button dateButton;
        dateButton = (Button) findViewById(R.id.date_button);
        dateButton.setOnClickListener(new View.OnClickListener() {
            // ボタンがクリックされた時のハンドラ
            @Override
            public void onClick(View v) {
                //DatePickerダイアログを表示
                DatePickerDialogFragment datePicker = new DatePickerDialogFragment();
                datePicker.show(getSupportFragmentManager(), "datePicker");
            }
        });
    }


    /**
     * 日付ボタンに日付を設定（YYYY/MM/DD形式）
     * @param year  設定する年
     * @param month 設定する月
     * @param day   設定する日
     */
    public void setDateToButton(int year, int month, int day) {
        //セットする文字列を生成
        String setStr = Integer.toString(year) + "/" + Integer.toString(month) + "/" + Integer.toString(day);
        toastMake(setStr, 100, 100);
        //日付ボタンをインスタンス化
        //Button dateButton = (Button)findViewById(R.id.date_button);
        //日付ボタンに文字列をセット
        //dateButton.setText(setStr);
    }


    /**
     * calculation 残り残業時間を計算する
     * @param time 今日の残業時間
     * @return int 残り残業時間
     */
    public int calculation(int time) {
        int limit = 155;
        int result;
        result = limit - time;
        return result;
    }


    //トーストを一旦表示する
    public void toastMake(String message) {
        toastMake(message, 0, 200);
    }

    /**
     * toastMake トースト表示処理
     * @param message トーストに表示する文字列
     * @param x       横軸位置
     * @param y       縦軸位置
     */
    public void toastMake(String message, int x, int y) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER | Gravity.CENTER, x, y);
        toast.show();
    }

    public ArrayList<Integer> getTimeListByListView() {
        //返却するリスト
        ArrayList<Integer> timeList = new ArrayList<Integer>();

        ListView listView = (ListView)findViewById(R.id.listView1);
        //リストビューのサイズを取得
        int listCount = listView.getCount();
        for(int i=0;i<listCount;i++){
            //View itemView = listView.getChildAt(i);
            String item = (String) listView.getItemAtPosition(i);
            String[] tmp = item.split("　");
            int time = (Integer)Integer.parseInt(tmp[0].replace("時間：",""));
            timeList.add(time);    //リストビューの要素の時間を数値型でadd
        }
        return timeList;    //リストビューの要素の時間が詰まったArrayListを返却
    }

    public ArrayList<String> getDateListByListView() {
        ArrayList<String> dateList = new ArrayList<String>();  //返却するリスト

        ListView listView = (ListView)findViewById(R.id.listView1);
        //リストビューのサイズを取得
        int listCount = listView.getCount();
        for(int i = 0 ; i < listCount ; i++) {
            String item = (String) listView.getItemAtPosition(i);
            String[] tmp = item.split("　");
            String date = (String)tmp[1].replace("日付：","");
            dateList.add(date);
        }
        return dateList;

    }
}
