package com.azzahraa.paboosyar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.azzahraa.paboosyar.RetrofitModels.NetworkAPIService;

public class MainActivity extends AppCompatActivity {

    Button mFoodBtn;
    Button mCulturalBtn;
    Button mRecreationalBtn;
    Button mBlanketBtn;
    Button mEntityBtn;


    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFoodBtn = findViewById(R.id.activity_main_food_button);
        mCulturalBtn  = findViewById(R.id.activity_main_cultural_button);
        mRecreationalBtn = findViewById(R.id.activity_main_recreational_button);
        mBlanketBtn = findViewById(R.id.activity_main_blanket_button);
        mEntityBtn = findViewById(R.id.activity_main_entity_button);


        preferences = getApplicationContext().getSharedPreferences(Prefs.MAIN_PREF, 0);
        editor = preferences.edit();

        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, 1);
        }

    }


    public void showBlanket(View view) {
        Intent intent = new Intent(MainActivity.this, ScannerActivity.class);
        intent.putExtra("title", getString(R.string.blanket));
        intent.putExtra("has_history", true);
        intent.putExtra("url", NetworkAPIService.BLANKET);
        intent.putExtra("history_url", NetworkAPIService.BLANKET_HISTORY);
        startActivity(intent);
    }


    public void showEntity(View view) {
        Intent intent = new Intent(MainActivity.this, ScannerActivity.class);
        intent.putExtra("title", getString(R.string.entity));
        intent.putExtra("has_history", false);
        intent.putExtra("url", NetworkAPIService.ENTITY);
        intent.putExtra("history_url", "");
        startActivity(intent);
    }


    public void showRecreational(View view) {
        Intent intent = new Intent(MainActivity.this, RecreationalActivity.class);
        startActivity(intent);
    }

    public void showFood(View view) {
        Intent intent = new Intent(MainActivity.this, FoodActivity.class);
        intent.putExtra("title", getString(R.string.food));
        intent.putExtra("has_history", true);
        startActivity(intent);
    }

    public void showCultural(View view) {
        Intent intent = new Intent(MainActivity.this, CulturalActivity.class);
        intent.putExtra("title", getString(R.string.cultural));
        intent.putExtra("has_history", true);
        startActivity(intent);
    }

    public void logout(View view) {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
        editor.putString(Prefs.TOKEN, "");
        editor.commit();
    }

    public void showBook(View view) {
        Intent intent = new Intent(MainActivity.this, ScannerActivity.class);
        intent.putExtra("title", getString(R.string.book));
        intent.putExtra("has_history", true);
        intent.putExtra("url", NetworkAPIService.BOOK);
        intent.putExtra("history_url", NetworkAPIService.BOOK_HISTORY);
        startActivity(intent);
    }
}
