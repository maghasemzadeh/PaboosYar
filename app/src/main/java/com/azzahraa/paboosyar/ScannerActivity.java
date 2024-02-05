package com.azzahraa.paboosyar;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.azzahraa.paboosyar.RetrofitModels.Meal;
import com.azzahraa.paboosyar.RetrofitModels.NetworkAPIService;
import com.azzahraa.paboosyar.RetrofitModels.Response;
import com.azzahraa.paboosyar.RetrofitModels.Username;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanIntentResult;
import com.journeyapps.barcodescanner.ScanOptions;
import com.kinda.alert.KAlertDialog;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * This activity is redundant as there is {@link CaptureActivity} for scanning the Qr codes.
 * But is not yet removed, because {@link CaptureActivity} only scans barcode and removes itself
 * from navigation stack and thus the api won't be called, so for the rest of logic, we should
 * handle it where it is called (like {@link MainActivity}), but for now and to lessen
 * the side effects of changes, did not happen. So for later, either the logic must be moved to
 * {@link MainActivity}, or must be moved to {@link CaptureActivity} and the immediate closing of
 * page be removed.
 */
public class ScannerActivity extends AppCompatActivity {
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

    Button scanButton;

    SharedPreferences preferences;

    String base;
    String token;
    int programID;
    int amount;

    Retrofit retrofit;

    NetworkAPIService retrofitHandler;

    ActivityResultLauncher<Intent> scanQrResultLauncher;
    ;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_page);

        scanButton = findViewById(R.id.scan_qr_code_button);
        acceptSound = MediaPlayer.create(this, R.raw.accept);
        rejectSound = MediaPlayer.create(this, R.raw.wrong_answer);
        clickSound = MediaPlayer.create(this, R.raw.click_sound);

        mResultTv = findViewById(R.id.frg_result_message_text_view);
        mEnableBtn = findViewById(R.id.frg_done_button);

        preferences = getApplicationContext().getSharedPreferences(Prefs.MAIN_PREF, 0);
        token = preferences.getString(Prefs.TOKEN, "");
        programID = preferences.getInt(Prefs.PROGRAM_ID, 49);

        setTitle(getIntent().getExtras().getString("title"));
        amount = getIntent().getExtras().getInt("amount");


        url = getIntent().getExtras().getString("url");
        historyUrl = getIntent().getExtras().getString("history_url");


        base = preferences.getString("base", NetworkAPIService.ONLINE);

        retrofit = new Retrofit.Builder()
                .baseUrl(base)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitHandler = retrofit.create(NetworkAPIService.class);


        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        scanQrResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                resultData -> {
                    Log.d("TAGGGG", "what the hell");
                    Log.d("TAGGGG", String.valueOf(resultData.getResultCode()));
                    if (resultData.getResultCode() == RESULT_OK) {
                        ScanIntentResult result = ScanIntentResult.parseActivityResult(resultData.getResultCode(), resultData.getData());

                        //this will be qr activity result
                        if (result.getContents() == null) {
                            Toast.makeText(getApplicationContext(), getString(R.string.dismiss), Toast.LENGTH_LONG).show();

                        } else {
                            String qrContents = result.getContents();
                            handleScanResult(qrContents);
                        }
                    }
                });

        scanButton.setOnClickListener((view) -> {
            initializeCamera();
        });
    }

    private void handleScanResult(String qrCodeData) {
        if (toast != null)
            toast.cancel();
        final String qrCodes = qrCodeData;
        if (!Objects.equals(qrCodes, "")) {
            if (token.equals("")) {
                Intent intent = new Intent(ScannerActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                return;
            }
            String nationalCode = null;
            try {
                nationalCode = decryptNationalCode(qrCodeData);
                toast = Toast.makeText(ScannerActivity.this, "شماره ملی: " + nationalCode, Toast.LENGTH_SHORT);
                toast.show();
            } catch (InvalidQRCodeException e) {
                toast = Toast.makeText(ScannerActivity.this, R.string.invalid_qrcode, Toast.LENGTH_SHORT);
                toast.show();
            }
            Call<Response> responseCall = retrofitHandler.getResponse(new Username(nationalCode, amount), token, url, programID);
            responseCall.enqueue(new Callback<Response>() {
                @Override
                public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                    Log.d("TAG", "onResponse: " + response);
                    if (response.isSuccessful()) {
                        boolean ok = response.body().isOk();
                        resp = response.body();
                        Vibrator vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                        FragmentManager fm = getSupportFragmentManager();
                        ResultFragment fragment = new ResultFragment();
                        FragmentTransaction ft = fm.beginTransaction();
                        ft.setCustomAnimations(R.anim.slide_up, R.anim.slide_down, R.anim.slide_up, R.anim.slide_down);
                        ft.addToBackStack(null);
                        ft.replace(R.id.main_layout, fragment).commit();
                        fragment.setType(ok);
                        if (ok) {
                            acceptSound.start();
                            vibrator.vibrate(200);
                        } else {
                            vibrator.vibrate(1000);
                            rejectSound.start();
                        }
                    } else {
                        String message = response.message();
                        if (message.isEmpty()) {
                            message = getString(R.string.fetch_info_failure);
                        }
                        toast = Toast.makeText(ScannerActivity.this, message, Toast.LENGTH_SHORT);
                        toast.show();
                        initializeCamera();
                    }
                }

                @Override
                public void onFailure(Call<Response> call, Throwable t) {
                    createNetErrorDialog();
                }
            });
        }
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

    private void initializeCamera() {
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA}, 1);
            }


            ScanOptions options = new ScanOptions();
            options.setOrientationLocked(false);
            options.setCaptureActivity(CaptureActivity.class);

            scanQrResultLauncher.launch(new ScanContract().createIntent(getApplicationContext(), options));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private String decryptNationalCode(String displayValue) throws InvalidQRCodeException {
        StringBuilder result = new StringBuilder();
        if (!displayValue.trim().matches("\\d{21}")) {
            throw new InvalidQRCodeException();
        }
        for (int i = 0; i < 10; i++) {
            int tmp = displayValue.charAt(2 * i + 1) - 48;
            result.append(9 - tmp);
        }
        return result.toString();
    }

    public void refresh() {
        Call<Response> call = retrofitHandler.getHistory(token, historyUrl, programID);
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

    private String decryptPhoneNumber(String displayValue) throws InvalidQRCodeException {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < displayValue.length() / 2; i++) {
            int tmp = displayValue.charAt(2 * i) - 48;
            result.insert(0, (9 - tmp));
        }
        if (result.length() != 11) {
            throw new InvalidQRCodeException();
        }
        return result.toString();
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