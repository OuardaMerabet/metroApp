package com.example.metro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private Button service;
    private Button metro;
    private Button tram;
    private Button telepherique;
    private Button wilaya;
    private Button alger;
    private Button constantine;
    private Button oran;
    private Button sidi;
    private Button ouargla;
    private Button setif;
    private ImageButton button_continuer;
    private ImageView point1;
    private ImageView point2;
    private ImageView point3;
    private ImageView point4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        service=(Button) findViewById(R.id.service);
        metro=(Button) findViewById(R.id.metro);
        tram=(Button) findViewById(R.id.tram);
        telepherique=(Button) findViewById(R.id.telepherique);
        wilaya=(Button) findViewById(R.id.wilaya);
        alger=(Button) findViewById(R.id.alger);
        constantine=(Button) findViewById(R.id.constantine);
        oran=(Button) findViewById(R.id.oran);
        sidi=(Button) findViewById(R.id.sidi);
        ouargla=(Button) findViewById(R.id. ouargla);
        setif=(Button) findViewById(R.id.setif);
        point1=(ImageView) findViewById(R.id.point1);
        point2=(ImageView) findViewById(R.id.point2);
        point3=(ImageView) findViewById(R.id.point3);
        point4=(ImageView) findViewById(R.id.point4);
        button_continuer=(ImageButton) findViewById(R.id.button_continuer);

        metro.setVisibility(View.GONE);
        tram.setVisibility(View.GONE);
        telepherique.setVisibility(View.GONE);
        alger.setVisibility(View.GONE);
        constantine.setVisibility(View.GONE);
        oran.setVisibility(View.GONE);
        sidi.setVisibility(View.GONE);
        ouargla.setVisibility(View.GONE);
        setif.setVisibility(View.GONE);
        point3.setVisibility(View.GONE);
        point4.setVisibility(View.GONE);

        service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                metro.setVisibility(View.VISIBLE);
                tram.setVisibility(View.VISIBLE);
                telepherique.setVisibility(View.VISIBLE);
                alger.setVisibility(View.GONE);
                constantine.setVisibility(View.GONE);
                oran.setVisibility(View.GONE);
                sidi.setVisibility(View.GONE);
                ouargla.setVisibility(View.GONE);
                setif.setVisibility(View.GONE);
                point2.setVisibility(View.VISIBLE);
                point1.setVisibility(View.GONE);
                point3.setVisibility(View.VISIBLE);
                point4.setVisibility(View.GONE);

                metro.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        metro.setBackgroundDrawable(getResources().getDrawable(R.drawable.selection));
                        tram.setBackgroundDrawable(getResources().getDrawable(R.color.transparent));
                        telepherique.setBackgroundDrawable(getResources().getDrawable(R.color.transparent));

                        metro.setTextColor(getResources().getColor(R.color.black));
                        tram.setTextColor(getResources().getColor(R.color.gris));
                        telepherique.setTextColor(getResources().getColor(R.color.gris));

                        ;}
                });

                tram.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        tram.setBackgroundDrawable(getResources().getDrawable(R.drawable.selection));
                        metro.setBackgroundDrawable(getResources().getDrawable(R.color.transparent));
                        telepherique.setBackgroundDrawable(getResources().getDrawable(R.color.transparent));

                        metro.setTextColor(getResources().getColor(R.color.gris));
                        tram.setTextColor(getResources().getColor(R.color.black));
                        telepherique.setTextColor(getResources().getColor(R.color.gris));

                        ;}
                });

                telepherique.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        telepherique.setBackgroundDrawable(getResources().getDrawable(R.drawable.selection));
                        tram.setBackgroundDrawable(getResources().getDrawable(R.color.transparent));
                        metro.setBackgroundDrawable(getResources().getDrawable(R.color.transparent));

                        metro.setTextColor(getResources().getColor(R.color.gris));
                        tram.setTextColor(getResources().getColor(R.color.gris));
                        telepherique.setTextColor(getResources().getColor(R.color.black));

                        ;}
                });

            }
        });


        wilaya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                alger.setVisibility(View.VISIBLE);
                constantine.setVisibility(View.VISIBLE);
                oran.setVisibility(View.VISIBLE);
                sidi.setVisibility(View.VISIBLE);
                ouargla.setVisibility(View.VISIBLE);
                setif.setVisibility(View.VISIBLE);
                metro.setVisibility(View.GONE);
                tram.setVisibility(View.GONE);
                telepherique.setVisibility(View.GONE);
                point2.setVisibility(View.GONE);
                point1.setVisibility(View.VISIBLE);
                point3.setVisibility(View.GONE);
                point4.setVisibility(View.VISIBLE);

                alger.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        alger.setBackgroundDrawable(getResources().getDrawable(R.drawable.selection));
                        constantine.setBackgroundDrawable(getResources().getDrawable(R.color.transparent));
                        oran.setBackgroundDrawable(getResources().getDrawable(R.color.transparent));
                        sidi.setBackgroundDrawable(getResources().getDrawable(R.color.transparent));
                        ouargla.setBackgroundDrawable(getResources().getDrawable(R.color.transparent));
                        setif.setBackgroundDrawable(getResources().getDrawable(R.color.transparent));

                        alger.setTextColor(getResources().getColor(R.color.black));
                        constantine.setTextColor(getResources().getColor(R.color.gris));
                        oran.setTextColor(getResources().getColor(R.color.gris));
                        sidi.setTextColor(getResources().getColor(R.color.gris));
                        ouargla.setTextColor(getResources().getColor(R.color.gris));
                        setif.setTextColor(getResources().getColor(R.color.gris));
                        ;}
                });
                constantine.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        alger.setBackgroundDrawable(getResources().getDrawable(R.color.transparent));
                        constantine.setBackgroundDrawable(getResources().getDrawable(R.drawable.selection));
                        oran.setBackgroundDrawable(getResources().getDrawable(R.color.transparent));
                        sidi.setBackgroundDrawable(getResources().getDrawable(R.color.transparent));
                        ouargla.setBackgroundDrawable(getResources().getDrawable(R.color.transparent));
                        setif.setBackgroundDrawable(getResources().getDrawable(R.color.transparent));

                        alger.setTextColor(getResources().getColor(R.color.gris));
                        constantine.setTextColor(getResources().getColor(R.color.black));
                        oran.setTextColor(getResources().getColor(R.color.gris));
                        sidi.setTextColor(getResources().getColor(R.color.gris));
                        ouargla.setTextColor(getResources().getColor(R.color.gris));
                        setif.setTextColor(getResources().getColor(R.color.gris));
                        ;}
                });

                oran.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        alger.setBackgroundDrawable(getResources().getDrawable(R.color.transparent));
                        constantine.setBackgroundDrawable(getResources().getDrawable(R.color.transparent));
                        oran.setBackgroundDrawable(getResources().getDrawable(R.drawable.selection));
                        sidi.setBackgroundDrawable(getResources().getDrawable(R.color.transparent));
                        ouargla.setBackgroundDrawable(getResources().getDrawable(R.color.transparent));
                        setif.setBackgroundDrawable(getResources().getDrawable(R.color.transparent));

                        alger.setTextColor(getResources().getColor(R.color.gris));
                        constantine.setTextColor(getResources().getColor(R.color.gris));
                        oran.setTextColor(getResources().getColor(R.color.black));
                        sidi.setTextColor(getResources().getColor(R.color.gris));
                        ouargla.setTextColor(getResources().getColor(R.color.gris));
                        setif.setTextColor(getResources().getColor(R.color.gris));
                        ;}
                });

                sidi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        alger.setBackgroundDrawable(getResources().getDrawable(R.color.transparent));
                        constantine.setBackgroundDrawable(getResources().getDrawable(R.color.transparent));
                        oran.setBackgroundDrawable(getResources().getDrawable(R.color.transparent));
                        sidi.setBackgroundDrawable(getResources().getDrawable(R.drawable.selection));
                        ouargla.setBackgroundDrawable(getResources().getDrawable(R.color.transparent));
                        setif.setBackgroundDrawable(getResources().getDrawable(R.color.transparent));

                        alger.setTextColor(getResources().getColor(R.color.gris));
                        constantine.setTextColor(getResources().getColor(R.color.gris));
                        oran.setTextColor(getResources().getColor(R.color.gris));
                        sidi.setTextColor(getResources().getColor(R.color.black));
                        ouargla.setTextColor(getResources().getColor(R.color.gris));
                        setif.setTextColor(getResources().getColor(R.color.gris));
                        ;}
                });

                ouargla.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        alger.setBackgroundDrawable(getResources().getDrawable(R.color.transparent));
                        constantine.setBackgroundDrawable(getResources().getDrawable(R.color.transparent));
                        oran.setBackgroundDrawable(getResources().getDrawable(R.color.transparent));
                        sidi.setBackgroundDrawable(getResources().getDrawable(R.color.transparent));
                        ouargla.setBackgroundDrawable(getResources().getDrawable(R.drawable.selection));
                        setif.setBackgroundDrawable(getResources().getDrawable(R.color.transparent));

                        alger.setTextColor(getResources().getColor(R.color.gris));
                        constantine.setTextColor(getResources().getColor(R.color.gris));
                        oran.setTextColor(getResources().getColor(R.color.gris));
                        sidi.setTextColor(getResources().getColor(R.color.gris));
                        ouargla.setTextColor(getResources().getColor(R.color.black));
                        setif.setTextColor(getResources().getColor(R.color.gris));
                        ;}
                });

                setif.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        alger.setBackgroundDrawable(getResources().getDrawable(R.color.transparent));
                        constantine.setBackgroundDrawable(getResources().getDrawable(R.color.transparent));
                        oran.setBackgroundDrawable(getResources().getDrawable(R.color.transparent));
                        sidi.setBackgroundDrawable(getResources().getDrawable(R.color.transparent));
                        ouargla.setBackgroundDrawable(getResources().getDrawable(R.color.transparent));
                        setif.setBackgroundDrawable(getResources().getDrawable(R.drawable.selection));

                        alger.setTextColor(getResources().getColor(R.color.gris));
                        constantine.setTextColor(getResources().getColor(R.color.gris));
                        oran.setTextColor(getResources().getColor(R.color.gris));
                        sidi.setTextColor(getResources().getColor(R.color.gris));
                        ouargla.setTextColor(getResources().getColor(R.color.gris));
                        setif.setTextColor(getResources().getColor(R.color.black));

                        ;}
                });
            }
        });

        button_continuer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent appel = new Intent(MainActivity.this, MainActivity3.class);
                startActivity(appel);

                ;}
        });


}}