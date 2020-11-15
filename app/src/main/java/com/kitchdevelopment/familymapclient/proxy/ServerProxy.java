package com.kitchdevelopment.familymapclient.proxy;

import com.kitchdevelopment.familymapclient.utils.GsonSerializer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import Models.AuthToken;
import Requests.LoginRequest;
import Requests.RegisterRequest;
import Results.BatchResult;
import Results.EventResult;
import Results.LoginOrRegisterResult;
import Results.PersonResult;

public class ServerProxy {
    final String REQUEST_METHOD_POST = "POST";
    final String REQUEST_METHOD_GET = "GET";
    final String API_PATH_LOGIN = "/user/login";
    final String API_PATH_REGISTER = "/user/register";
    final String API_PATH_PERSONS = "/user/person";
    final String API_PATH_EVENTS = "/user/event";

    private String getUrlString(URL url, String requestMethod, String requestBodyJson) {
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(requestMethod);
            connection.setDoOutput(true);
            connection.connect();

            try {
                //Get request stream
                OutputStream requestBody = connection.getOutputStream();
                //Write request to server
                requestBody.write(requestBodyJson.getBytes());
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                //Get responseBody input
                InputStream responseBody = connection.getInputStream();

                //Read responseBody
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int numBytes;
                while ((numBytes = responseBody.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, numBytes);
                }
                return outputStream.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private String getUrlString(URL url, String requestMethod, AuthToken authToken) {
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(requestMethod);
            if(authToken != null) {
                connection.setRequestProperty("Authorization", authToken.getAuthToken());
            }
            connection.setDoOutput(true);
            connection.connect();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                //Get responseBody input
                InputStream responseBody = connection.getInputStream();

                //Read responseBody
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int numBytes;
                while ((numBytes = responseBody.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, numBytes);
                }
                return outputStream.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    public LoginOrRegisterResult login(LoginRequest request, URL url) throws IOException {
        //Serialize the request info
        String requestBodyJson = GsonSerializer.getInstance().serialize(request);

        //Get and return response
        return GsonSerializer.deserialize(getUrlString(url, REQUEST_METHOD_POST, requestBodyJson), LoginOrRegisterResult.class);
    }

    public LoginOrRegisterResult register(RegisterRequest request, URL url) throws IOException {
        //Serialize the request info
        String requestBodyJson = GsonSerializer.getInstance().serialize(request);

        //Get and return response
        return GsonSerializer.deserialize(getUrlString(url, REQUEST_METHOD_POST, requestBodyJson), LoginOrRegisterResult.class);
    }

    public BatchResult getPeople(AuthToken token, String serverHost, String port) throws IOException {
        //Make url string
        StringBuilder requestString = new StringBuilder();
        requestString.append("http://" + serverHost + ":" + port + API_PATH_PERSONS);
        URL url = new URL(requestString.toString());

        //Does not have a request body

        //Get and return response
        BatchResult result = GsonSerializer.deserialize(getUrlString(url, REQUEST_METHOD_POST, token), BatchResult.class);
        //GivetoDataCache()
        return result;
    }

    public BatchResult getEvents(AuthToken token, String serverHost, String port) throws IOException {
        //Make url string
        StringBuilder requestString = new StringBuilder();
        requestString.append("http://" + serverHost + ":" + port + API_PATH_EVENTS);
        URL url = new URL(requestString.toString());

        //Does not have a request body

        //Get and return response
        BatchResult result = GsonSerializer.deserialize(getUrlString(url, REQUEST_METHOD_POST, token), BatchResult.class);
        //GivetoDataCache()
        return result;
    }
}
