package com.example.paboosyar;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.paboosyar.RetrofitModels.Meal;
import com.example.paboosyar.RetrofitModels.Response;
import com.example.paboosyar.RetrofitModels.Username;
import com.example.paboosyar.RetrofitModels.NetworkAPIService;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ScannerActivity extends AppCompatActivity implements ResultFragment.OnFragmentInteractionListener {


    SurfaceView mCameraPriview;
    TextView mResultTv;
    Button mEnableBtn;
    TextView mTitle;
    TextView mHistory;
    ImageView mRefresh;

    BarcodeDetector barcodeDetector;
    CameraSource cameraSource;

    MediaPlayer acceptSound;
    MediaPlayer rejectSound;

    boolean hasHistory;


    Toast toast;

    String message = "";
    String name = "";
    String authorization;
    String url = "";
    String historyUrl = "";

    SharedPreferences preferences;

    String token;

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://account.azzahraa.ir/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    NetworkAPIService retrofitHandler = retrofit.create(NetworkAPIService.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        acceptSound = MediaPlayer.create(this, R.raw.accept);
        rejectSound = MediaPlayer.create(this, R.raw.reject);

        mCameraPriview = findViewById(R.id.activity_scanner_camera_preview);
        mResultTv = findViewById(R.id.frg_result_main_text_view);
        mEnableBtn = findViewById(R.id.frg_done_button);
        mTitle = findViewById(R.id.activity_scanner_title);
        mHistory = findViewById(R.id.activity_scanner_history);
        mRefresh = findViewById(R.id.activity_scanner_ic_refresh);

        preferences = getApplicationContext().getSharedPreferences("mainPref", 0);
        token = preferences.getString(Prefs.TOKEN, "");
        authorization = "Token " + token;

        url = getIntent().getExtras().getString("url");
        historyUrl = getIntent().getExtras().getString("history_url");

        hasHistory = getIntent().getExtras().getBoolean("has_history");
        mTitle.setText(getIntent().getExtras().getString("title"));
        if (hasHistory) {
            mHistory.setVisibility(View.VISIBLE);
            mRefresh.setVisibility(View.VISIBLE);
            refresh(mRefresh);
        }
        else{
            mRefresh.setVisibility(View.INVISIBLE);
            mHistory.setVisibility(View.INVISIBLE);
        }

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
                toast.cancel();
                final SparseArray<Barcode> qrCodes = detections.getDetectedItems();
                if(qrCodes.size() != 0) {
                    mCameraPriview.post(() -> {
                        try {
                            if(token.equals("")) {
                                Intent intent = new Intent(ScannerActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                                return;
                            }
                            String nationalCode = decryptNationalCode(qrCodes.valueAt(0).displayValue);
                            cameraSource.stop();
                            Call<Response> responseCall = retrofitHandler.getResponse(new Username(nationalCode), authorization, url);
                            responseCall.enqueue(new Callback<Response>() {
                                @Override
                                public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                                    if(response.isSuccessful()) {
                                        boolean ok = response.body().isOk();
                                        Vibrator vibrator = (Vibrator)getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                                        FragmentManager fm = getSupportFragmentManager();
                                        ResultFragment fragment = new ResultFragment();
                                        FragmentTransaction ft = fm.beginTransaction();
                                        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                                        ft.addToBackStack(null);
                                        message = response.body().getMessage();
                                        name = response.body().getName();
                                        ft.replace(R.id.main_layout,fragment).commit();
                                        fragment.setType(ok);
                                        if(ok) {
                                            acceptSound.start();
                                            vibrator.vibrate(200);
                                        } else {
                                            vibrator.vibrate(1000);
                                            rejectSound.start();
                                        }
                                    } else {
                                        toast = Toast.makeText(ScannerActivity.this, response.message(), Toast.LENGTH_SHORT);
                                        toast.show();
                                        initializeCamera(mCameraPriview.getHolder());
                                    }
                                }

                                @Override
                                public void onFailure(Call<Response> call, Throwable t) {
                                    toast = Toast.makeText(ScannerActivity.this, "ridi", Toast.LENGTH_SHORT);
                                    toast.show();
                                }
                            });

                        } catch (InvalidQRCodeException ignored) {
                            toast = Toast.makeText(ScannerActivity.this, R.string.invalid_qrcode, Toast.LENGTH_SHORT);
                            toast.show();
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
        if (! displayValue.trim().matches("\\d{21}")) {
            throw new InvalidQRCodeException();
        }
        for(int i = 0; i < 10  ; i++) {
            int tmp = displayValue.charAt(2 * i + 1) - 48;
            result.append(9 - tmp);
        }
        return result.toString();
    }

    public void refresh(View view) {
        toast = Toast.makeText(this, getString(R.string.refreshing), Toast.LENGTH_SHORT);
        toast.show();
        Call<Response> call = retrofitHandler.getHistory(authorization, historyUrl);
        call.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                if (response.isSuccessful()) {
                    mHistory.setText(makeHistoryText(response.body().getMeal()));
                } else {
                    toast = Toast.makeText(ScannerActivity.this, response.message(), Toast.LENGTH_SHORT);
                    toast.show();
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                toast = Toast.makeText(ScannerActivity.this, "Refresh Failed", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    private String makeHistoryText(Meal meal) {
        String result = "آمار ثبت شده: ";
        result += meal.getReceipt_count();
        result += "\nآمار درخواستی: ";
        result += meal.getTotal();
        return result;
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

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String getName() {return name;}

    @Override
    public void onFragmentInteraction() {
        initializeCamera(mCameraPriview.getHolder());
    }
}