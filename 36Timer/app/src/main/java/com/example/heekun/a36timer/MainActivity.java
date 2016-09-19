package com.example.heekun.a36timer;

import android.graphics.Typeface;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

import static com.example.heekun.a36timer.R.id.date_button;
import static com.example.heekun.a36timer.R.id.date_text_1;


public class MainActivity extends AppCompatActivity {

    //コンストラクタ
    public MainActivity() {
    }

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
        //残り時間テキストボックス
        final TextView limitTimeBox;
        limitTimeBox = (TextView) findViewById(R.id.limit_time);
        limitTimeBox.setTypeface(Typeface.SANS_SERIF,Typeface.BOLD);  //使用フォントの変更
        //入力日付テキストボックス
        final EditText dateBox1;
        dateBox1 = (EditText) findViewById(R.id.date_text_1);
        //リストビュー
        final ListView listView1 = (ListView) findViewById(R.id.listView1);
        final ArrayList<String> items = new ArrayList<String>();

        //残業時間スピナー
        final Spinner hourSpinner;  //"時"入力部スピナー
        hourSpinner = (Spinner)findViewById(R.id.hour_spinner);
        final Spinner minutesSpinner ;  //"分"入力部スピナー
        minutesSpinner = (Spinner)findViewById(R.id.minutes_spinner);

        //日付の初期値のセット
        DateUtil dateUtil = new DateUtil();
        String now = dateUtil.getNowDate();
        //日付ボックスへ初期値をセット
        dateBox1.setText(now);

        //plusボタン（FloatingActionButton）
        final FloatingActionButton fab ;
        fab = (FloatingActionButton)findViewById(R.id.fab_plus);
        fab.show();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculationButton.callOnClick();  //[計算]ボタンを押したことと同様にする
            }
        });

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
                //入力された残業時間をスピナーより取得
                String inHourStr = hourSpinner.getSelectedItem().toString();
                String inMinutesStr = minutesSpinner.getSelectedItem().toString();
                String setTimeStr = inHourStr + "時間" + inMinutesStr + "分";
                //入力された日付をスピナーより取得
                String inDateStr = dateBox1.getText().toString();
                if (inDateStr.equals("")) {
                    toastMake("残業日付を入力してください");
                    return;
                }
                //入力日付がすでにリストに存在しないか確認
                EditText dateText = (EditText) findViewById(R.id.date_text_1);
                String inDate = dateText.getText().toString();
                ArrayList<String> dateList = new ArrayList<String>();
                dateList = getDateListByListView();  //リスト入力済みの日付を配列に格納
                for(String date : dateList) {
                    if (inDate.equals(date)) {
                        toastMake("その日付は既に入力されています");
                        return;
                    }
                }
                //リストビューにデータを追加
                //items.add("時間：" + inTimeStr + "　日付：" + inDateStr);
                items.add(inDateStr +  "　" + setTimeStr);
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
        dateButton = (Button) findViewById(date_button);
        dateButton.setOnClickListener(new View.OnClickListener() {
            // ボタンがクリックされた時のハンドラ
            @Override
            public void onClick(View v) {
                //DatePickerダイアログを表示
                DatePickerDialogFragment datePicker = new DatePickerDialogFragment();
                datePicker.show(getSupportFragmentManager(), "datePicker");
            }
        });

        //----------------------------------------
        //  リスト項目が長押しされた時の処理
        //----------------------------------------
        listView1.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                ListView listView = (ListView) parent;
                String item = (String) listView.getItemAtPosition(position);  //選択項目の取得
                ArrayAdapter<String> adapter = (ArrayAdapter<String>)listView.getAdapter();  //リストビューのアダプターを取得
                adapter.remove(item);  //長押しされた項目の削除
                return false;
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
        //String setStr = Integer.toString(year) + "/" + Integer.toString(month) + "/" + Integer.toString(day);
        String setStr = "YYYY/MM/DD";

        EditText dateText = (EditText) findViewById(date_text_1);
        dateText.setText(setStr);
        //日付ボタンをインスタンス化
        //Button dateButton = (Button)findViewById(R.id.date_button);
        //日付ボタンに文字列をセット
        //dateButton.setText(setStr);
    }
    public Button getDateButton(){
        Button dateButton = (Button)findViewById(date_text_1);
        return dateButton;
    }

    /**
     * calculation 残り残業時間を計算する
     * @param time 今日の残業時間
     * @return int 残り残業時間
     */
    public int calculation(int time) {
        int limit = 155;
        int result;
        result = (limit * 60 - time) / 60;
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
            String[] tmp = item.replace("分","").split("　");
            String[] timeArray = tmp[1].split("時間");
            String timeStr = conversionToMinutes(timeArray[0],timeArray[1]);
            timeList.add(Integer.parseInt(timeStr));    //リストビューの要素の時間を分に換算して数値型でadd
        }
        return timeList;    //リストビューの要素の時間（分形式）が詰まったArrayListを返却
    }

    public ArrayList<String> getDateListByListView() {
        ArrayList<String> dateList = new ArrayList<String>();  //返却するリスト

        ListView listView = (ListView)findViewById(R.id.listView1);
        //リストビューのサイズを取得
        int listCount = listView.getCount();
        for(int i = 0 ; i < listCount ; i++) {
            String item = (String) listView.getItemAtPosition(i);
            String[] tmp = item.split("　");
            String date = tmp[0];
            dateList.add(date);
        }
        return dateList;
    }

    //時間を分に換算
    public String conversionToMinutes(String hour , String minutes) {
        int hourInt = Integer.parseInt(hour);
        int minutesInt = Integer.parseInt(minutes);
        int sum = hourInt * 60 + minutesInt;
        return Integer.toString(sum);  //分換算した時間を返却
    }
    //分を時間に換算（"hour時minutes分"形式）
    public String conversionToHour(String minutes) {
        int minutesInt = Integer.parseInt(minutes);
        int hourInt = minutesInt / 60;
        minutesInt = minutesInt - hourInt * 60;
        return Integer.toString(hourInt) +"時間" + Integer.toString(minutesInt) + "分";
    }
}
