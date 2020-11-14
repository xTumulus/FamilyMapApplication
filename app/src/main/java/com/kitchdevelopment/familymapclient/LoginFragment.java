package com.kitchdevelopment.familymapclient;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.kitchdevelopment.familymapclient.proxy.HttpClient;
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
                //Do stuff
                System.out.println("clicked login");
            }
        });

        final Button registerButton = v.findViewById(R.id.register_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Do stuff
                System.out.println("clicked register");
            }
        });
        return v;
    }

    public class LoginUserTask extends AsyncTask<URL, Void, PersonResult> {
        @Override
        protected LoginOrRegisterResult doInBackground(URL... urls) {
            HttpClient httpClient = new HttpClient();

            for(int i = 0; i < urls.length; i++)
                try {
                    String urlContent = httpClient.getUrlString(urls[i]);
                    Log.i("LoginFragment", "Fetched contents of url: " + urlContent);
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
            HttpClient httpClient = new HttpClient();

            for(int i = 0; i < urls.length; i++)
                try {
                    PersonResult persons = httpClient.getUrlString(urls[i]);
                    ServerProxy.getPeople();
//                    Log.i("LoginFragment", "Fetched contents of url: " + urlContent);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            //Do something with url content
            return null;
        }
    }
}
