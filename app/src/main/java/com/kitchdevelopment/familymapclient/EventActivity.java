package com.kitchdevelopment.familymapclient;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.kitchdevelopment.familymapclient.cache.DataCache;

import Models.Event;

public class EventActivity extends AppCompatActivity {

	Event event;
	DataCache dataCache = DataCache.getInstance();
	private FragmentManager fm = this.getSupportFragmentManager();
	private MapFragment mapFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		String eventId = getIntent().getStringExtra("eventId");
		event = dataCache.getEventById(eventId);
		dataCache.setSelectedEvent(event);
		dataCache.setFromEventView(true);

		super.onCreate(savedInstanceState);
		Iconify.with(new FontAwesomeModule());
		setContentView(R.layout.activity_event);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		mapFragment = (MapFragment) fm.findFragmentById(R.id.fragment_container);
		if (mapFragment == null) {
			mapFragment = new MapFragment();
			fm.beginTransaction()
					.add(R.id.fragment_container, mapFragment)
					.commit();
		}
	}
}
