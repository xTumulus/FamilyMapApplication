package com.kitchdevelopment.familymapclient;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.kitchdevelopment.familymapclient.proxy.ServerProxy;

import java.io.IOException;
import java.net.URL;

import Models.User;
import Results.LoginOrRegisterResult;
import Results.PersonResult;

public class LoginFragment extends Fragment {
    
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        final Button loginButton = v.findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("clicked login");
                new LoginUserTask().execute();
                new GetFamilyDataTask().execute();
            }
        });

        final Button registerButton = v.findViewById(R.id.register_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("clicked register");
//                new RegisterUserTask().execute();
            }
        });
        return v;
    }

    public class LoginUserTask extends AsyncTask<URL, Void, LoginOrRegisterResult> {
        @Override
        protected LoginOrRegisterResult doInBackground(URL... urls) {
            ServerProxy serverProxy = new ServerProxy();

            for(int i = 0; i < urls.length; i++)
                try {
                    LoginOrRegisterResult result = serverProxy.login();
//                    Log.i("LoginFragment", "Fetched contents of url: " + urlContent);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            //Do something with url content
            return null;
        }
    }

    public class GetFamilyDataTask extends AsyncTask<URL, Void, PersonResult> {
        @Override
        protected PersonResult doInBackground(URL... urls) {
            ServerProxy serverProxy = new ServerProxy();

            for(int i = 0; i < urls.length; i++)
                try {
                    PersonResult result = serverProxy.getPeople();
//                    Log.i("LoginFragment", "Fetched contents of url: " + urlContent);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            return null;
        }
    }
}
