package com.kitchdevelopment.familymapclient;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.kitchdevelopment.familymapclient.cache.DataCache;

import Models.Event;

public class EventActivity extends AppCompatActivity {

    private FragmentManager fm = this.getSupportFragmentManager();
    private MapFragment mapFragment;
    Event event;
    DataCache dataCache = DataCache.getInstance();

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
