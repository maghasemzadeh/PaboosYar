package com.example.paboosyar;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements ResultFragment.OnFragmentInteractionListener {


    SurfaceView mCameraPriview;
    TextView mResultTv;
    Button mEnableBtn;
    Spinner mSpinner;


    BarcodeDetector barcodeDetector;
    CameraSource cameraSource;

    MediaPlayer acceptSound;
    MediaPlayer rejectSound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        acceptSound = MediaPlayer.create(this, R.raw.accept);
        rejectSound = MediaPlayer.create(this, R.raw.reject);

        mCameraPriview = findViewById(R.id.activity_result_camera_preview);
        mResultTv = findViewById(R.id.frg_result_main_text_view);
        mEnableBtn = findViewById(R.id.frg_done_button);
        mSpinner = findViewById(R.id.activity_main_actions_spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.actions, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mSpinner.setAdapter(adapter);


        barcodeDetector = new BarcodeDetector.Builder(this).setBarcodeFormats(Barcode.QR_CODE).build();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        cameraSource = new CameraSource.Builder(this, barcodeDetector).setRequestedPreviewSize(height, width).setAutoFocusEnabled(true).build();

        mCameraPriview.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                initializeCamera(holder);
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });


        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> qrCodes = detections.getDetectedItems();

                if(qrCodes.size() != 0) {
                    mCameraPriview.post(new Runnable() {
                        @Override
                        public void run() {
                            try {

                                String nationalCode = decryptNationalCode(qrCodes.valueAt(0).displayValue);
                                boolean hasFood = hasFood(nationalCode);
                                Vibrator vibrator = (Vibrator)getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                                FragmentManager fm = getSupportFragmentManager();
                                ResultFragment fragment = new ResultFragment();
                                androidx.fragment.app.FragmentTransaction ft = fm.beginTransaction();
                                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                                ft.addToBackStack(null);
                                ft.replace(R.id.main_layout,fragment).commit();
                                fragment.setType(hasFood);
                                if(hasFood) {
                                    vibrator.vibrate(200);
                                    acceptSound.start();
                                } else {
                                    vibrator.vibrate(1000);
                                    rejectSound.start();
                                }
                                cameraSource.stop();
                            } catch (InvalidQRCodeException ignored) {
                                Toast.makeText(MainActivity.this, R.string.invalid_qrcode, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        acceptSound.reset();
        rejectSound.reset();
    }

    private void initializeCamera(SurfaceHolder holder) {
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, 1);
        }
        try {
            cameraSource.start(holder);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private String decryptNationalCode(String displayValue) throws InvalidQRCodeException {
        StringBuilder result = new StringBuilder();
        for(int i = 0; i < displayValue.length() / 2 ; i++) {
            int tmp = displayValue.charAt(2 * i + 1) - 48;
            result.append(9 - tmp);
        }
        InvalidQRCodeException e = new InvalidQRCodeException();
        if (result.length() != 10)
            throw e;
        try {
            int tmp = Integer.parseInt(result.toString());
        } catch (Exception ignored) {
            throw e;
        }
        return result.toString();
    }

    private class InvalidQRCodeException extends Exception {
    }

    private String decryptPhoneNumber(String displayValue) throws InvalidQRCodeException{
        StringBuilder result = new StringBuilder();
        for(int i = 0; i < displayValue.length() / 2 ; i++) {
            int tmp = displayValue.charAt(2 * i) - 48;
            result.insert(0, (9 - tmp));
        }
        if(result.length() != 11) {
            throw new InvalidQRCodeException();
        }
        return result.toString();
    }

    boolean hasFood(String code) {
        Random r = new Random();
        return r.nextBoolean();
    }

    @Override
    public void onFragmentInteraction() {
        initializeCamera(mCameraPriview.getHolder());
    }
}