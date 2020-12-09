package com.kitchdevelopment.familymapclient.cache;

import com.google.android.gms.maps.GoogleMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import Models.Event;
import Models.Person;
import Models.User;
import Results.BatchEventResult;
import Results.BatchPersonResult;

public class DataCache {

    private static final int FIRST_ANCESTOR_INDEX = 1;

    private static DataCache instance;
//    private AuthToken authToken;

    //User
//    private User user;
    private Person userPerson;

    //Application State
    boolean isLoggedIn = false;
    boolean fromEventView = false;
    Event selectedEvent = null;

    //Settings
    boolean showSpouseLines = true;
    boolean showFamilyTreeLines = true;
    boolean showLifeStoryLine = true;
    boolean showFathersSideEvents = true;
    boolean showMothersSideEvents = true;
    boolean showMaleEvents = true;
    boolean showFemaleEvents = true;

    //Family
    private final Map<String, Person> familyMembers = new HashMap<>();
    private final Set<Person> patrilinearMales = new HashSet<>();
    private final Set<Person> patrilinearFemales = new HashSet<>();
    private final Set<Person> matrilinearMales = new HashSet<>();
    private final Set<Person> matrilinearFemales = new HashSet<>();

    //Events
    private final ArrayList<String> eventTypes = new ArrayList<>();
    private final Map<String, Event> familyEvents = new HashMap<>();
    private Map<String, Event> filteredFamilyEvents = new HashMap<>();
    private final Map<String, ArrayList<Event>> eventsByPerson = new HashMap<>();
    private Map<String, ArrayList<Event>> filteredEventsByPerson = new HashMap<>();

    private DataCache() {};

    public static DataCache getInstance() {
        if(instance == null) {
            instance = new DataCache();
        }
        return instance;
    }

    //User Methods
    public Person getUserPerson() {
        return userPerson;
    }

//    public void setUser(User user) {
//        this.user = user;
//    }

    //Application State Methods
    public boolean isFromEventView() {
        return fromEventView;
    }

    public void setFromEventView(boolean fromEventView) {
        this.fromEventView = fromEventView;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
    }

    public Event getSelectedEvent() {
        return selectedEvent;
    }

    public void setSelectedEvent(Event selectedEvent) {
        this.selectedEvent = selectedEvent;
    }

    //Settings methods
    public boolean showFathersSideEvents() {
        return showFathersSideEvents;
    }

    public void setShowFathersSideEvents(boolean showFathersSideEvents) {
        this.showFathersSideEvents = showFathersSideEvents;
    }

    public boolean showMothersSideEvents() {
        return showMothersSideEvents;
    }

    public void setShowMothersSideEvents(boolean showMothersSideEvents) {
        this.showMothersSideEvents = showMothersSideEvents;
    }

    public boolean showMaleEvents() {
        return showMaleEvents;
    }

    public void setShowMaleEvents(boolean showMaleEvents) {
        this.showMaleEvents = showMaleEvents;
    }

    public boolean showFemaleEvents() {
        return showFemaleEvents;
    }

    public void setShowFemaleEvents(boolean showFemaleEvents) {
        this.showFemaleEvents = showFemaleEvents;
    }

    public boolean showSpouseLines() {
        return showSpouseLines;
    }

    public void setShowSpouseLines(boolean showSpouseLines) {
        this.showSpouseLines = showSpouseLines;
    }

    public boolean showFamilyTreeLines() {
        return showFamilyTreeLines;
    }

    public void setShowFamilyTreeLines(boolean showFamilyTreeLines) {
        this.showFamilyTreeLines = showFamilyTreeLines;
    }

    public boolean showLifeStoryLine() {
        return showLifeStoryLine;
    }

    public void setShowLifeStoryLine(boolean showLifeStoryLine) {
        this.showLifeStoryLine = showLifeStoryLine;
    }

//    public AuthToken getAuthToken() {
//        return authToken;
//    }
//
//    public void setAuthToken(AuthToken authToken) {
//        this.authToken = authToken;
//    }

    //Family methods
    public Map<String, Person> getFamilyMembersMap() {
        return familyMembers;
    }

    public Person getPersonById(String personId) {
        return familyMembers.get(personId);
    }

    //Events
    public ArrayList<String> getEventTypes() {
        return eventTypes;
    }

    public Map<String, Event> getFamilyEventsMap() {
        return filteredFamilyEvents;
    }

    public Event getEventById(String eventId) {
        return filteredFamilyEvents.get(eventId);
    }

    public ArrayList<Event> getPersonEvents(String personId) {
        return filteredEventsByPerson.get(personId);
    }

    public ArrayList<Event> getChronologicalPersonEvents(String personId) {
        ArrayList<Event> temp = new ArrayList<>(filteredEventsByPerson.get(personId));
        ArrayList<Event> sortedEvents = new ArrayList<Event>();
        while(!temp.isEmpty()) {
            Event earliestEvent = null;
            for(Event event : temp) {
                if(earliestEvent == null) {
                    earliestEvent = event;
                }
                else {
                    if(event.getEventType().equalsIgnoreCase("birth")) {
                        earliestEvent = event;
                    }
                    else if(!event.getEventType().equalsIgnoreCase("death") && event.getYear() < earliestEvent.getYear()) {
                        earliestEvent = event;
                    }
                }
            }
            sortedEvents.add(earliestEvent);
            temp.remove(earliestEvent);
        }
        return sortedEvents;
    }

