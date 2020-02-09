package com.azzahraa.paboosyar;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.azzahraa.paboosyar.RetrofitModels.Meal;
import com.azzahraa.paboosyar.RetrofitModels.Response;
import com.azzahraa.paboosyar.RetrofitModels.Username;
import com.azzahraa.paboosyar.RetrofitModels.NetworkAPIService;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.kinda.alert.KAlertDialog;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ScannerActivity extends AppCompatActivity implements ResultFragment.OnFragmentInteractionListener {


    SurfaceView mCameraPriview;
    TextView mResultTv;
    Button mEnableBtn;

    BarcodeDetector barcodeDetector;
    CameraSource cameraSource;

    MediaPlayer acceptSound;
    MediaPlayer rejectSound;
    MediaPlayer clickSound;

    boolean hasHistory;


    Toast toast;

    String url = "";
    String historyUrl = "";
    Response resp;

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
        rejectSound = MediaPlayer.create(this, R.raw.wrong_answer);
        clickSound = MediaPlayer.create(this, R.raw.click_sound);

        mCameraPriview = findViewById(R.id.activity_scanner_camera_preview);
        mResultTv = findViewById(R.id.frg_result_message_text_view);
        mEnableBtn = findViewById(R.id.frg_done_button);

        preferences = getApplicationContext().getSharedPreferences(Prefs.MAIN_PREF, 0);
        token = preferences.getString(Prefs.TOKEN, "");

        setTitle(getIntent().getExtras().getString("title"));



        url = getIntent().getExtras().getString("url");
        historyUrl = getIntent().getExtras().getString("history_url");



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
                if(toast != null)
                    toast.cancel();
                final SparseArray<Barcode> qrCodes = detections.getDetectedItems();
                if(qrCodes.size() != 0) {
                    if (! isInSquare(qrCodes.valueAt(0).cornerPoints))
                        return;
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
                            Call<Response> responseCall = retrofitHandler.getResponse(new Username(nationalCode), token, url);
                            responseCall.enqueue(new Callback<Response>() {
                                @Override
                                public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                                    if(response.isSuccessful()) {
                                        boolean ok = response.body().isOk();
                                        resp = response.body();
                                        Vibrator vibrator = (Vibrator)getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                                        FragmentManager fm = getSupportFragmentManager();
                                        ResultFragment fragment = new ResultFragment();
                                        FragmentTransaction ft = fm.beginTransaction();
                                        ft.setCustomAnimations(R.anim.slide_up, R.anim.slide_down, R.anim.slide_up, R.anim.slide_down);
                                        ft.addToBackStack(null);
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
                                    createNetErrorDialog();
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

    private boolean isInSquare(Point[] cornerPoints) {
        for (Point p: cornerPoints) {
            if (p.x < 0.23f * mCameraPriview.getWidth() ||
                    p.y < 0.35f * mCameraPriview.getHeight() ||
                    p.x > 0.4f * mCameraPriview.getWidth() + 400 ||
                    p.y > 0.44f * mCameraPriview.getHeight() + 400)
                return false;
        }
        return true;
    }


    protected void createNetErrorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.please_connect_internet))
                .setTitle(getString(R.string.internet_connection_error))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.settings),
                        (dialog, id) -> {
                            Intent i = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                            startActivity(i);
                        }
                )
                .setNegativeButton(getString(R.string.dismiss),
                        (dialog, id) -> {
                            ScannerActivity.this.finish();
                        }
                );
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        acceptSound.reset();
        rejectSound.reset();
    }

    private void initializeCamera(SurfaceHolder holder) {
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

    public void refresh() {
        Call<Response> call = retrofitHandler.getHistory(token, historyUrl);
        call.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                if (response.isSuccessful()) {
                    new KAlertDialog(ScannerActivity.this)
                            .setTitleText(getString(R.string.stat))
                            .setConfirmText(getString(R.string.ok))
                            .setContentText(makeStatText(response.body().getMeal()))
                            .show();
                } else {
                    toast = Toast.makeText(ScannerActivity.this, response.message(), Toast.LENGTH_SHORT);
                    toast.show();
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                toast = Toast.makeText(ScannerActivity.this, getString(R.string.connection_error), Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    private String makeStatText(Meal meal) {
        String result = "ثبت شده: ";
        result += meal.getReceipt_count();
        result += "\n درخواستی: ";
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
    public Response getResponse() {
        return resp;
    }


    @Override
    public void onFragmentFinished() {
        initializeCamera(mCameraPriview.getHolder());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.stat_menu, menu);

        hasHistory = getIntent().getExtras().getBoolean("has_history");
        MenuItem item = menu.findItem(R.id.stat);
        if (hasHistory)
            item.setVisible(true);
        else
            item.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.stat:
                clickSound.start();
                refresh();
                return true;
            case R.id.back:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}