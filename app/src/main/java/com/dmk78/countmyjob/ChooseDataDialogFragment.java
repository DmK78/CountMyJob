package com.dmk78.countmyjob;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ChooseDataDialogFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    private String day;
    private String month;
    private String year;




    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            day = getArguments().getString(ReportsFragment.DAY);
            month = getArguments().getString(ReportsFragment.MONTH);
            year = getArguments().getString(ReportsFragment.YEAR);
        }

        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }



    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        TextView textViewDateTime = (TextView) getActivity().findViewById(R.id.textViewAjChooseDate);
        TextView textViewData1 = (TextView) getActivity().findViewById(R.id.textViewRepData1);
        TextView textViewData2 = (TextView) getActivity().findViewById(R.id.textViewRepData2);
        // Create a Date variable/object with user chosen date
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(0);
        cal.set(year, month, day, 0, 0, 0);
        Date chosenDate = cal.getTime();
        SimpleDateFormat output = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = output.format(chosenDate);
        if (textViewDateTime != null) textViewDateTime.setText(formattedDate);
        if(getTag().equals("date1")){textViewData1.setText(formattedDate);}
        if(getTag().equals("date2")){textViewData2.setText(formattedDate);}

        //if (textViewData1 != null) textViewData1.setText(formattedDate);
        //if (textViewData2 != null) textViewData2.setText(formattedDate);
    }



}
