package com.example.metro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MainActivity2 extends AppCompatActivity {
    private ImageButton titres;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        titres=(ImageButton) findViewById(R.id.titres);

        titres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent appel = new Intent(MainActivity2.this, MainActivity.class);
                startActivity(appel);

                ;}
        });


    }
}