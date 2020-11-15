package com.kitchdevelopment.familymapclient;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.kitchdevelopment.familymapclient.proxy.ServerProxy;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import Models.User;
import Requests.LoginRequest;
import Requests.RegisterRequest;
import Results.LoginOrRegisterResult;
import Results.PersonResult;

public class LoginFragment extends Fragment {

    final String API_PATH_LOGIN = "/user/login";
    final String API_PATH_REGISTER = "/user/register";

    String userName;
    String password;
    String email;
    String firstName;
    String lastName;
    String serverHost;
    String serverPort;
    Character gender;

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

                //get form values
                EditText userNameField = v.findViewById(R.id.userName);
                userName = userNameField.getText().toString();
                EditText passwordField = v.findViewById(R.id.password);
                password = passwordField.getText().toString();
                EditText hostField = v.findViewById(R.id.serverHost);
                serverHost = hostField.getText().toString();
                EditText portText = v.findViewById(R.id.serverPort);
                serverPort = portText.getText().toString();

                try {
                    StringBuilder requestString = new StringBuilder();
                    requestString.append("http://" + serverHost + ":" + serverPort + API_PATH_LOGIN);
                    URL url = new URL(requestString.toString());
                    new LoginUserTask().execute(url);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        });

        final Button registerButton = v.findViewById(R.id.register_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("clicked register");

                //get form values
                EditText userNameField = v.findViewById(R.id.userName);
                userName = userNameField.getText().toString();
                EditText passwordField = v.findViewById(R.id.password);
                password = passwordField.getText().toString();
                EditText emailField = v.findViewById(R.id.email);
                email = emailField.getText().toString();
                EditText firstNameField = v.findViewById(R.id.firstName);
                firstName = firstNameField.getText().toString();
                EditText lastNameField = v.findViewById(R.id.lastName);
                lastName = lastNameField.getText().toString();
                EditText hostField = v.findViewById(R.id.serverHost);
                serverHost = hostField.getText().toString();
                EditText portText = v.findViewById(R.id.serverPort);
                serverPort = portText.getText().toString();

                //determine gender from radio buttons
                RadioGroup radioGroup = v.findViewById(R.id.genderRadioGroup);
                int genderRadioId = radioGroup.getCheckedRadioButtonId();
                RadioButton selectedGender = v.findViewById(genderRadioId);
                if (selectedGender.getText() == "Male") {
                    gender = 'm';
                }
                else {
                    gender = 'f';
                }

                try {
                    StringBuilder requestString = new StringBuilder();
                    requestString.append("http://" + serverHost + ":" + serverPort + API_PATH_REGISTER);
                    URL url = new URL(requestString.toString());
                    new RegisterUserTask().execute(url);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        });
        return v;
    }

    public class LoginUserTask extends AsyncTask<URL, Void, LoginOrRegisterResult> {
        @Override
        protected LoginOrRegisterResult doInBackground(URL... urls) {
            ServerProxy serverProxy = new ServerProxy();

            for(int i = 0; i < urls.length; i++) {
                LoginRequest loginRequest = new LoginRequest(userName, password);
                LoginOrRegisterResult result = null;
                try {
                    result = serverProxy.login(loginRequest, urls[i]);
//                    serverProxy.getPeople(result.getAuthToken())
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return result;
            }

            return null;
        }
    }

    public class RegisterUserTask extends AsyncTask<URL, Void, LoginOrRegisterResult> {
        @Override
        protected LoginOrRegisterResult doInBackground(URL... urls) {
            ServerProxy serverProxy = new ServerProxy();

            for(int i = 0; i < urls.length; i++) {
                RegisterRequest registerRequest = new RegisterRequest(userName, password, email, firstName, lastName, gender);
                LoginOrRegisterResult result = null;
                try {
                    result = serverProxy.register(registerRequest, urls[i]);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return result;
            }
            return null;
        }
    }

//    public class GetFamilyDataTask extends AsyncTask<URL, Void, PersonResult> {
//        @Override
//        protected PersonResult doInBackground(URL... urls) {
//            ServerProxy serverProxy = new ServerProxy();
//
//            for(int i = 0; i < urls.length; i++)
//                try {
//                    PersonResult result = serverProxy.getPeople();
////                    Log.i("LoginFragment", "Fetched contents of url: " + urlContent);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//            return null;
//        }
//    }
}
