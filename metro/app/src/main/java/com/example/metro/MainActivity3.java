package com.example.metro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.CompoundButton;
import android.widget.TextView;

public class MainActivity3 extends AppCompatActivity {
    public ImageButton button_continuer;
    public CheckBox check_icon_classic;
    public CheckBox check_classic;
    public CheckBox check_etudiant;
    public CheckBox check_senior;
    public CheckBox check_annuel_classic;
    public CheckBox check_annuel_etudiant;
    public TextView text1;
    public TextView text2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        button_continuer=(ImageButton) findViewById(R.id.button_continuer);
        check_icon_classic=(CheckBox)findViewById(R.id.check_icon_classic);
        check_classic=(CheckBox)findViewById(R.id.check_classic);
        check_etudiant=(CheckBox)findViewById(R.id.check_etudiant);
        check_senior=(CheckBox)findViewById(R.id.check_senior);
        check_annuel_classic=(CheckBox)findViewById(R.id.check_annuel_classic);
        check_annuel_etudiant=(CheckBox)findViewById(R.id.check_annuel_etudiant);
        text1=(TextView)findViewById(R.id.text1);
        text2=(TextView)findViewById(R.id.text2);

        button_continuer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent appel = new Intent(MainActivity3.this, MainActivity4.class);
                startActivity(appel);

                ;}
        });

        check_classic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                check_icon_classic.setChecked(isChecked);
                check_etudiant.setChecked(false);
                check_senior.setChecked(false);
                check_annuel_classic.setChecked(false);
                check_annuel_etudiant.setChecked(false);
                text1.setTextColor(getResources().getColor(R.color.black));
                text2.setTextColor(getResources().getColor(R.color.black));
            }
        });



    }
}