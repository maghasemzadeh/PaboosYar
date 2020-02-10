package com.azzahraa.paboosyar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.azzahraa.paboosyar.RetrofitModels.NetworkAPIService;

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
        intent.putExtra("url", NetworkAPIService.POOL);
        intent.putExtra("history_url", NetworkAPIService.POOL_HISTORY);
        startActivity(intent);
    }


    public void showPaintball(View view) {
        Intent intent = new Intent(RecreationalActivity.this, ScannerActivity.class);
        intent.putExtra("title", getString(R.string.paintball));
        intent.putExtra("has_history", true);
        intent.putExtra("url", NetworkAPIService.PAINTBALL);
        intent.putExtra("history_url", NetworkAPIService.PAINTBALL_HISTORY);
        startActivity(intent);
    }


    public void showBowling(View view) {
        Intent intent = new Intent(RecreationalActivity.this, ScannerActivity.class);
        intent.putExtra("title", getString(R.string.bowling));
        intent.putExtra("has_history", true);
        intent.putExtra("url", NetworkAPIService.BOWLING);
        intent.putExtra("history_url", NetworkAPIService.BOWLING_HISTORY);
        startActivity(intent);
    }

    public void showFutsal(View view) {
        Intent intent = new Intent(RecreationalActivity.this, ScannerActivity.class);
        intent.putExtra("title", getString(R.string.futsal));
        intent.putExtra("has_history", true);
        intent.putExtra("url", NetworkAPIService.FOOTSAL);
        intent.putExtra("history_url", NetworkAPIService.FOOTSAL_HISTORY);
        startActivity(intent);
    }


    public void back(View view) {
        finish();
    }
}
