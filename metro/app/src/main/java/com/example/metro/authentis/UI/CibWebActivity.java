package com.example.metro.authentis.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.metro.R;
import com.example.metro.authentis.BaseActivity;

import java.util.Calendar;

public class CibWebActivity extends BaseActivity {
    private Button dateDBButton,purchase,sendButton,Terminer;
    private DatePickerDialog dateDBPickerDialog;
    private LinearLayout paiementInformations,QRCode;
    private RelativeLayout codeVerification,fin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cib_web);

        initDatePicker();
        dateDBButton = findViewById(R.id.dateDBpicker);
        dateDBButton.setText(getDateDB());
        paiementInformations = findViewById(R.id.cardInformations);
        codeVerification = findViewById(R.id.codeVerification);
        QRCode = findViewById(R.id.QRCode);
        sendButton = findViewById(R.id.sendButton);
        fin=findViewById(R.id.fin);
        Terminer = findViewById(R.id.Terminer);
        purchase= findViewById(R.id.purchase);
        purchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paiementInformations.setVisibility(View.GONE);
                fin.setVisibility(View.GONE);
                QRCode.setVisibility(View.GONE);
                codeVerification.setVisibility(View.VISIBLE);
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paiementInformations.setVisibility(View.GONE);
                fin.setVisibility(View.VISIBLE);
                QRCode.setVisibility(View.GONE);
                codeVerification.setVisibility(View.GONE);
            }
        });
        Terminer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paiementInformations.setVisibility(View.GONE);
                fin.setVisibility(View.GONE);
                QRCode.setVisibility(View.VISIBLE);
                codeVerification.setVisibility(View.GONE);
            }
        });
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateDBSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String date = makeDateString(dayOfMonth,month,year);
                dateDBButton.setText(date);
            }
        };
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        dateDBPickerDialog = new DatePickerDialog(this, style, dateDBSetListener,year,month,dayOfMonth);

    }

    public void openDateDBPicker(View view) {
        dateDBPickerDialog.show();
    }

    public void Clickback(View view) {
        finish();
    }

}