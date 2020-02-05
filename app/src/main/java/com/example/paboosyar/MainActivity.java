package com.example.paboosyar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.vision.CameraSource;

import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    Button mFoodBtn;
    Button mCulturalBtn;
    Button mRecreationalBtn;
    Button mTrainBtn;
    Button mBlanketBtn;
    Button mEntityBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFoodBtn = findViewById(R.id.activity_main_food_button);
        mCulturalBtn  = findViewById(R.id.activity_main_cultural_button);
        mRecreationalBtn = findViewById(R.id.activity_main_recreational_button);
        mTrainBtn = findViewById(R.id.activity_main_train_button);
        mBlanketBtn = findViewById(R.id.activity_main_blanket_button);
        mEntityBtn = findViewById(R.id.activity_main_Entity_button);


    }


    public void showBlanket(View view) {
        Intent intent = new Intent(MainActivity.this, ScannerActivity.class);
        intent.putExtra("title", getString(R.string.blanket));
        intent.putExtra("has_history", true);
        startActivity(intent);
    }


    public void showEntity(View view) {
        Intent intent = new Intent(MainActivity.this, ScannerActivity.class);
        intent.putExtra("title", getString(R.string.entity));
        intent.putExtra("has_history", false);
        startActivity(intent);
    }

    public void showTrain(View view) {
        Intent intent = new Intent(MainActivity.this, ScannerActivity.class);
        intent.putExtra("title", getString(R.string.train));
        intent.putExtra("has_history", false);
        startActivity(intent);
    }

    public void showRecreational(View view) {
        Intent intent = new Intent(MainActivity.this, RecreationalActivity.class);
        startActivity(intent);
    }

    public void showFood(View view) {
        Intent intent = new Intent(MainActivity.this, ScannerActivity.class);
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
}
