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
        startActivity(intent);
    }

    public void showRestHomeJanbazView(View view) {
        Intent intent = new Intent(CulturalActivity.this, ScannerActivity.class);
        intent.putExtra("title", getString(R.string.rest_home_view_janbaz));
        intent.putExtra("has_history", true);
        startActivity(intent);
    }

    public void showRestHomeMaloolView(View view) {
        Intent intent = new Intent(CulturalActivity.this, ScannerActivity.class);
        intent.putExtra("title", getString(R.string.rest_home_malool_view));
        intent.putExtra("has_history", true);
        startActivity(intent);
    }

    public void back(View view) {
        finish();
    }
}
