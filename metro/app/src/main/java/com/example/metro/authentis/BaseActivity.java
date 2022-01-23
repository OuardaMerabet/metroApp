package com.example.metro.authentis;

import android.app.Activity;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class BaseActivity extends AppCompatActivity {

    protected void redirectActivity(Activity activity, Class aClass) {
        // Initalize intent
        Intent intent = new Intent(activity,aClass);
        // Set flag
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //start activity
        activity.startActivity(intent);
    }
    public static String makeDateString(int dayOfMonth, int month, int year) {
        Calendar call = Calendar.getInstance();
        call.set(Calendar.YEAR, year);
        call.set(Calendar.MONTH, month);
        call.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String FORMAT_API_DATETIME = "dd/MM/yyy";
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_API_DATETIME, Locale.getDefault());
        String mDateTime = sdf.format(call.getTime());
        return  mDateTime;
        //return  dayOfMonth + "/" + frenchMonths[month]  +"/" + year;
    }


    public static String getDateFN() {
        Calendar calFN = Calendar.getInstance();
        int year = calFN.get(Calendar.YEAR);
        int month = calFN.get(Calendar.MONTH);
        int dayOfMonth = calFN.get(Calendar.DAY_OF_MONTH);
        return  makeDateString(dayOfMonth,month,year);
    }

    public static String getDateDB() {
        Calendar calDB = Calendar.getInstance();
        int year = calDB.get(Calendar.YEAR);
        int month = calDB.get(Calendar.MONTH);
        int dayOfMonth = calDB.get(Calendar.DAY_OF_MONTH);
        return  makeDateString(dayOfMonth,month,year);
    }
}