    //Data Caching methods
    public void cachePersonData(BatchPersonResult personResults) {
        ArrayList<Person> persons = personResults.getData();
        userPerson = persons.get(0);
        familyMembers.put(userPerson.getPersonID(), userPerson);
        if(userPerson.getGender() == 'm') {
            patrilinearMales.add(userPerson);
            matrilinearMales.add(userPerson);
        }
        else {
            patrilinearFemales.add(userPerson);
            matrilinearFemales.add(userPerson);
        }
//        setUser(new User("","","", userPerson.getFirstName(), userPerson.getLastName(), userPerson.getGender(), userPerson.getPersonID()));
//        for(int i = 0; i < persons.size(); ++i) {
//            Person temp = persons.get(i);
//            familyMembers.put(temp.getPersonID(), temp);
//        }
        for(int i = FIRST_ANCESTOR_INDEX; i < persons.size() / 2; ++i) {
            Person temp = persons.get(i);
            familyMembers.put(temp.getPersonID(), temp);
            if(temp.getGender() == 'm') {
                patrilinearMales.add(temp);
            }
            else {
                patrilinearFemales.add(temp);
            }
        }
        for(int i = persons.size() / 2; i < persons.size(); ++i) {
            Person temp = persons.get(i);
            familyMembers.put(temp.getPersonID(), temp);
            if(temp.getGender() == 'm') {
                matrilinearMales.add(temp);
            }
            else {
                matrilinearFemales.add(temp);
            }
        }
    }

    public void cacheEventData(BatchEventResult eventsResult) {
        ArrayList<Event> events = eventsResult.getData();
        for (int i = 0 ; i < events.size(); ++i) {
            Event temp = events.get(i);
            familyEvents.put(temp.getEventID(), temp);
            if(eventsByPerson.containsKey(temp.getPersonID())) {
                eventsByPerson.get(temp.getPersonID()).add(temp);
            }
            else {
                ArrayList<Event> tempArray = new ArrayList<>();
                tempArray.add(temp);
                eventsByPerson.put(temp.getPersonID(), tempArray);
            }
            String eventType = temp.getEventType().toLowerCase();
            if (!eventTypes.contains(eventType)) {
                eventTypes.add(eventType);
            }
        }
        filteredFamilyEvents = new HashMap<>(familyEvents);
        filteredEventsByPerson = new HashMap<>(eventsByPerson);
    }

    public boolean syncSuccess() {
        if(userPerson.getFirstName() != null) {
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
                else if(person.getMotherID() != null) {
                    if(person.getMotherID().equals(personId)) {
                        children.add(person);
                    }
                }
            }
        }
        return children;
    }

    public Event getEarliestEvent(String personId) {
        ArrayList<Event> personEvents = getPersonEvents(personId);
        Event earliestEvent = null;

        if(personEvents != null) {
            for(Event event : personEvents) {
                if(earliestEvent == null) {
                    earliestEvent = event;
                }
                else {
                    if(event.getEventType().equalsIgnoreCase("birth")) {
                        return event;
                    }
                    else if(event.getYear() < earliestEvent.getYear()) {
                        earliestEvent = event;
                    }
                }
            }
        }
        return earliestEvent;
    }

    public void filterEvents() {
        filteredFamilyEvents = new HashMap<>();
        filteredEventsByPerson = new HashMap<>();
        if(showFathersSideEvents && showMothersSideEvents && showMaleEvents && showFemaleEvents) {
            //No events are filtered out
            filteredFamilyEvents = new HashMap<>(familyEvents);
            filteredEventsByPerson = new HashMap<>(eventsByPerson);
        }
        else {
            if(showMaleEvents()) {
                if(userPerson.getGender() == 'm') {
                    String userPersonId = userPerson.getPersonID();
                    filteredEventsByPerson.put(userPersonId, eventsByPerson.get(userPersonId));
                }
                else {
                    String spouseId = userPerson.getSpouseID();
                    if(spouseId != null) {
                        filteredEventsByPerson.put(spouseId, eventsByPerson.get(spouseId));
                    }
                }
            }
            if(showFemaleEvents()) {
                if(userPerson.getGender() == 'f') {
                    String userPersonId = userPerson.getPersonID();
                    filteredEventsByPerson.put(userPersonId, eventsByPerson.get(userPersonId));
                }
                else {
                    String spouseId = userPerson.getSpouseID();
                    if(spouseId != null) {
                        filteredEventsByPerson.put(spouseId, eventsByPerson.get(spouseId));
                    }
                }
            }
            if(showFathersSideEvents) {
                if(showMaleEvents) {
                   for(Person person : patrilinearMales) {
                       filteredEventsByPerson.put(person.getPersonID(), eventsByPerson.get(person.getPersonID()));
                   }
                }
                if(showFemaleEvents) {
                    for(Person person : patrilinearFemales) {
                        filteredEventsByPerson.put(person.getPersonID(), eventsByPerson.get(person.getPersonID()));
                    }
                }
            }
            if(showMothersSideEvents) {
                if(showMaleEvents) {
                    for(Person person : matrilinearMales) {
                        filteredEventsByPerson.put(person.getPersonID(), eventsByPerson.get(person.getPersonID()));
                    }
                }
                if(showFemaleEvents) {
                    for(Person person : matrilinearFemales) {
                        filteredEventsByPerson.put(person.getPersonID(), eventsByPerson.get(person.getPersonID()));
                    }
                }
            }
            for(ArrayList<Event> events : filteredEventsByPerson.values()) {
                for(Event event : events) {
                    filteredFamilyEvents.put(event.getEventID(), event);
                }
            }
        }
        if(selectedEvent == null || !filteredFamilyEvents.containsValue(selectedEvent.getEventID())) {
            selectedEvent = null;
        }
    }
}
