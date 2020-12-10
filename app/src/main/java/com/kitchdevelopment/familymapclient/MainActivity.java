package com.kitchdevelopment.familymapclient;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.kitchdevelopment.familymapclient.cache.DataCache;

public class MainActivity extends AppCompatActivity {

    private FragmentManager fm = this.getSupportFragmentManager();
    private LoginFragment loginFragment;
    private MapFragment mapFragment;
    DataCache dataCache = DataCache.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Iconify.with(new FontAwesomeModule());

        loginFragment = (LoginFragment) fm.findFragmentById(R.id.fragment_container);
        if (loginFragment == null) {
            loginFragment = createLoginFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, loginFragment)
                    .commit();
        }

        if(dataCache.isLoggedIn()) {
            changeToMapFragment();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search_button_item:
                startActivity(new Intent(this, SearchActivity.class));
                return true;
            case R.id.settings_button_item:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        MenuInflater inflater = getMenuInflater();
        if (dataCache.isLoggedIn()) {
            inflater.inflate(R.menu.main_menu, menu);
            return true;
        }
        else {
            return false;
        }
    }

    public void changeToMapFragment() {
        dataCache.setLoggedIn(true);
        mapFragment = createMapFragment();
        fm.beginTransaction()
            .replace(R.id.fragment_container, mapFragment)
            .commit();
        updateMenu();
    }

    private void updateMenu() {
        invalidateOptionsMenu();
    }

    private LoginFragment createLoginFragment() {
        LoginFragment fragment = new LoginFragment();
        return fragment;
    }

    private MapFragment createMapFragment() {
        MapFragment fragment = new MapFragment();
        return fragment;
    }
}
