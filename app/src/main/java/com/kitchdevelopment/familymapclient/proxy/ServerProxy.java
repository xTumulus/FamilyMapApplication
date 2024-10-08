package com.kitchdevelopment.familymapclient.proxy;

import com.kitchdevelopment.familymapclient.cache.DataCache;
import com.kitchdevelopment.familymapclient.utils.GsonSerializer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import Requests.LoginRequest;
import Requests.RegisterRequest;
import Results.BatchEventResult;
import Results.BatchPersonResult;
import Results.LoginOrRegisterResult;

public class ServerProxy {
	final static String REQUEST_METHOD_POST = "POST";
	final static String REQUEST_METHOD_GET = "GET";
	final static String API_PATH_LOGIN = "/user/login";
	final static String API_PATH_REGISTER = "/user/register";
	final static String API_PATH_PERSONS = "/person";
	final static String API_PATH_EVENTS = "/event";

	private static String getUrlStringPOST(URL url, String requestBodyJson) {
		try {
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod(REQUEST_METHOD_POST);
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.connect();

			try {
				//Get and write to request stream
				OutputStream requestBody = connection.getOutputStream();
				requestBody.write(requestBodyJson.getBytes());
			} catch (Exception e) {
				e.printStackTrace();
			}

			//Get and read responseBody
			InputStream responseBody = connection.getInputStream();
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int numBytes;
			while ((numBytes = responseBody.read(buffer)) != -1) {
				outputStream.write(buffer, 0, numBytes);
			}

			if (connection.getResponseCode() == HttpURLConnection.HTTP_OK || connection.getResponseCode() == HttpURLConnection.HTTP_BAD_REQUEST) {
				return outputStream.toString();
			} else {
				//Throw error?
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	private String getUrlStringGET(URL url, String authToken) {
		try {
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod(REQUEST_METHOD_GET);
			if (authToken != null) {
				connection.setRequestProperty("Authorization", authToken);
			}
			connection.setDoOutput(false);
			connection.connect();

			//Get and read responseBody input
			InputStream responseBody = connection.getInputStream();
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int numBytes;
			while ((numBytes = responseBody.read(buffer)) != -1) {
				outputStream.write(buffer, 0, numBytes);
			}

			if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				return outputStream.toString();
			} else {
				//Throw error?
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}


	public static LoginOrRegisterResult login(LoginRequest request, URL url) throws IOException {
		//Serialize the request info
		String requestBodyJson = GsonSerializer.serialize(request);

		//Get and return response
		String responseString = getUrlStringPOST(url, requestBodyJson);
		LoginOrRegisterResult result = GsonSerializer.deserialize(responseString, LoginOrRegisterResult.class);
		return result;
	}

	public static LoginOrRegisterResult register(RegisterRequest request, URL url) throws IOException {
		//Serialize the request info
		String requestBodyJson = GsonSerializer.serialize(request);

		//Get and return response
		String responseString = getUrlStringPOST(url, requestBodyJson);
		LoginOrRegisterResult result = GsonSerializer.deserialize(responseString, LoginOrRegisterResult.class);
		return result;
	}

	public BatchPersonResult getPeople(String authToken, String serverHost, String port) throws IOException {
		URL url = new URL("http://" + serverHost + ":" + port + API_PATH_PERSONS);

		//Does not have a request body

		//Get response and cache data
		String responseString = getUrlStringGET(url, authToken);
		BatchPersonResult result = GsonSerializer.deserialize(responseString, BatchPersonResult.class);
		DataCache.getInstance().cachePersonData(result);
		return result;
	}

	public BatchEventResult getEvents(String authToken, String serverHost, String port) throws IOException {
		URL url = new URL("http://" + serverHost + ":" + port + API_PATH_EVENTS);

		//Does not have a request body

		//Get response and cache data
		String responseString = getUrlStringGET(url, authToken);
		BatchEventResult result = GsonSerializer.deserialize(responseString, BatchEventResult.class);
		DataCache.getInstance().cacheEventData(result);
		return result;
	}
}
