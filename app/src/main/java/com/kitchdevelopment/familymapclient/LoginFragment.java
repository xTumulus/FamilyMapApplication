package com.kitchdevelopment.familymapclient;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.kitchdevelopment.familymapclient.cache.DataCache;
import com.kitchdevelopment.familymapclient.proxy.ServerProxy;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import Models.User;
import Requests.LoginRequest;
import Requests.RegisterRequest;
import Results.BaseResult;
import Results.BatchEventResult;
import Results.LoginOrRegisterResult;
import Results.PersonResult;

public class LoginFragment extends Fragment {

    final String API_PATH_LOGIN = "/user/login";
    final String API_PATH_REGISTER = "/user/register";

    Button loginButton;
    Button registerButton;

    String userName;
    String password;
    String email;
    String firstName;
    String lastName;
    String serverHost;
    String serverPort;
    char gender;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        //Text fields
        final EditText userNameField = v.findViewById(R.id.userName);
        final EditText passwordField = v.findViewById(R.id.password);
        final EditText hostField = v.findViewById(R.id.serverHost);
        final EditText portField = v.findViewById(R.id.serverPort);
        final EditText emailField = v.findViewById(R.id.email);
        final EditText firstNameField = v.findViewById(R.id.firstName);
        final EditText lastNameField = v.findViewById(R.id.lastName);

        //Radiogroup
        final RadioGroup genderRadioGroup = v.findViewById(R.id.genderRadioGroup);

        //Buttons
        loginButton = v.findViewById(R.id.login_button);
        registerButton = v.findViewById(R.id.register_button);
        loginButton.setEnabled(false);
        registerButton.setEnabled(false);

        //Radiogroup listener
        genderRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.genderMale) {
                    gender = 'm';
                }
                else {
                    gender = 'f';
                }
                if(userNameField.getText() != null && !userNameField.getText().toString().isEmpty()
                        && passwordField.getText() != null && !passwordField.getText().toString().isEmpty()
                        && hostField.getText() != null && !hostField.getText().toString().isEmpty()
                        && portField.getText() != null && !portField.getText().toString().isEmpty()) {

                    loginButton.setEnabled(true);

                    if(firstNameField.getText() != null && !firstNameField.getText().toString().isEmpty()
                            && lastNameField.getText() != null && !lastNameField.getText().toString().isEmpty()
                            && emailField.getText() != null && !emailField.getText().toString().isEmpty()
                            && gender != 0){

                        registerButton.setEnabled(true);
                    }
                    else {
                        registerButton.setEnabled(false);
                    }
                }
                else {
                    loginButton.setEnabled(false);
                }
            }
        });

        //Text field listeners
        TextWatcher validateFields = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {};

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {};

            @Override
            public void afterTextChanged(Editable s){
                if(userNameField.getText() != null && !userNameField.getText().toString().isEmpty()
                        && passwordField.getText() != null && !passwordField.getText().toString().isEmpty()
                        && hostField.getText() != null && !hostField.getText().toString().isEmpty()
                        && portField.getText() != null && !portField.getText().toString().isEmpty()) {

                    loginButton.setEnabled(true);

                    if(firstNameField.getText() != null && !firstNameField.getText().toString().isEmpty()
                            && lastNameField.getText() != null && !lastNameField.getText().toString().isEmpty()
                            && emailField.getText() != null && !emailField.getText().toString().isEmpty()
                            && gender != 0){

                        registerButton.setEnabled(true);
                    }
                    else {
                        registerButton.setEnabled(false);
                    }
                }
                else {
                    loginButton.setEnabled(false);
                }
            };
        };

        userNameField.addTextChangedListener(validateFields);
        passwordField.addTextChangedListener(validateFields);
        hostField.addTextChangedListener(validateFields);
        portField.addTextChangedListener(validateFields);
        firstNameField.addTextChangedListener(validateFields);
        lastNameField.addTextChangedListener(validateFields);
        emailField.addTextChangedListener(validateFields);

        //Button Watchers
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("clicked login");
                loginButton.setEnabled(false);
                registerButton.setEnabled(false);

                //get form values
                userName = userNameField.getText().toString();
                password = passwordField.getText().toString();
                serverHost = hostField.getText().toString();
                serverPort = portField.getText().toString();

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

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("clicked register");
                loginButton.setEnabled(false);
                registerButton.setEnabled(false);

                //get form values
                userName = userNameField.getText().toString();
                password = passwordField.getText().toString();
                email = emailField.getText().toString();
                firstName = firstNameField.getText().toString();
                lastName = lastNameField.getText().toString();
                serverHost = hostField.getText().toString();
                serverPort = portField.getText().toString();
                //gender determined in radiogroup listener

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

    private void sendAsyncToast(final String message) {
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public class LoginUserTask extends AsyncTask<URL, Void, LoginOrRegisterResult> {
        @Override
        protected LoginOrRegisterResult doInBackground(URL... urls) {
            ServerProxy serverProxy = new ServerProxy();

            LoginRequest loginRequest = new LoginRequest(userName, password);
            try {
                LoginOrRegisterResult result = serverProxy.login(loginRequest, urls[0]);
                if(result.isSuccess()) {
                    new GetFamilyDataTask().execute(result);
                }
                onPostExecute(result);
                return result;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(LoginOrRegisterResult loginOrRegisterResult) {
            if(loginOrRegisterResult.isSuccess()) {
                sendAsyncToast(getResources().getString(R.string.login_success_message));
            }
            else {
                sendAsyncToast(getResources().getString(R.string.login_failed_message));
            }
        }
    }

    public class RegisterUserTask extends AsyncTask<URL, Void, LoginOrRegisterResult> {
        @Override
        protected LoginOrRegisterResult doInBackground(URL... urls) {
            ServerProxy serverProxy = new ServerProxy();

            RegisterRequest registerRequest = new RegisterRequest(userName, password, email, firstName, lastName, gender);
            try {
                LoginOrRegisterResult result = serverProxy.register(registerRequest, urls[0]);
                if(result.isSuccess()) {
                    new GetFamilyDataTask().execute(result);
                }
                onPostExecute(result);
                return result;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(LoginOrRegisterResult loginOrRegisterResult) {
            if(loginOrRegisterResult.isSuccess()) {
                sendAsyncToast(getResources().getString(R.string.register_success_message));
            }
            else {
                sendAsyncToast(getResources().getString(R.string.register_failed_message));
            }
        }
    }

    public class GetFamilyDataTask extends AsyncTask<LoginOrRegisterResult, Void, BatchEventResult> {
        ServerProxy serverProxy = new ServerProxy();

        @Override
        protected BatchEventResult doInBackground(LoginOrRegisterResult...results) {
            try {
                serverProxy.getPeople(results[0].getAuthToken(), serverHost, serverPort);
                BatchEventResult result = serverProxy.getEvents(results[0].getAuthToken(), serverHost, serverPort);
                onPostExecute(result);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(BatchEventResult result) {
            sendAsyncToast(getResources().getString(R.string.welcome) + " " +
                    DataCache.getInstance().getUser().getFirstName() + " " + DataCache.getInstance().getUser().getLastName());
        }
    }
}
