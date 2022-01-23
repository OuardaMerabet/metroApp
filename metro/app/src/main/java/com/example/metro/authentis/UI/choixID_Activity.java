package com.example.metro.authentis.UI;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.metro.R;
import com.example.metro.authentis.BaseActivity;
import com.example.metro.authentis.UI.SimpleGestureFilter.SimpleGestureListener;
import com.example.metro.authentis.model.AdditionalPersonDetails;
import com.example.metro.authentis.model.DocType;
import com.example.metro.authentis.model.EDocument;
import com.example.metro.authentis.model.PersonDetails;
import com.example.metro.authentis.util.AppUtil;
import com.example.metro.authentis.util.DateUtil;
import com.example.metro.authentis.util.Image;
import com.example.metro.authentis.util.ImageUtil;
import com.example.metro.authentis.util.PermissionUtil;
import com.example.metro.authentis.util.SecurityUtil;
import com.example.metro.authentis.util.StringUtil;
import com.google.android.material.snackbar.Snackbar;

import net.sf.scuba.smartcards.CardFileInputStream;
import net.sf.scuba.smartcards.CardService;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.jmrtd.BACKey;
import org.jmrtd.BACKeySpec;
import org.jmrtd.PassportService;
import org.jmrtd.Util;
import org.jmrtd.lds.CardSecurityFile;
import org.jmrtd.lds.ChipAuthenticationInfo;
import org.jmrtd.lds.ChipAuthenticationPublicKeyInfo;
import org.jmrtd.lds.DisplayedImageInfo;
import org.jmrtd.lds.PACEInfo;
import org.jmrtd.lds.SODFile;
import org.jmrtd.lds.SecurityInfo;
import org.jmrtd.lds.icao.DG11File;
import org.jmrtd.lds.icao.DG12File;
import org.jmrtd.lds.icao.DG14File;
import org.jmrtd.lds.icao.DG15File;
import org.jmrtd.lds.icao.DG1File;
import org.jmrtd.lds.icao.DG2File;
import org.jmrtd.lds.icao.DG3File;
import org.jmrtd.lds.icao.DG5File;
import org.jmrtd.lds.icao.DG7File;
import org.jmrtd.lds.icao.MRZInfo;
import org.jmrtd.lds.iso19794.FaceImageInfo;
import org.jmrtd.lds.iso19794.FaceInfo;
import org.jmrtd.lds.iso19794.FingerImageInfo;
import org.jmrtd.lds.iso19794.FingerInfo;
import org.jmrtd.protocol.AAResult;
import org.jmrtd.protocol.EACCAResult;

import java.security.MessageDigest;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.Signature;
import java.security.cert.X509Certificate;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.PSSParameterSpec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static com.example.metro.authentis.UI.mrzscan.DOC_TYPE;
import static com.example.metro.authentis.UI.mrzscan.MRZ_RESULT;
import static org.jmrtd.PassportService.DEFAULT_MAX_BLOCKSIZE;
import static org.jmrtd.PassportService.NORMAL_MAX_TRANCEIVE_LENGTH;


public class choixID_Activity extends BaseActivity implements  SimpleGestureListener, View.OnClickListener {

