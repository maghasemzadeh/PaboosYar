package com.example.paboosyar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.paboosyar.RetrofitModels.User;
import com.example.paboosyar.RetrofitModels.UserClient;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {


    Button loginBtn;
    EditText mUsernameEt;
    EditText mPasswordEt;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        loginBtn = findViewById(R.id.activity_login_sign_in_button);
        mUsernameEt = findViewById(R.id.activity_login_username_et);
        mPasswordEt = findViewById(R.id.activity_login_password_tv);

    }

    public void signIn(View view) {
        String username = String.valueOf(mUsernameEt.getText());
        String password = String.valueOf(mPasswordEt.getText());



        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://account.azzahraa.sharif.ir/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        UserClient userClient = retrofit.create(UserClient.class);

        Call<String> call = userClient.getUser((new User(username, password)));
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Intent intent = new Intent(LoginActivity.this, ScannerActivity.class);
                    startActivity(intent);
                    Toast.makeText(LoginActivity.this, response.body(), Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(LoginActivity.this, getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
            }
        });
        Toast.makeText(LoginActivity.this, getString(R.string.connection_error), Toast.LENGTH_SHORT).show();

        try {
            call.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {
//        protected Long doInBackground(URL... urls) {
//            int count = urls.length;
//            long totalSize = 0;
//            for (int i = 0; i < count; i++) {
//                totalSize += Downloader.downloadFile(urls[i]);
//                publishProgress((int) ((i / (float) count) * 100));
//                // Escape early if cancel() is called
//                if (isCancelled()) break;
//            }
//            return totalSize;
//        }
//
//        protected void onProgressUpdate(Integer... progress) {
//            setProgressPercent(progress[0]);
//        }
//
//        protected void onPostExecute(Long result) {
//            showDialog("Downloaded " + result + " bytes");
//        }
//    }

}
