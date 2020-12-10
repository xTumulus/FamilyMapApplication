package com.kitchdevelopment.familymapclient;

import com.kitchdevelopment.familymapclient.cache.DataCache;
import com.kitchdevelopment.familymapclient.proxy.ServerProxy;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;

import Requests.RegisterRequest;
import Results.LoginOrRegisterResult;

public class RegisterTests {
	/*
		To run these tests you must load the server with the test data and
		start the server.
	*/
	DataCache dataCache = DataCache.getInstance();
	RegisterRequest registerRequest;
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
	public void register_isSuccessful() throws IOException {
		registerRequest = new RegisterRequest("SammyGirl", "metroid", "samus@bountyhunters.com", "Samus", "Aran", 'f');
		url = new URL("http://localHost:8080/user/register");
		LoginOrRegisterResult result = ServerProxy.register(registerRequest, url);

		assert(result.isSuccess());
	}

	@Test
	public void register_userAlreadyRegistered() throws IOException {
		registerRequest = new RegisterRequest("sheila", "parker", "", "", "", 'f');
		url = new URL("http://localHost:8080/user/register");
		LoginOrRegisterResult result = ServerProxy.register(registerRequest, url);

		assert(!result.isSuccess());
	}
}