    private static final String TAG = choixID_Activity.class.getSimpleName();
    private SimpleGestureFilter detector;
    private LinearLayout logout;
    public boolean signin;
    private NfcAdapter adapter;
    private static final int APP_CAMERA_ACTIVITY_REQUEST_CODE = 150;
    private static final int APP_SETTINGS_ACTIVITY_REQUEST_CODE = 550;
    private static final int IMAGE_CAPTURE_CODE = 1001;
    private String passportNumber, expirationDate , birthDate;
    private ImageView ivPhoto, selfie_photo1,selfie_photo2;
    private  ImageView output_signature;
    private TextView nameFirstname,output_last_name,output_first_name,output_gender,output_nationality,output_birthDate,output_birthPlace,IDdoc,output_state,output_expirationDate,output_groupeSinguin;
    private RelativeLayout startNFC;
    private  LinearLayout formatDoc;
    private RelativeLayout NFCscan,Selfie, Selfie2;
    private  LinearLayout results,output_groupeSinguinField;
    DrawerLayout drawerLayout;
    private ImageButton button_annuler,Next,Reconnaitre_person;
    private  CheckBox check_card, check_passeport, check_permis;
    private DocType docType;
    private ProgressBar progressbar;
    private Uri image_uri;
    private Bitmap photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choix_i_d_);


        formatDoc = findViewById(R.id.formatDoc);
        startNFC = findViewById(R.id.startNFC);
        drawerLayout=findViewById(R.id.drawer_layout);
        NFCscan = findViewById(R.id.nfcScan);
        results= findViewById(R.id.results);
        button_annuler= findViewById(R.id.button_annuler);
        button_annuler.setOnClickListener(this);
        progressbar = findViewById(R.id.progressbar);
        Reconnaitre_person = findViewById(R.id.Reconnaitre_person);
        Reconnaitre_person.setOnClickListener(this);
        Next = findViewById(R.id.Next);
        Next.setOnClickListener(this);
        Selfie = findViewById(R.id.Selfie);

        output_groupeSinguinField =findViewById(R.id.output_groupeSinguinField);
        // Detect touched area
        detector = new SimpleGestureFilter(this, this);

        // To ensure that only one doc ID is selected at a time
        check_card = findViewById(R.id.check_card);
        check_passeport = findViewById(R.id.check_passeport);
        check_permis = findViewById(R.id.check_permis);
        check_card.setOnClickListener(this);
        check_permis.setOnClickListener(this);
        check_passeport.setOnClickListener(this);

        logout=findViewById(R.id.Logout);
        logout.setOnClickListener(this);

        ImageButton selectdocument_button = findViewById(R.id.selectdocument_button);
        selectdocument_button.setOnClickListener(this);

        //results
        nameFirstname = findViewById(R.id.nameFirstname);
        output_last_name = findViewById(R.id.output_last_name);
        output_first_name = findViewById(R.id.output_first_name);
        output_gender = findViewById(R.id.output_gender);
        output_nationality = findViewById(R.id.output_nationality);
        output_birthDate = findViewById(R.id.output_birthDate);
        IDdoc = findViewById(R.id.IDdoc);
        output_state = findViewById(R.id.output_state);
        output_expirationDate = findViewById(R.id.output_expirationDate);
        output_signature = findViewById(R.id.output_signature);
        ivPhoto = findViewById(R.id.view_photo);
        selfie_photo1 = findViewById(R.id.selfie_photo1);
        selfie_photo2 = findViewById(R.id.selfie_photo2);
        output_birthPlace= findViewById(R.id.output_birthPlace);
        output_groupeSinguin= findViewById(R.id.output_groupeSinguin);
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.check_card:
                if ( check_card.isChecked())
                {
                    check_card.setChecked(true);
                    check_passeport.setChecked(false);
                    check_permis.setChecked(false);
                }
                break;
            case R.id.check_permis:
                if (check_permis.isChecked()){
                    check_card.setChecked(false);
                    check_permis.setChecked(true);
                    check_passeport.setChecked(false);
                }
                break;
            case R.id.check_passeport:
                if (check_passeport.isChecked()){
                    check_card.setChecked(false);
                    check_permis.setChecked(false);
                    check_passeport.setChecked(true);
                }
                break;
            case R.id.button_annuler:
                formatDoc.setVisibility(View.GONE);
                startNFC.setVisibility(View.VISIBLE);
                NFCscan.setVisibility(View.GONE);
                results.setVisibility(View.GONE);
                break;
            case R.id.Logout:
                signin = true;
                // save state
                SharedPreferences.Editor editor = getSharedPreferences("save",MODE_PRIVATE).edit();
                editor.putBoolean("value",signin);
                editor.apply();
                logout(choixID_Activity.this);
                break;
            case R.id.selectdocument_button:
                if (check_card.isChecked() || check_permis.isChecked() ){
                    docType = DocType.ID_CARD;
                    requestPermissionForCamera();
                }
                else if (check_passeport.isChecked() ){
                    docType = DocType.PASSPORT;
                    requestPermissionForCamera();
                }
                else {
                    Toast.makeText(getApplicationContext(), R.string.selection_pieceID, Toast.LENGTH_SHORT).show();
                }
                break;

            case  R.id.Next :
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, "New Picture");
                values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera");
                image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                //Camera intent
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
                cameraIntent.putExtra("android.intent.extras.CAMERA_FACING", 1);
                startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE);
                break;
            case  R.id.Reconnaitre_person :
                Intent CibWebActivity= new Intent(getApplicationContext(),CibWebActivity.class);
                startActivity(CibWebActivity  );
                break;
        }
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

    // Back button
    public void Clickback (View view){
        finish();
    }

    /////////To open the settings menu/////////
    public void ClickMenu(View view ){
        // open drawer
        openDrawer(drawerLayout);
    }

    private static void openDrawer(DrawerLayout drawerLayout) {
        // open drawer layout
        drawerLayout.openDrawer(GravityCompat.END);
    }
    /////////To Close the settings menu/////////
    public  void ClickLogo(View view ){
        // close drawer
        closeDrawer(drawerLayout);
    }

    private static void closeDrawer(DrawerLayout drawerLayout) {
        // close drawer layout
        if (drawerLayout.isDrawerOpen(GravityCompat.END)){
            //When drawer is open close darwer
            drawerLayout.closeDrawer(GravityCompat.END);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //close drawer
        closeDrawer(drawerLayout);

        if (adapter != null) {
            adapter.disableForegroundDispatch(this);
        }
    }


  private static void logout(choixID_Activity choixID_activity) {
        //Initialize alerte dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(choixID_activity);
        //Set title
        builder.setTitle("Logout");
        //Set message
        builder.setMessage("Are you sure you want to logout?");
        //Positive yes button
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Finish activity
                choixID_activity.finish();
                //Exit app
                System.exit(0);
            }
        });
          // Negative no button
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // dismiss dialog
                dialog.dismiss();
            }
        });
        // Show dialog
        builder.show();
    }


    ////////////MRZ
    private void openCameraActivity() {
        Intent intent = new Intent(this, mrzscan.class);
        intent.putExtra(DOC_TYPE, docType);
        startActivityForResult(intent, APP_CAMERA_ACTIVITY_REQUEST_CODE);
    }

    private void requestPermissionForCamera() {
        String[] permissions = { Manifest.permission.CAMERA };
        boolean isPermissionGranted = PermissionUtil.hasPermissions(this, permissions);

        if (!isPermissionGranted) {
            AppUtil.showAlertDialog(this, getString(R.string.permission_title), getString(R.string.permission_description), getString(R.string.button_ok), false, (dialogInterface, i) -> ActivityCompat.requestPermissions(this, permissions, PermissionUtil.REQUEST_CODE_MULTIPLE_PERMISSIONS));
        } else {
            openCameraActivity();
        }
    }

    private void clearViews() {
        ivPhoto.setImageBitmap(null);
        output_signature.setImageBitmap(null);
        nameFirstname.setText("");
        output_last_name.setText("");
        output_first_name.setText("");
        output_gender.setText("");
        output_nationality.setText("");
        output_birthDate.setText("");
        IDdoc.setText("");
        output_state.setText("");
        output_expirationDate.setText("");
        output_birthPlace.setText("");
        output_groupeSinguin.setText("");
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (adapter != null) {
            Intent intent = new Intent(getApplicationContext(), this.getClass());
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            String[][] filter = new String[][]{new String[]{"android.nfc.tech.IsoDep"}};
            adapter.enableForegroundDispatch(this, pendingIntent, null, filter);
        }
    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(intent.getAction())) {
            Tag tag = intent.getExtras().getParcelable(NfcAdapter.EXTRA_TAG);
            if (Arrays.asList(tag.getTechList()).contains("android.nfc.tech.IsoDep")) {
                clearViews();
                if (passportNumber != null && !passportNumber.isEmpty()
                        && expirationDate != null && !expirationDate.isEmpty()
                        && birthDate != null && !birthDate.isEmpty()) {
                    BACKeySpec bacKey = new BACKey(passportNumber, birthDate, expirationDate);
                    IsoDep isoDep = IsoDep.get(tag);
                    isoDep.setTimeout(5*1000);
                    new ReadTask(isoDep, bacKey).execute();

                    formatDoc.setVisibility(View.GONE);
                    startNFC.setVisibility(View.GONE);
                    NFCscan.setVisibility(View.VISIBLE);
                }
                else {
                    Snackbar.make(progressbar, R.string.error_input, Snackbar.LENGTH_SHORT).show();
                }
            }
        }
    }


    private class ReadTask extends AsyncTask<Void, Void, Exception> {

        private IsoDep isoDep;
        private BACKeySpec bacKey;

        private ReadTask(IsoDep isoDep, BACKeySpec bacKey) {
            this.isoDep = isoDep;
            this.bacKey = bacKey;
        }

        EDocument eDocument = new EDocument();
        DocType docType = DocType.DZ;
        PersonDetails personDetails = new PersonDetails();
        AdditionalPersonDetails additionalPersonDetails = new AdditionalPersonDetails();

        @Override
        protected Exception doInBackground(Void... params) {

            try {
                CardService cardService = CardService.getInstance(isoDep);
                cardService.open();

                PassportService service = new PassportService(cardService, NORMAL_MAX_TRANCEIVE_LENGTH, DEFAULT_MAX_BLOCKSIZE, true, false);
                service.open();

                boolean paceSucceeded = false;
                try {
                    CardSecurityFile cardSecurityFile = new CardSecurityFile(service.getInputStream(PassportService.EF_CARD_SECURITY));
                    Collection<SecurityInfo> securityInfoCollection = cardSecurityFile.getSecurityInfos();
                    for (SecurityInfo securityInfo : securityInfoCollection) {
                        if (securityInfo instanceof PACEInfo) {
                            PACEInfo paceInfo = (PACEInfo) securityInfo;
                            service.doPACE(bacKey, paceInfo.getObjectIdentifier(), PACEInfo.toParameterSpec(paceInfo.getParameterId()), null);
                            paceSucceeded = true;
                        }
                    }
                } catch (Exception e) {
                    Log.w(TAG, e);
                }

                service.sendSelectApplet(paceSucceeded);

                if (!paceSucceeded) {
                    try {
                        service.getInputStream(PassportService.EF_COM).read();
                    } catch (Exception e) {
                        service.doBAC(bacKey);
                    }
                }

                boolean hashesMatched = true;
                boolean activeAuth = true;
                boolean chipAuth = true;
                boolean docSignatureValid;

                CardFileInputStream sodIn = service.getInputStream(PassportService.EF_SOD);
                SODFile sodFile = new SODFile(sodIn);
                for (Map.Entry<Integer, byte[]> dgHash : sodFile.getDataGroupHashes().entrySet()) {
                    Log.d(TAG, "Data group: " + dgHash.getKey() + " hash value: " + StringUtil.byteArrayToHex(dgHash.getValue()));
                }

                String digestAlgorithm = sodFile.getDigestAlgorithm();
                Log.d(TAG, "Digest Algorithm: " + digestAlgorithm);

                X509Certificate docSigningCert = sodFile.getDocSigningCertificate();
                String pemFile = SecurityUtil.convertToPem(docSigningCert);
                Log.d(TAG, "Document Signer Certificate: " + docSigningCert.toString());
                Log.d(TAG, "Document Signer Certificate Pem : " + pemFile);

                String digestEncryptionAlgorithm = sodFile.getDigestEncryptionAlgorithm();
                String signerInfoDigestAlgorithm = sodFile.getSignerInfoDigestAlgorithm();

                byte[] eContent = sodFile.getEContent();
                byte[] signature = sodFile.getEncryptedDigest();

                MessageDigest digest;

                if (digestEncryptionAlgorithm == null) {
                    try {
                        digest = MessageDigest.getInstance(signerInfoDigestAlgorithm);
                    } catch (Exception e) {
                        digest = MessageDigest.getInstance(signerInfoDigestAlgorithm, new BouncyCastleProvider());
                    }

                    digest.update(eContent);
                    byte[] digestBytes = digest.digest();
                    docSignatureValid = Arrays.equals(digestBytes, signature);
                } else {

                    if ("SSAwithRSA/PSS".equals(digestEncryptionAlgorithm)) {
                        digestEncryptionAlgorithm = signerInfoDigestAlgorithm.replace("-", "") + "withRSA/PSS";
                    } else if ("RSA".equals(digestEncryptionAlgorithm)) {
                        digestEncryptionAlgorithm = signerInfoDigestAlgorithm.replace("-", "") + "withRSA";
                    }

                    Log.d(TAG, "Digest Encryption Algorithm: " + digestEncryptionAlgorithm);

                    Signature sig = Signature.getInstance(digestEncryptionAlgorithm, new BouncyCastleProvider());

                    if (digestEncryptionAlgorithm.endsWith("withRSA/PSS")) {
                        int saltLength = SecurityUtil.findSaltRSA_PSS(digestEncryptionAlgorithm, docSigningCert, eContent, signature);
                        MGF1ParameterSpec mgf1ParameterSpec = new MGF1ParameterSpec("SHA-256");
                        PSSParameterSpec pssParameterSpec = new PSSParameterSpec("SHA-256", "MGF1", mgf1ParameterSpec, saltLength, 1);
                        sig.setParameter(pssParameterSpec);
                    }

                    sig.initVerify(docSigningCert);
                    sig.update(eContent);
                    docSignatureValid = sig.verify(signature);
                }

                if (Security.getAlgorithms("MessageDigest").contains(digestAlgorithm)) {
                    digest = MessageDigest.getInstance(digestAlgorithm);
                } else {
                    digest = MessageDigest.getInstance(digestAlgorithm, new BouncyCastleProvider());
                }

                // -- Personal Details -- //
                CardFileInputStream dg1In = service.getInputStream(PassportService.EF_DG1);
                DG1File dg1File = new DG1File(dg1In);

                String encodedDg1File = new String(dg1File.getEncoded());

                MRZInfo mrzInfo = dg1File.getMRZInfo();
                personDetails.setName(mrzInfo.getSecondaryIdentifier().replace("<", " ").trim());
                personDetails.setSurname(mrzInfo.getPrimaryIdentifier().replace("<", " ").trim());
                personDetails.setPersonalNumber(mrzInfo.getPersonalNumber());
                personDetails.setGender(mrzInfo.getGender().toString());
                personDetails.setBirthDate(DateUtil.convertFromMrzDate(mrzInfo.getDateOfBirth()));
                personDetails.setExpiryDate(DateUtil.convertFromMrzDate(mrzInfo.getDateOfExpiry()));
                personDetails.setSerialNumber(mrzInfo.getDocumentNumber());
                personDetails.setNationality(mrzInfo.getNationality());
                personDetails.setIssuerAuthority(mrzInfo.getIssuingState());

                if("I".equals(mrzInfo.getDocumentCode())) {
                    docType = DocType.ID_CARD;
                    encodedDg1File = StringUtil.fixPersonalNumberMrzData(encodedDg1File, mrzInfo.getPersonalNumber());
                } else if("P".equals(mrzInfo.getDocumentCode())) {
                    docType = DocType.PASSPORT;
                }

                byte[] dg1StoredHash = sodFile.getDataGroupHashes().get(1);
                byte[] dg1ComputedHash = digest.digest(encodedDg1File.getBytes());
                Log.d(TAG, "DG1 Stored Hash: " + StringUtil.byteArrayToHex(dg1StoredHash));
                Log.d(TAG, "DG1 Computed Hash: " + StringUtil.byteArrayToHex(dg1ComputedHash));

                if (Arrays.equals(dg1StoredHash, dg1ComputedHash)) {
                    Log.d(TAG, "DG1 Hashes are matched");
                } else {
                    hashesMatched = false;
                }


                // -- Face Image -- //
                CardFileInputStream dg2In = service.getInputStream(PassportService.EF_DG2);
                DG2File dg2File = new DG2File(dg2In);
                byte[] dg2StoredHash = sodFile.getDataGroupHashes().get(2);
                byte[] dg2ComputedHash = digest.digest(dg2File.getEncoded());
                Log.d(TAG, "DG2 Stored Hash: " + StringUtil.byteArrayToHex(dg2StoredHash));
                Log.d(TAG, "DG2 Computed Hash: " + StringUtil.byteArrayToHex(dg2ComputedHash));

                if (Arrays.equals(dg2StoredHash, dg2ComputedHash)) {
                    Log.d(TAG, "DG2 Hashes are matched");
                } else {
                    hashesMatched = false;
                }

                List<FaceInfo> faceInfos = dg2File.getFaceInfos();
                List<FaceImageInfo> allFaceImageInfos = new ArrayList<>();
                for (FaceInfo faceInfo : faceInfos) {
                    allFaceImageInfos.addAll(faceInfo.getFaceImageInfos());
                }


                if (!allFaceImageInfos.isEmpty()) {
                    FaceImageInfo faceImageInfo = allFaceImageInfos.iterator().next();
                    Image image = ImageUtil.getImage(choixID_Activity.this, faceImageInfo, docType, "Face");
                    personDetails.setFaceImage(image.getBitmapImage());
                    personDetails.setFaceImageBase64(image.getBase64Image());
                }

                // -- Fingerprint (if exist)-- //
                try {
                    CardFileInputStream dg3In = service.getInputStream(PassportService.EF_DG3);
                    DG3File dg3File = new DG3File(dg3In);

                    byte[] dg3StoredHash = sodFile.getDataGroupHashes().get(3);
                    byte[] dg3ComputedHash = digest.digest(dg3File.getEncoded());
                    Log.d(TAG, "DG3 Stored Hash: " + StringUtil.byteArrayToHex(dg3StoredHash));
                    Log.d(TAG, "DG3 Computed Hash: " + StringUtil.byteArrayToHex(dg3ComputedHash));

                    if (Arrays.equals(dg3StoredHash, dg3ComputedHash)) {
                        Log.d(TAG, "DG3 Hashes are matched");
                    } else {
                        hashesMatched = false;
                    }

                    List<FingerInfo> fingerInfos = dg3File.getFingerInfos();
                    List<FingerImageInfo> allFingerImageInfos = new ArrayList<>();
                    for (FingerInfo fingerInfo : fingerInfos) {
                        allFingerImageInfos.addAll(fingerInfo.getFingerImageInfos());
                    }

                    List<Bitmap> fingerprintsImage = new ArrayList<>();

                    if (!allFingerImageInfos.isEmpty()) {

                        for(FingerImageInfo fingerImageInfo : allFingerImageInfos) {
                            Image image = ImageUtil.getImage(choixID_Activity.this, fingerImageInfo, docType, "Fingerprint");
                            fingerprintsImage.add(image.getBitmapImage());
                        }

                        personDetails.setFingerprints(fingerprintsImage);

                    }
                } catch (Exception e) {
                    Log.w(TAG, e);
                }

                // -- Portrait Picture -- //
                try {
                    CardFileInputStream dg5In = service.getInputStream(PassportService.EF_DG5);
                    DG5File dg5File = new DG5File(dg5In);
                    byte[] dg5StoredHash = sodFile.getDataGroupHashes().get(5);
                    byte[] dg5ComputedHash = digest.digest(dg5File.getEncoded());
                    Log.d(TAG, "DG5 Stored Hash: " + StringUtil.byteArrayToHex(dg5StoredHash));
                    Log.d(TAG, "DG5 Computed Hash: " + StringUtil.byteArrayToHex(dg5ComputedHash));

                    if (Arrays.equals(dg5StoredHash, dg5ComputedHash)) {
                        Log.d(TAG, "DG5 Hashes are matched");
                    } else {
                        hashesMatched = false;
                    }

                    List<DisplayedImageInfo> displayedImageInfos = dg5File.getImages();

                    if (!displayedImageInfos.isEmpty()) {
                        DisplayedImageInfo displayedImageInfo = displayedImageInfos.iterator().next();
                        Image image = ImageUtil.getImage(choixID_Activity.this, displayedImageInfo, docType, "Portrait");
                        personDetails.setPortraitImage(image.getBitmapImage());
                        personDetails.setPortraitImageBase64(image.getBase64Image());
                    }
                } catch (Exception e) {
                    Log.w(TAG, e);
                }

                // -- Signature (if exist) -- //
                try {
                    CardFileInputStream dg7In = service.getInputStream(PassportService.EF_DG7);
                    DG7File dg7File = new DG7File(dg7In);

                    byte[] dg7StoredHash = sodFile.getDataGroupHashes().get(7);
                    byte[] dg7ComputedHash = digest.digest(dg7File.getEncoded());
                    Log.d(TAG, "DG7 Stored Hash: " + StringUtil.byteArrayToHex(dg7StoredHash));
                    Log.d(TAG, "DG7 Computed Hash: " + StringUtil.byteArrayToHex(dg7ComputedHash));

                    if (Arrays.equals(dg7StoredHash, dg7ComputedHash)) {
                        Log.d(TAG, "DG7 Hashes are matched");
                    } else {
                        hashesMatched = false;
                    }

                    List<DisplayedImageInfo> signatureImageInfos = dg7File.getImages();
                    if (!signatureImageInfos.isEmpty()) {
                        DisplayedImageInfo displayedImageInfo = signatureImageInfos.iterator().next();
                        Image image = ImageUtil.getImage(choixID_Activity.this, displayedImageInfo, docType, "Signature");
                        personDetails.setPortraitImage(image.getBitmapImage());
                        personDetails.setPortraitImageBase64(image.getBase64Image());
                    }

                } catch (Exception e) {
                    Log.w(TAG, e);
                }

                // -- Additional Personal Details (if exist) -- //
                try {
                    CardFileInputStream dg11In = service.getInputStream(PassportService.EF_DG11);
                    DG11File dg11File = new DG11File(dg11In);

                    byte[] dg11StoredHash = sodFile.getDataGroupHashes().get(11);
                    byte[] dg11ComputedHash = digest.digest(dg11File.getEncoded());
                    Log.d(TAG, "DG11 Stored Hash: " + StringUtil.byteArrayToHex(dg11StoredHash));
                    Log.d(TAG, "DG11 Computed Hash: " + StringUtil.byteArrayToHex(dg11ComputedHash));

                    if (Arrays.equals(dg11StoredHash, dg11ComputedHash)) {
                        Log.d(TAG, "DG11 Hashes are matched");
                    } else {
                        hashesMatched = false;
                    }

                    if(dg11File.getLength() > 0) {
                        additionalPersonDetails.setCustodyInformation(dg11File.getCustodyInformation());
                        additionalPersonDetails.setNameOfHolder(dg11File.getNameOfHolder());
                        additionalPersonDetails.setFullDateOfBirth(dg11File.getFullDateOfBirth());
                        additionalPersonDetails.setOtherNames(dg11File.getOtherNames());
                        additionalPersonDetails.setOtherValidTDNumbers(dg11File.getOtherValidTDNumbers());
                        additionalPersonDetails.setPermanentAddress(dg11File.getPermanentAddress());
                        additionalPersonDetails.setPersonalNumber(dg11File.getPersonalNumber());
                        additionalPersonDetails.setPersonalSummary(dg11File.getPersonalSummary());
                        additionalPersonDetails.setPlaceOfBirth(dg11File.getPlaceOfBirth());
                        additionalPersonDetails.setProfession(dg11File.getProfession());
                        additionalPersonDetails.setProofOfCitizenship(dg11File.getProofOfCitizenship());
                        additionalPersonDetails.setTag(dg11File.getTag());
                        additionalPersonDetails.setTagPresenceList(dg11File.getTagPresenceList());
                        additionalPersonDetails.setTelephone(dg11File.getTelephone());
                        additionalPersonDetails.setTitle(dg11File.getTitle());
                    }
                } catch (Exception e) {
                    Log.w(TAG, e);
                }

                // -- Additional Document Details (if exist) -- //
                try {
                    CardFileInputStream dg12In = service.getInputStream(PassportService.EF_DG12);
                    DG12File dg12File = new DG12File(dg12In);

                    byte[] dg12StoredHash = sodFile.getDataGroupHashes().get(12);
                    byte[] dg12ComputedHash = digest.digest(dg12File.getEncoded());
                    Log.d(TAG, "DG12 Stored Hash: " + StringUtil.byteArrayToHex(dg12StoredHash));
                    Log.d(TAG, "DG12 Computed Hash: " + StringUtil.byteArrayToHex(dg12ComputedHash));

                    if (Arrays.equals(dg12StoredHash, dg12ComputedHash)) {
                        Log.d(TAG, "DG12 Hashes are matched");
                    } else {
                        hashesMatched = false;
                    }

                } catch (Exception e) {
                    Log.w(TAG, e);
                }

                // -- Security Options (if exist) -- //
                try {
                    CardFileInputStream dg14In = service.getInputStream(PassportService.EF_DG14);
                    DG14File dg14File = new DG14File(dg14In);
                    byte[] dg14StoredHash = sodFile.getDataGroupHashes().get(14);
                    byte[] dg14ComputedHash = digest.digest(dg14File.getEncoded());

                    Log.d(TAG, "DG14 Stored Hash: " + StringUtil.byteArrayToHex(dg14StoredHash));
                    Log.d(TAG, "DG14 Computed Hash: " + StringUtil.byteArrayToHex(dg14ComputedHash));

                    if (Arrays.equals(dg14StoredHash, dg14ComputedHash)) {
                        Log.d(TAG, "DG14 Hashes are matched");
                    } else {
                        hashesMatched = false;
                    }

                    // Chip Authentication
                    List<EACCAResult> eaccaResults = new ArrayList<>();
                    List<ChipAuthenticationPublicKeyInfo> chipAuthenticationPublicKeyInfos = new ArrayList<>();
                    ChipAuthenticationInfo chipAuthenticationInfo = null;

                    if (!dg14File.getSecurityInfos().isEmpty()) {
                        for (SecurityInfo securityInfo : dg14File.getSecurityInfos()) {
                            Log.d(TAG, "DG14 Security Info Identifier: " + securityInfo.getObjectIdentifier());
                            if (securityInfo instanceof ChipAuthenticationInfo) {
                                chipAuthenticationInfo = (ChipAuthenticationInfo) securityInfo;
                            } else if (securityInfo instanceof ChipAuthenticationPublicKeyInfo) {
                                chipAuthenticationPublicKeyInfos.add((ChipAuthenticationPublicKeyInfo) securityInfo);
                            }
                        }

                        for (ChipAuthenticationPublicKeyInfo chipAuthenticationPublicKeyInfo: chipAuthenticationPublicKeyInfos) {
                            if (chipAuthenticationInfo != null) {
                                EACCAResult eaccaResult = service.doEACCA(chipAuthenticationInfo.getKeyId(), chipAuthenticationInfo.getObjectIdentifier(), chipAuthenticationInfo.getProtocolOIDString(), chipAuthenticationPublicKeyInfo.getSubjectPublicKey());
                                eaccaResults.add(eaccaResult);
                            } else {
                                Log.d(TAG, "Chip Authentication failed for key: " + chipAuthenticationPublicKeyInfo.toString());
                            }
                        }

                        if (eaccaResults.size() == 0)
                            chipAuth = false;
                    }

                    /*
                        if (paceSucceeded) {
                            service.doEACTA(caReference, terminalCerts, privateKey, null, eaccaResults.get(0), mrzInfo.getDocumentNumber())
                        } else {
                            service.doEACTA(caReference, terminalCerts, privateKey, null, eaccaResults.get(0), paceSucceeded)
                        }
                    */

                } catch (Exception e) {
                    Log.w(TAG, e);
                }

                // -- Document (Active Authentication) Public Key -- //
                try {
                    CardFileInputStream dg15In = service.getInputStream(PassportService.EF_DG15);
                    DG15File dg15File = new DG15File(dg15In);

                    byte[] dg15StoredHash = sodFile.getDataGroupHashes().get(15);
                    byte[] dg15ComputedHash = digest.digest(dg15File.getEncoded());
                    Log.d(TAG, "DG15 Stored Hash: " + StringUtil.byteArrayToHex(dg15StoredHash));
                    Log.d(TAG, "DG15 Computed Hash: " + StringUtil.byteArrayToHex(dg15ComputedHash));

                    if (Arrays.equals(dg15StoredHash, dg15ComputedHash)) {
                        Log.d(TAG, "DG15 Hashes are matched");
                    } else {
                        hashesMatched = false;
                    }

                    PublicKey publicKey = dg15File.getPublicKey();
                    String publicKeyAlgorithm = publicKey.getAlgorithm();
                    eDocument.setDocPublicKey(publicKey);

                    // Active Authentication
                    if ("EC".equals(publicKeyAlgorithm) || "ECDSA".equals(publicKeyAlgorithm)) {
                        digestAlgorithm = Util.inferDigestAlgorithmFromSignatureAlgorithm("SHA1WithRSA/ISO9796-2");
                    }

                    SecureRandom sr = new SecureRandom();
                    byte[] challenge = new byte[8];
                    sr.nextBytes(challenge);
                    AAResult result = service.doAA(dg15File.getPublicKey(), sodFile.getDigestAlgorithm(), sodFile.getSignerInfoDigestAlgorithm(), challenge);
                    activeAuth = SecurityUtil.verifyAA(dg15File.getPublicKey(), digestAlgorithm, digestEncryptionAlgorithm, challenge, result.getResponse());
                    Log.d(TAG, StringUtil.byteArrayToHex(result.getResponse()));
                } catch (Exception e) {
                    Log.w(TAG, e);
                }

                eDocument.setDocType(docType);
                eDocument.setPersonDetails(personDetails);
                eDocument.setAdditionalPersonDetails(additionalPersonDetails);
                eDocument.setPassiveAuth(hashesMatched);
                eDocument.setActiveAuth(activeAuth);
                eDocument.setChipAuth(chipAuth);
                eDocument.setDocSignatureValid(docSignatureValid);

            } catch (Exception e) {
                return e;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Exception exception) {
            results.setVisibility(View.VISIBLE);
            NFCscan.setVisibility(View.GONE);

            if (exception == null) {
                setResultToView(eDocument);
            } else {
                Snackbar.make(results, exception.getLocalizedMessage(), Snackbar.LENGTH_LONG).show();
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PermissionUtil.REQUEST_CODE_MULTIPLE_PERMISSIONS) {
            int result = grantResults[0];
            if (result == PackageManager.PERMISSION_DENIED) {
                if (!PermissionUtil.showRationale(this, permissions[0])) {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivityForResult(intent, APP_SETTINGS_ACTIVITY_REQUEST_CODE);
                } else {
                    requestPermissionForCamera();
                }
            } else if (result == PackageManager.PERMISSION_GRANTED) {
                openCameraActivity();
            }
        }
    }

    private void setMrzData(MRZInfo mrzInfo) {
        adapter = NfcAdapter.getDefaultAdapter(this);
        formatDoc.setVisibility(View.GONE);
        NFCscan.setVisibility(View.GONE);
        results.setVisibility(View.GONE);
        startNFC.setVisibility(View.VISIBLE);
        passportNumber = mrzInfo.getDocumentNumber();
        expirationDate = mrzInfo.getDateOfExpiry();
        birthDate = mrzInfo.getDateOfBirth();
    }

    private void readCard() {

        String mrzData = "I<UTOD231458907<<<<<<<<<<<<<<<" +
                "7408122F1204159UTO<<<<<<<<<<<6"+
                "ERIKSSON<<ANNA<MARIA<<<<<<<<<<";
        MRZInfo mrzInfo = new MRZInfo(mrzData);
        setMrzData(mrzInfo);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case APP_CAMERA_ACTIVITY_REQUEST_CODE:
                    MRZInfo mrzInfo = (MRZInfo) data.getSerializableExtra(MRZ_RESULT);
                    if(mrzInfo != null) {
                        setMrzData(mrzInfo);
                    }
                    else {
                        Snackbar.make(progressbar, R.string.error_input, Snackbar.LENGTH_SHORT).show();
                    }
                    break;
                case IMAGE_CAPTURE_CODE:
                    //called when image was captured from camera
                    super.onActivityResult(requestCode, resultCode, data);
                    if (resultCode == RESULT_OK) {
                        //set the image captured to our ImageView
                        formatDoc.setVisibility(View.GONE);
                        NFCscan.setVisibility(View.GONE);
                        results.setVisibility(View.GONE);
                        startNFC.setVisibility(View.GONE);
                        Selfie.setVisibility(View.VISIBLE);
                        selfie_photo1.setImageBitmap(photo);
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("Selfie",image_uri);
                        setResult(Activity.RESULT_OK, returnIntent);
                        selfie_photo2.setImageURI(image_uri);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private void setResultToView(EDocument eDocument) {

        photo = ImageUtil.scaleImage(eDocument.getPersonDetails().getFaceImage());
        ivPhoto.setImageBitmap(photo);


        Bitmap image_signature = ImageUtil.scaleImage(eDocument.getPersonDetails().getPortraitImage());
        output_signature.setImageBitmap(image_signature);

        nameFirstname.setText(eDocument.getPersonDetails().getName() +" "+ eDocument.getPersonDetails().getSurname());
        output_last_name.setText(eDocument.getPersonDetails().getName());
        output_first_name.setText(eDocument.getPersonDetails().getSurname());
        output_gender.setText(eDocument.getPersonDetails().getGender());
        output_nationality.setText(eDocument.getPersonDetails().getNationality());
        output_birthDate.setText(eDocument.getPersonDetails().getBirthDate());
        IDdoc.setText(eDocument.getPersonDetails().getSerialNumber());
        output_state.setText(eDocument.getPersonDetails().getIssuerAuthority());
        output_expirationDate.setText(eDocument.getPersonDetails().getExpiryDate());

        List<String> output_groupeSinguin1 = eDocument.getAdditionalPersonDetails().getPermanentAddress();
        if (output_groupeSinguin1 != null)
        {output_groupeSinguin.setText(output_groupeSinguin1.get(2));}
        else {output_groupeSinguinField.setVisibility(View.GONE); }

        List<String> output_Placebirth= eDocument.getAdditionalPersonDetails().getPlaceOfBirth();
        String birthPlace="";
        for(String item:output_Placebirth){
            birthPlace+= (item+"");
        }
        output_birthPlace.setText(output_Placebirth.get(0));

    }

}