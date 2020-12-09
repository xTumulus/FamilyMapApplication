package com.kitchdevelopment.familymapclient;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import com.kitchdevelopment.familymapclient.cache.DataCache;

public class SettingsActivity extends AppCompatActivity {
    DataCache dataCache = DataCache.getInstance();

    //View Elements
    ToggleButton lifeLinesToggle;
    ToggleButton familyTreeLinesToggle;
    ToggleButton spouseLinesToggle;
    ToggleButton fathersSideToggle;
    ToggleButton mothersSideToggle;
    ToggleButton maleEventToggle;
    ToggleButton femaleEventToggle;
    LinearLayout logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        lifeLinesToggle = findViewById(R.id.lifeLinesToggle);
        familyTreeLinesToggle = findViewById(R.id.familyTreeLinesToggle);
        spouseLinesToggle = findViewById(R.id.spouseLinesToggle);
        fathersSideToggle = findViewById(R.id.fathersSideToggle);
        mothersSideToggle = findViewById(R.id.mothersSideToggle);
        maleEventToggle = findViewById(R.id.maleEventToggle);
        femaleEventToggle = findViewById(R.id.femaleEventToggle);
        logoutButton = findViewById(R.id.logout_button);

        getButtonStates();

        lifeLinesToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    dataCache.setShowLifeStoryLine(true);
                }
                else {
                    dataCache.setShowLifeStoryLine(false);
                }
            }
        });

        familyTreeLinesToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    dataCache.setShowFamilyTreeLines(true);
                }
                else {
                    dataCache.setShowFamilyTreeLines(false);
                }
            }
        });

        spouseLinesToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    dataCache.setShowSpouseLines(true);
                }
                else {
                    dataCache.setShowSpouseLines(false);
                }
            }
        });

        //TODO Add Others Here

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataCache.setLoggedIn(false);
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
    }

    @Override
    protected void onResume() {
        getButtonStates();
        super.onResume();
    }

    public void getButtonStates() {
        //Default all to true
        lifeLinesToggle.setChecked(true);
        familyTreeLinesToggle.setChecked(true);
        spouseLinesToggle.setChecked(true);
        fathersSideToggle.setChecked(true);
        mothersSideToggle.setChecked(true);
        maleEventToggle.setChecked(true);
        femaleEventToggle.setChecked(true);

        //Check for false and change accordingly
        if(!dataCache.showLifeStoryLine()) { lifeLinesToggle.setChecked(false); }
        if(!dataCache.showFamilyTreeLines()) { familyTreeLinesToggle.setChecked(false); }
        if(!dataCache.showSpouseLines()) { spouseLinesToggle.setChecked(false); }
        if(!dataCache.showFathersSideEvents()) { fathersSideToggle.setChecked(false); }
        if(!dataCache.showMothersSideEvents()) { mothersSideToggle.setChecked(false); }
        if(!dataCache.showMaleEvents()) { maleEventToggle.setChecked(false); }
        if(!dataCache.showFemaleEvents()) { femaleEventToggle.setChecked(false); }
    }
}
