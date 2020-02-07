package com.example.paboosyar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.paboosyar.RetrofitModels.NetworkAPIService;

public class FoodActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);
    }


    public void showMenFood(View view) {
        Intent intent = new Intent(FoodActivity.this, ScannerActivity.class);
        intent.putExtra("title", getString(R.string.food) + "ی " + getString(R.string.men));
        intent.putExtra("has_history", true);
        intent.putExtra("url", NetworkAPIService.MEN_FOOD);
        intent.putExtra("history_url", NetworkAPIService.MEN_FOOD_HISTORY);
        startActivity(intent);
    }

    public void showGraduatedFood(View view) {
        Intent intent = new Intent(FoodActivity.this, ScannerActivity.class);
        intent.putExtra("title", getString(R.string.food) + "ی " + getString(R.string.graduated));
        intent.putExtra("has_history", true);
        intent.putExtra("url", NetworkAPIService.GRADUATED_FOOD);
        intent.putExtra("history_url", NetworkAPIService.GRADUATED_FOOD_HISTORY);
        startActivity(intent);
    }

    public void showWomenFood(View view) {
        Intent intent = new Intent(FoodActivity.this, ScannerActivity.class);
        intent.putExtra("title", getString(R.string.food) + "ی " + getString(R.string.women));
        intent.putExtra("has_history", true);
        intent.putExtra("url", NetworkAPIService.WOMEN_FOOD);
        intent.putExtra("history_url", NetworkAPIService.WOMEN_FOOD_HISTORY);
        startActivity(intent);
    }

    public void back(View view) {
        finish();
    }
}
