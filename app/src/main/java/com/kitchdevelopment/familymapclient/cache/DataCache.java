package com.kitchdevelopment.familymapclient.cache;

import java.util.HashSet;
import java.util.Set;

import Models.AuthToken;
import Models.Person;
import Models.User;
import Results.BatchResult;
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

    public void cachePersonData(BatchResult people) {
        System.out.println(people.toString());
    }

    public void cacheEventData(BatchResult events) {
        System.out.println(events.toString());
    }
}
