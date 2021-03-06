package com.example.heekun.a36timer;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.Button;
import android.widget.DatePicker;

import java.util.Calendar;

//DatePickerDialog
public class DatePickerDialogFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),this, year, month, dayOfMonth);

        return datePickerDialog;
    }

    //@Override
    //public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

    //}

    //日付が選択されたときの処理
    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        MainActivity mainActivity = new MainActivity();
        mainActivity.setDateToButton(year,monthOfYear,dayOfMonth);

        //Button dateButton = (Button)findViewById(R.id.date_button);
        //Class<MainActivity> mainActivityClass = MainActivity.class;
        //Button dateButton = (Button)mainActivityClass.cast("Button").findViewById(R.id.date_button);
        //dateButton.setText(monthOfYear + "/" + dayOfMonth);
    }

}