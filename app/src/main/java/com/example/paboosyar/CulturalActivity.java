package com.example.paboosyar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class CulturalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cultural);
    }

    public void showMartyrView(View view) {
        Intent intent = new Intent(CulturalActivity.this, ScannerActivity.class);
        intent.putExtra("title", getString(R.string.martyr_view));
        intent.putExtra("has_history", true);
        intent.putExtra("url", "program/26/sub_program/77/");
        intent.putExtra("history_url", "program/26/sub_program/77/");
        startActivity(intent);
    }

    public void showRestHomeJanbazView(View view) {
        Intent intent = new Intent(CulturalActivity.this, ScannerActivity.class);
        intent.putExtra("title", getString(R.string.rest_home_view_janbaz));
        intent.putExtra("has_history", true);
        intent.putExtra("url", "program/26/sub_program/79/");
        intent.putExtra("history_url", "program/26/sub_program/79/");
        startActivity(intent);
    }

    public void showRestHomeMaloolView(View view) {
        Intent intent = new Intent(CulturalActivity.this, ScannerActivity.class);
        intent.putExtra("title", getString(R.string.rest_home_malool_view));
        intent.putExtra("has_history", true);
        intent.putExtra("url", "program/26/sub_program/78/");
        intent.putExtra("history_url", "program/26/sub_program/78/");
        startActivity(intent);
    }

    public void back(View view) {
        finish();
    }
}
