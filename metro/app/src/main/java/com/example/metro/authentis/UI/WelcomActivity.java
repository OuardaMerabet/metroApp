package com.example.metro.authentis.UI;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.metro.R;
import com.example.metro.authentis.UI.SimpleGestureFilter.SimpleGestureListener;


public class WelcomActivity extends AppCompatActivity implements  SimpleGestureListener {

    private SimpleGestureFilter detector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_welcom);
        // Detect touched area
        detector = new SimpleGestureFilter(WelcomActivity.this, this);
        //Start Button
        ImageButton Commencer=findViewById(R.id.commencer_bouton);
        Commencer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Login_Activity= new Intent(getApplicationContext(),choixID_Activity.class);
                startActivity(Login_Activity);
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent me) {
        // Call onTouchEvent of SimpleGestureFilter class
        this.detector.onTouchEvent(me);
        return super.dispatchTouchEvent(me);
    }

    @Override
    public void onSwipe(int direction) {
        //Detect the swipe gestures and display toast
        String showToastMessage = "";

        switch (direction) {

            case SimpleGestureFilter.SWIPE_RIGHT:
                break;
            case SimpleGestureFilter.SWIPE_LEFT:
                break;
            case SimpleGestureFilter.SWIPE_DOWN:
                showToastMessage = "Glisser vers le haut pour quitter l'application";
                Toast.makeText(this, showToastMessage, Toast.LENGTH_SHORT).show();
                break;
            case SimpleGestureFilter.SWIPE_UP:
                showCustomDialog();
                break;
        }
    }

    //Sortir de l'application avec double click
    @Override
    public void onDoubleTap() {
      showCustomDialog();
    }

    public  void  ClickSortir (View view){
        // sortir
        finish();
    }
    void showCustomDialog (){
        final Dialog dialog = new Dialog( WelcomActivity.this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // disable the default title
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // the user will be able to cancel the dialog by clicking anywhere outside the dialog
        dialog.setCancelable(true);
        // initialization
        dialog.setContentView(R.layout.sortirapp);
        ImageButton buton_confirmer= findViewById(R.id.ConfirmSortir);
        dialog.show();
    }
}
