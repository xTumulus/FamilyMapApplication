package com.kitchdevelopment.familymapclient.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kitchdevelopment.familymapclient.cache.DataCache;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class GsonSerializer {

    private static GsonSerializer instance;

    private GsonSerializer() {};

    public static GsonSerializer getInstance() {
        if(instance == null) {
            instance = new GsonSerializer();
        }
        return instance;
    }

    public static String serialize(Object input) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(input);
    }

    public static <T> T deserialize(String input, Class<T> returnType) throws IOException {
        return (new Gson()).fromJson(input, returnType);
    }
}
