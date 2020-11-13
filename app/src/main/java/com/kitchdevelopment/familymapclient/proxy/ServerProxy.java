package com.kitchdevelopment.familymapclient.proxy;

import java.util.HashSet;

import Models.AuthToken;
import Models.User;
import Requests.LoginRequest;
import Requests.RegisterRequest;
import Results.EventResult;
import Results.LoginOrRegisterResult;
import Results.PersonResult;

public class ServerProxy {
    public LoginOrRegisterResult register(RegisterRequest request) {
        return null;
    }

    public LoginOrRegisterResult login(LoginRequest request) {
        return null;
    }

    public PersonResult getPeople(AuthToken token) {
        return null;
    }

    public EventResult getEvents(AuthToken token) {
        return null;
    }
}
