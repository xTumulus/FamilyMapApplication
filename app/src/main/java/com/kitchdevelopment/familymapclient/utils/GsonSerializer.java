package com.kitchdevelopment.familymapclient.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

public class GsonSerializer {

	private GsonSerializer() {};

	public static String serialize(Object input) throws IOException {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		return gson.toJson(input);
	}

	public static <T> T deserialize(String input, Class<T> returnType) throws IOException {
		return (new Gson()).fromJson(input, returnType);
	}
}
