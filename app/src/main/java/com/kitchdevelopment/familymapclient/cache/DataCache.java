package com.kitchdevelopment.familymapclient.cache;

import java.util.ArrayList;
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

    public AuthToken getAuthToken() {
        return authToken;
    }

    public void setAuthToken(AuthToken authToken) {
        this.authToken = authToken;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Person> getImmediateMales() {
        return immediateMales;
    }

    public Set<Person> getImmediateFemales() {
        return immediateFemales;
    }

    public Set<Person> getPatrilinearMales() {
        return patrilinearMales;
    }

    public Set<Person> getPatrilinearFemales() {
        return patrilinearFemales;
    }

    public Set<Person> getMatrilinearMales() {
        return matrilinearMales;
    }

    public Set<Person> getMatrilinearFemales() {
        return matrilinearFemales;
    }

    public Set<String> getEventTypes() {
        return eventTypes;
    }

    public void cachePersonData(BatchResult people) {
        ArrayList<Person> persons = people.getData();
        Person userPerson = persons.get(0);
        user.setFirstName(userPerson.getFirstName());
        user.setLastName(userPerson.getLastName());
        user.setGender(userPerson.getGender());
        user.setPersonId(userPerson.getPersonID());
        System.out.println(people.toString());
    }

    public void cacheEventData(BatchResult events) {
        System.out.println(events.toString());
    }
}
