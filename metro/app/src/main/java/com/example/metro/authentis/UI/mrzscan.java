package com.example.metro.authentis.UI;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.metro.R;
import com.example.metro.authentis.BaseActivity;
import com.example.metro.authentis.camera.CameraSource;
import com.example.metro.authentis.camera.CameraSourcePreview;
import com.example.metro.authentis.model.DocType;
import com.example.metro.authentis.other.GraphicOverlay;
import com.example.metro.authentis.text.TextRecognitionProcessor;

import org.jmrtd.lds.icao.MRZInfo;

import java.io.IOException;

public class mrzscan extends BaseActivity implements SimpleGestureFilter.SimpleGestureListener, TextRecognitionProcessor.ResultListener {
    private SimpleGestureFilter detector;
    public static final String DOC_TYPE = "DOC_TYPE";
    public static final String MRZ_RESULT = "MRZ_RESULT";
    private CameraSource cameraSource = null;
    private CameraSourcePreview preview;
    private GraphicOverlay graphicOverlay;
    private DocType docType = DocType.DZ;
    private static String TAG = mrzscan.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mrzscan);
        // Detect touched area
        detector = new SimpleGestureFilter(this, this);


        if(getIntent().hasExtra(DOC_TYPE)) {
            docType = (DocType) getIntent().getSerializableExtra(DOC_TYPE);
            if(docType == DocType.PASSPORT) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }
        }

        preview = findViewById(R.id.camera_source_preview);
        if (preview == null) {
            Log.d(TAG, "Preview is null");
        }
        graphicOverlay = findViewById(R.id.graphics_overlay);
        if (graphicOverlay == null) {
            Log.d(TAG, "graphicOverlay is null");
        }

        createCameraSource();
        startCameraSource();

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        startCameraSource();
    }

    /** Stops the camera. */
    @Override
    protected void onPause() {
        super.onPause();
        preview.stop();
    }


    private void createCameraSource() {

        if (cameraSource == null) {
            cameraSource = new CameraSource(this, graphicOverlay);
            cameraSource.setFacing(CameraSource.CAMERA_FACING_BACK);
        }
        cameraSource.setMachineLearningFrameProcessor(new TextRecognitionProcessor(docType, mrzscan.this));
    }

    private void startCameraSource() {
        if (cameraSource != null) {
            try {
                if (preview == null) {
                    Log.d(TAG, "resume: Preview is null");
                }
                if (graphicOverlay == null) {
                    Log.d(TAG, "resume: graphOverlay is null");
                }
                preview.start(cameraSource, graphicOverlay);
            } catch (IOException e) {
                Log.e(TAG, "Unable to start camera source.", e);
                cameraSource.release();
                cameraSource = null;
            }
        }
    }
    // Back button
    public void ClickbackIDselect (View view){
        // back button
        finish();
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

    @Override
    public void onDoubleTap() {
        redirectActivity(this,choixID_Activity.class);
    }

    public  void  ClickSortir (View view){
        // sortir
        finish();
    }
    void showCustomDialog (){
        final Dialog dialog = new Dialog( this);
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

    @Override
    public void onSuccess(MRZInfo mrzInfo) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(MRZ_RESULT,mrzInfo);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    @Override
    public void onError(Exception exp) {
        setResult(Activity.RESULT_CANCELED);
        Toast.makeText(getApplicationContext(), "error, Try again", Toast.LENGTH_SHORT).show();
        finish();
    }

}