package com.azzahraa.paboosyar;

import static com.azzahraa.paboosyar.RetrofitModels.NetworkAPIService.ONLINE;
import static com.azzahraa.paboosyar.RetrofitModels.NetworkAPIService.OPEN_PROGRAMS;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.azzahraa.paboosyar.RetrofitModels.Authentication;
import com.azzahraa.paboosyar.RetrofitModels.NetworkAPIService;
import com.azzahraa.paboosyar.RetrofitModels.Program;
import com.azzahraa.paboosyar.RetrofitModels.User;
import com.azzahraa.paboosyar.RetrofitModels.Username;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Button mFoodBtn;
    Button mBlanketBtn;
    Button mEntityBtn;
    Button mPackBtn;
    Button mBookBtn;
    Spinner programSelect;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    ArrayList<Program> programs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFoodBtn = findViewById(R.id.activity_main_food_button);
        mBlanketBtn = findViewById(R.id.activity_main_blanket_button);
        mEntityBtn = findViewById(R.id.activity_main_entity_button);
        mPackBtn = findViewById(R.id.activity_main_pack_button);
        mBookBtn = findViewById(R.id.activity_main_book_button);


        preferences = getApplicationContext().getSharedPreferences(Prefs.MAIN_PREF, 0);
        editor = preferences.edit();
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, 1);
        }

        programSelect = findViewById(R.id.program_select);
        programSelect.setOnItemSelectedListener(this);

        getPrograms();

    }

    public ArrayList<Program> getPrograms() {
        AndroidNetworking.initialize(getApplicationContext());
        preferences = getApplicationContext().getSharedPreferences(Prefs.MAIN_PREF, 0);
        String token = preferences.getString(Prefs.TOKEN, "");
        AndroidNetworking.get(ONLINE + '/' + OPEN_PROGRAMS)
                .addHeaders("Authorization", token)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Gson gson = new GsonBuilder().create();
                        List<String> spinnerArray = new ArrayList<String>();


                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject object = response.getJSONObject(i);
                                Program program = gson.fromJson(object.toString(), Program.class);
                                spinnerArray.add(program.title);
                                programs.add(program);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                                MainActivity.this, android.R.layout.simple_spinner_item, spinnerArray);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        programSelect.setAdapter(adapter);
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("Eeeeeeeeeeeeeee", anError.toString());
                    }
                });
        return programs;
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

    public void showFood(View view) {
        Intent intent = new Intent(MainActivity.this, ScannerActivity.class);
        intent.putExtra("title", getString(R.string.food));
        intent.putExtra("has_history", true);
        intent.putExtra("url", NetworkAPIService.FOOD);
        intent.putExtra("history_url", NetworkAPIService.FOOD_HISTORY);
        startActivity(intent);
    }

    public void showPack(View view) {
        Intent intent = new Intent(MainActivity.this, ScannerActivity.class);
        intent.putExtra("title", getString(R.string.pack));
        intent.putExtra("has_history", true);
        intent.putExtra("url", NetworkAPIService.PACK);
        intent.putExtra("history_url", NetworkAPIService.PACK_HISTORY);
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

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        Program selectedProgram = programs.get(pos);
        editor.putInt(Prefs.PROGRAM_ID, selectedProgram.id).commit();
        if (!selectedProgram.type.equals("mashhad")) {
            mFoodBtn.setVisibility(View.GONE);
            mBlanketBtn.setVisibility(View.GONE);
            mPackBtn.setVisibility(View.GONE);
            mBookBtn.setVisibility(View.GONE);
        } else {
            mFoodBtn.setVisibility(View.VISIBLE);
            mBlanketBtn.setVisibility(View.VISIBLE);
            mPackBtn.setVisibility(View.VISIBLE);
            mBookBtn.setVisibility(View.VISIBLE);
        }
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }
}
