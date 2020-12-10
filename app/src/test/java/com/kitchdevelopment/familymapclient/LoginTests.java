package com.kitchdevelopment.familymapclient;

import com.kitchdevelopment.familymapclient.cache.DataCache;
import com.kitchdevelopment.familymapclient.proxy.ServerProxy;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;

import Requests.LoginRequest;
import Results.LoginOrRegisterResult;

public class LoginTests {
	/*
		To run these tests you must load the server with the test data and
		start the server.
	*/
	DataCache dataCache = DataCache.getInstance();
	LoginRequest loginRequest;
	URL url;

	@Before
	public void init() {
		System.out.println("****Initializing Test****");
	}

	@After
	public void tearDown() {
		System.out.println("****Tear Down****");
	}

	@Test
	public void login_isSuccessful() throws IOException {
		loginRequest = new LoginRequest("sheila", "parker");
		url = new URL("http://localHost:8080/user/login");
		LoginOrRegisterResult result = ServerProxy.login(loginRequest, url);

		assert(result.isSuccess());
	}

	@Test
	public void login_userNotRegistered() throws IOException {
		loginRequest = new LoginRequest("Obi1Kenobi", "no such thing as luck");
		url = new URL("http://localHost:8080/user/login");
		LoginOrRegisterResult result = ServerProxy.login(loginRequest, url);

		assert(!result.isSuccess());
	}

	@Test
	public void login_badPassword() throws IOException {
		loginRequest = new LoginRequest("sheila", "no such thing as luck");
		url = new URL("http://localHost:8080/user/login");
		LoginOrRegisterResult result = ServerProxy.login(loginRequest, url);

		assert(!result.isSuccess());
	}
}
