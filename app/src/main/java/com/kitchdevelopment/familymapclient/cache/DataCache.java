package com.kitchdevelopment.familymapclient.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import Models.AuthToken;
import Models.Event;
import Models.Person;
import Models.User;
import Results.BatchEventResult;
import Results.BatchPersonResult;

public class DataCache {

    private static final int FIRST_ANCESTOR_INDEX = 1;
    private static final int LAST_PATRILINEAR_ANCESTOR_INDEX = 15;
    private static final int LAST_MATRILINEAR_ANCESTOR_INDEX = 1;

    private static DataCache instance;
    private AuthToken authToken;

    //User
    private User user;
    boolean isLoggedIn = false;

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
    }

    //People
    private final Map<String, Person> familyMembers = new HashMap<>();

    //Immediate Family
    private final Set<Person> immediateMales = new HashSet<>();
    private final Set<Person> immediateFemales = new HashSet<>();

    //Ancestral Family
    private final Set<Person> patrilinearMales = new HashSet<>();
    private final Set<Person> patrilinearFemales = new HashSet<>();
    private final Set<Person> matrilinearMales = new HashSet<>();
    private final Set<Person> matrilinearFemales = new HashSet<>();

    //Events
    private final ArrayList<String> eventTypes = new ArrayList<>();
    private final Map<String, Event> familyEvents = new HashMap<>();
    private final Map<String, ArrayList<Event>> familyEventsByPerson = new HashMap<>();

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

    public Map<String, Event> getFamilyEventsMap() {
        return familyEvents;
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

    public ArrayList<String> getEventTypes() {
        return eventTypes;
    }

    public Map<String, Person> getFamilyMembersMap() {
        return familyMembers;
    }

    public Person getPersonById(String personId) {
        return familyMembers.get(personId);
    }

    public Event getEventById(String eventId) {
        return familyEvents.get(eventId);
    }

    public ArrayList<Event> getPersonEvents(String personId) {
        return familyEventsByPerson.get(personId);
    }

    public void cachePersonData(BatchPersonResult personResults) {
        ArrayList<Person> persons = personResults.getData();
        Person userPerson = persons.get(0);
        setUser(new User("","","", userPerson.getFirstName(), userPerson.getLastName(), userPerson.getGender(), userPerson.getPersonID()));
        for(int i = 0; i < persons.size(); ++i) {
            Person temp = persons.get(i);
            familyMembers.put(temp.getPersonID(), temp);
        }
//        for(int i = FIRST_ANCESTOR_INDEX; i < LAST_PATRILINEAR_ANCESTOR_INDEX; ++i) {
//            Person temp = persons.get(i);
//            if(temp.getGender() == 'm') {
//                patrilinearMales.add(temp);
//            }
//            else {
//                patrilinearFemales.add(temp);
//            }
//        }
//        for(int i = LAST_PATRILINEAR_ANCESTOR_INDEX + 1; i < LAST_MATRILINEAR_ANCESTOR_INDEX; ++i) {
//            Person temp = persons.get(i);
//            if(temp.getGender() == 'm') {
//                matrilinearMales.add(temp);
//            }
//            else {
//                matrilinearFemales.add(temp);
//            }
//        }
    }

    public void cacheEventData(BatchEventResult eventsResult) {
        ArrayList<Event> events = eventsResult.getData();
        for (int i = 0 ; i < events.size(); ++i) {
            Event temp = events.get(i);
            familyEvents.put(temp.getEventID(), temp);
            if(familyEventsByPerson.containsKey(temp.getPersonID())) {
                familyEventsByPerson.get(temp.getPersonID()).add(temp);
            }
            else {
                ArrayList<Event> tempArray = new ArrayList<>();
                tempArray.add(temp);
                familyEventsByPerson.put(temp.getPersonID(), tempArray);
            }
            String eventType = temp.getEventType().toLowerCase();
            if (!eventTypes.contains(eventType)) {
                eventTypes.add(eventType);
            }
        }
        System.out.println(familyEvents.toString());
    }

    public boolean syncSuccess() {
        if(user.getFirstName() != null) {
            return true;
        }
        return false;
    }

    public ArrayList<Person> getChildren(String personId) {
        ArrayList<Person> children = new ArrayList<>();
        for(Person person : familyMembers.values()) {
            if(person.getFatherID() != null) {
                if(person.getFatherID().equals(personId)) {
                    children.add(person);
                }
            }
            else if(person.getMotherID() != null) {
                if(person.getMotherID().equals(personId)) {
                    children.add(person);
                }
            }
        }
        return children;
    }
}
