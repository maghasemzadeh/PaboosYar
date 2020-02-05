package com.example.paboosyar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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
        intent.putExtra("url", "dining/program/22/receipt");
        startActivity(intent);
    }

    public void showGraduatedFood(View view) {
        Intent intent = new Intent(FoodActivity.this, ScannerActivity.class);
        intent.putExtra("title", getString(R.string.food) + "ی " + getString(R.string.graduated));
        intent.putExtra("has_history", true);
        intent.putExtra("url", "dining/program/23/receipt");
        startActivity(intent);
    }

    public void showWomenFood(View view) {
        Intent intent = new Intent(FoodActivity.this, ScannerActivity.class);
        intent.putExtra("title", getString(R.string.food) + "ی " + getString(R.string.women));
        intent.putExtra("has_history", true);
        intent.putExtra("url", "dining/program/24/receipt/");
        startActivity(intent);
    }
}
