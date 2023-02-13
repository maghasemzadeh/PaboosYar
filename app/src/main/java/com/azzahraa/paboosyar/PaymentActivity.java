package com.azzahraa.paboosyar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.azzahraa.paboosyar.RetrofitModels.NetworkAPIService;

public class PaymentActivity extends AppCompatActivity {
    EditText paymentAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        paymentAmount = findViewById(R.id.activity_payment_amount_number);
    }

    public void showPaymentView(View view) {
        int amount = Integer.parseInt(String.valueOf(paymentAmount.getText()));
        Intent intent = new Intent(PaymentActivity.this, ScannerActivity.class);
        intent.putExtra("title", getString(R.string.payment));
        intent.putExtra("has_history", false);
        intent.putExtra("url", NetworkAPIService.PAYMENT);
        intent.putExtra("amount", amount);
        startActivity(intent);
    }

    public void back(View view) {
        finish();
    }
}
