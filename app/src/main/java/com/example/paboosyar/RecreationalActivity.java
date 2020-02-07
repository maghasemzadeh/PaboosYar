package com.example.paboosyar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class RecreationalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recreational);


    }


    public void showPool(View view) {
        Intent intent = new Intent(RecreationalActivity.this, ScannerActivity.class);
        intent.putExtra("title", getString(R.string.blue_waves_pool));
        intent.putExtra("has_history", true);
        intent.putExtra("url", "program/25/sub_program/59/");
        intent.putExtra("history_url", "program/25/sub_program/59/");
        startActivity(intent);
    }


    public void showPaintball(View view) {
        Intent intent = new Intent(RecreationalActivity.this, ScannerActivity.class);
        intent.putExtra("title", getString(R.string.paintball));
        intent.putExtra("has_history", true);
        intent.putExtra("url", "program/25/sub_program/60/");
        intent.putExtra("history_url", "program/25/sub_program/60/");
        startActivity(intent);
    }


    public void showBowling(View view) {
        Intent intent = new Intent(RecreationalActivity.this, ScannerActivity.class);
        intent.putExtra("title", getString(R.string.bowling));
        intent.putExtra("has_history", true);
        intent.putExtra("url", "program/25/sub_program/76/");
        intent.putExtra("history_url", "program/25/sub_program/76/");
        startActivity(intent);
    }

    public void showFutsal(View view) {
        Intent intent = new Intent(RecreationalActivity.this, ScannerActivity.class);
        intent.putExtra("title", getString(R.string.futsal));
        intent.putExtra("has_history", true);
        intent.putExtra("url", "program/25/sub_program/61/");
        intent.putExtra("history_url", "program/25/sub_program/61/");
        startActivity(intent);
    }


    public void back(View view) {
        finish();
    }
}
