package com.example.metro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import java.util.List;

public class MainActivity4 extends AppCompatActivity {
    private ImageButton continuer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        continuer = findViewById(R.id.continuer);
        continuer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent WelcomActivity= new Intent(getApplicationContext(), com.example.metro.authentis.UI.WelcomActivity.class);
                startActivity(WelcomActivity);
                finish();
            }
        });
    }



}