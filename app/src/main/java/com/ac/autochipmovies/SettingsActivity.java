package com.ac.autochipmovies;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import app_utility.DatabaseHandler;
import app_utility.OnGenericInterfaceListener;
import app_utility.SharedPreferencesClass;

public class SettingsActivity extends AppCompatActivity implements OnGenericInterfaceListener {

    RecyclerView recyclerView;
    public static OnGenericInterfaceListener onGenericInterfaceListener;
    DatabaseHandler dbHandler;
    private SharedPreferencesClass sharedPreferencesClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        onGenericInterfaceListener = this;
        sharedPreferencesClass = new SharedPreferencesClass(SettingsActivity.this);
        dbHandler = new DatabaseHandler(SettingsActivity.this);
        initViews();
    }

    void initViews(){
        recyclerView = findViewById(R.id.rv_add_account);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(SettingsActivity.this);
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLinearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(SettingsActivity.this, DividerItemDecoration.VERTICAL));
        recyclerView.setHasFixedSize(true);

        ArrayList<String> alAccountNames = new ArrayList<>();
        alAccountNames.add("Amazon Prime");
        alAccountNames.add("HotStar");
        alAccountNames.add("NetFlix");

        ArrayList<Integer> alAccountImages = new ArrayList<>();
        alAccountImages.add(R.drawable.amazon_prime_logo);
        alAccountImages.add(R.drawable.hotstar_logo);
        alAccountImages.add(R.drawable.netflix_logo);

        AddAccountRVAdapter addAccountRVAdapter = new AddAccountRVAdapter(recyclerView, alAccountNames, alAccountImages, dbHandler, sharedPreferencesClass);
        recyclerView.setAdapter(addAccountRVAdapter);
    }

    public void hideKeyboardFrom(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onCommandReceived(String sCase, int nID, String sResult) {
        switch (sCase){
            case "DISPLAY_TOAST":
                Toast.makeText(SettingsActivity.this, "clicked view : " + nID, Toast.LENGTH_SHORT).show();
                break;
            case "GENERAL_TOAST":
                Toast.makeText(SettingsActivity.this, sResult, Toast.LENGTH_LONG).show();
                if(nID>=3){
                    hideKeyboardFrom(recyclerView);
                }
                break;
        }
    }
}
