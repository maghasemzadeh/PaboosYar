package com.example.paboosyar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

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
    }
}
