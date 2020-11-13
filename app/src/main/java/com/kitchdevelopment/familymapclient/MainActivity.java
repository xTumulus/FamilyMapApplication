package com.kitchdevelopment.familymapclient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fm = this.getSupportFragmentManager();
        LoginFragment loginFragment = (LoginFragment) fm.findFragmentById(R.id.fragment_container);
        if (loginFragment == null) {
            loginFragment = createLoginFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, loginFragment)
                    .commit();
        }

    }

    private LoginFragment createLoginFragment() {
        LoginFragment fragment = new LoginFragment();

        return fragment;
//            Bundle args = new Bundle();
//            args.putString(LoginFragment.ARG_TITLE, title);
//            fragment.setArguments(args);
    }
}
