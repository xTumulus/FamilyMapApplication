package com.kitchdevelopment.familymapclient.cache;

import java.util.HashSet;
import java.util.Set;

import Models.AuthToken;
import Models.Person;
import Models.User;
import Results.EventResult;
import Results.PersonResult;

public class DataCache {

    private static DataCache instance;
    private AuthToken authToken;

    //User
    private User user;

    //Immediate Family
    private final Set<Person> immediateMales = new HashSet<>();
    private final Set<Person> immediateFemales = new HashSet<>();

    //Ancestral Family
    private final Set<Person> patrilinearMales = new HashSet<>();
    private final Set<Person> patrilinearFemales = new HashSet<>();
    private final Set<Person> matrilinearMales = new HashSet<>();
    private final Set<Person> matrilinearFemales = new HashSet<>();

    private final Set<String> eventTypes = new HashSet<>();

    private DataCache() {};

    public static DataCache getInstance() {
        if(instance == null) {
            instance = new DataCache();
        }
        return instance;
    }

    public void cacheData(PersonResult people, EventResult events) {
        //Get the data from the server proxy and fill the variables above
    }
}
