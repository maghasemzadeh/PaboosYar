package com.azzahraa.paboosyar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.azzahraa.paboosyar.RetrofitModels.Authentication;
import com.azzahraa.paboosyar.RetrofitModels.User;
import com.azzahraa.paboosyar.RetrofitModels.NetworkAPIService;
import com.azzahraa.paboosyar.RetrofitModels.Username;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.concurrent.atomic.AtomicBoolean;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAnalytics mFirebaseAnalytics;

    private final String azzahraa_site = "http://azzahraa.ir";

    Button loginBtn;
    EditText mUsernameEt;
    EditText mPasswordEt;
    ImageView azzahraaLogo;


    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        loginBtn = findViewById(R.id.activity_login_sign_in_button);
        mUsernameEt = findViewById(R.id.activity_login_username_et);
        mPasswordEt = findViewById(R.id.activity_login_password_et);
        azzahraaLogo = findViewById(R.id.azzahraa_logo);

        preferences = getApplicationContext().getSharedPreferences(Prefs.MAIN_PREF, 0);
        editor = preferences.edit();
        if(preferences.getString(Prefs.TOKEN, "") != "") {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        mPasswordEt.setOnEditorActionListener((v, actionId, event) -> {
            if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                signIn(null);
            }
            return false;
        });

    }

    public void signIn(View view) {
        String username = String.valueOf(mUsernameEt.getText());
        String password = String.valueOf(mPasswordEt.getText());

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://account.azzahraa.ir/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        NetworkAPIService retrofitHandler = retrofit.create(NetworkAPIService.class);

        Call<Authentication> call = retrofitHandler.getToken((new User(username, password)));
        call.enqueue(new Callback<Authentication>() {
            @Override
            public void onResponse(Call<Authentication> call, Response<Authentication> response) {
                if (response.isSuccessful()) {
                    String token = "Token " + response.body().getToken();
                    editor.putString(Prefs.TOKEN, token);
                    editor.commit();
                    Call<com.azzahraa.paboosyar.RetrofitModels.Response> validateCall = retrofitHandler.getResponse(new Username("0023457708"), token, NetworkAPIService.ENTITY);
                    validateCall.enqueue(new Callback<com.azzahraa.paboosyar.RetrofitModels.Response>() {
                        @Override
                        public void onResponse(Call<com.azzahraa.paboosyar.RetrofitModels.Response> call, Response<com.azzahraa.paboosyar.RetrofitModels.Response> response) {
                            if (!response.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, getString(R.string.just_admin_entrance), Toast.LENGTH_SHORT).show();
                                return;
                            }

                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            Toast.makeText(LoginActivity.this, getString(R.string.welcome_khadem), Toast.LENGTH_LONG).show();
                            finish();
                        }

                        @Override
                        public void onFailure(Call<com.azzahraa.paboosyar.RetrofitModels.Response> call, Throwable t) {
                            Toast.makeText(LoginActivity.this, getString(R.string.connection_error) , Toast.LENGTH_LONG).show();
                        }
                    });

                } else {
                    Toast.makeText(LoginActivity.this, getString(R.string.incorrect_pass), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Authentication> call, Throwable t) {
                createNetErrorDialog();
            }
        });

    }

    public void logoClick(View view) {
        Intent siteIntent = new Intent(Intent.ACTION_VIEW);
        siteIntent.setData(Uri.parse(azzahraa_site));
        siteIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(siteIntent);
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
                            LoginActivity.this.finish();
                        }
                );
        AlertDialog alert = builder.create();
        alert.show();
    }
}
