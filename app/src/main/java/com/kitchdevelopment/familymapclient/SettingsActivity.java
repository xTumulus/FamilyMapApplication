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
				if (isChecked) {
					dataCache.setShowLifeStoryLine(true);
				} else {
					dataCache.setShowLifeStoryLine(false);
				}
			}
		});

		familyTreeLinesToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					dataCache.setShowFamilyTreeLines(true);
				} else {
					dataCache.setShowFamilyTreeLines(false);
				}
			}
		});

		spouseLinesToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					dataCache.setShowSpouseLines(true);
				} else {
					dataCache.setShowSpouseLines(false);
				}
			}
		});

		fathersSideToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					dataCache.setShowFathersSideEvents(true);
				} else {
					dataCache.setShowFathersSideEvents(false);
				}
				dataCache.filterEvents();
			}
		});

		mothersSideToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					dataCache.setShowMothersSideEvents(true);
				} else {
					dataCache.setShowMothersSideEvents(false);
				}
				dataCache.filterEvents();
			}
		});

		maleEventToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					dataCache.setShowMaleEvents(true);
				} else {
					dataCache.setShowMaleEvents(false);
				}
				dataCache.filterEvents();
			}
		});

		femaleEventToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					dataCache.setShowFemaleEvents(true);
				} else {
					dataCache.setShowFemaleEvents(false);
				}
				dataCache.filterEvents();
			}
		});

		logoutButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dataCache.setLoggedIn(false);
				Intent intent = new Intent(getApplicationContext(), MainActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
		});
	}

	@Override
	protected void onResume() {
		getButtonStates();
		super.onResume();
	}

	public void getButtonStates() {
		//Check for value and change accordingly
		if (!dataCache.showLifeStoryLine()) {
			lifeLinesToggle.setChecked(false);
		} else {
			lifeLinesToggle.setChecked(true);
		}
		if (!dataCache.showFamilyTreeLines()) {
			familyTreeLinesToggle.setChecked(false);
		} else {
			familyTreeLinesToggle.setChecked(true);
		}
		if (!dataCache.showSpouseLines()) {
			spouseLinesToggle.setChecked(false);
		} else {
			spouseLinesToggle.setChecked(true);
		}
		if (!dataCache.showFathersSideEvents()) {
			fathersSideToggle.setChecked(false);
		} else {
			fathersSideToggle.setChecked(true);
		}
		if (!dataCache.showMothersSideEvents()) {
			mothersSideToggle.setChecked(false);
		} else {
			mothersSideToggle.setChecked(true);
		}
		if (!dataCache.showMaleEvents()) {
			maleEventToggle.setChecked(false);
		} else {
			maleEventToggle.setChecked(true);
		}
		if (!dataCache.showFemaleEvents()) {
			femaleEventToggle.setChecked(false);
		} else {
			femaleEventToggle.setChecked(true);
		}
	}
}
