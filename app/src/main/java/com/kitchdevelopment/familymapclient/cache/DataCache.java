package com.kitchdevelopment.familymapclient.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import Models.Event;
import Models.Person;
import Results.BatchEventResult;
import Results.BatchPersonResult;

public class DataCache {
	/*
		NOTE: Because of the large number of data members and methods in this class
		it breaks convention. Instead of getters/setters followed by other methods, it
		instead groups methods by purpose (e.g. Family related methods) and then by type
		Purpose --> Getters/Setters --> Other

		Data members are also grouped by purpose.
	*/

	private static final int FIRST_ANCESTOR_INDEX = 1;

	private static DataCache instance;

	//Family
	private final Map<String, Person> familyMembers = new HashMap<>();
	private final Set<Person> patrilinearMales = new HashSet<>();
	private final Set<Person> patrilinearFemales = new HashSet<>();
	private final Set<Person> matrilinearMales = new HashSet<>();
	private final Set<Person> matrilinearFemales = new HashSet<>();

	//Events
	private final ArrayList<String> eventTypes = new ArrayList<>();
	private final Map<String, Event> familyEvents = new HashMap<>();
	private final Map<String, ArrayList<Event>> eventsByPerson = new HashMap<>();
	private Map<String, Event> filteredFamilyEvents = new HashMap<>();
	private Map<String, ArrayList<Event>> filteredEventsByPerson = new HashMap<>();

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

	//User
	private Person userPerson;

	private DataCache() {};

	public static DataCache getInstance() {
		if (instance == null) {
			instance = new DataCache();
		}
		return instance;
	}

	//User Methods
	public Person getUserPerson() {
		return userPerson;
	}

	public void setUserPerson(Person person) {
		userPerson = person;
	}

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

	public boolean syncSuccess() {
		if (userPerson.getFirstName() != null) {
			return true;
		}
		return false;
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

	//Family methods
	public Map<String, Person> getFamilyMembersMap() {
		return familyMembers;
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

	public Person getPersonById(String personId) {
		return familyMembers.get(personId);
	}

	public ArrayList<Person> getChildren(String personId) {
		ArrayList<Person> children = new ArrayList<>();
		for (Person person : familyMembers.values()) {
			if (person.getFatherID() != null) {
				if (person.getFatherID().equals(personId)) {
					children.add(person);
				} else if (person.getMotherID() != null) {
					if (person.getMotherID().equals(personId)) {
						children.add(person);
					}
				}
			}
		}
		return children;
	}

	//Events
	public ArrayList<String> getEventTypes() {
		return eventTypes;
	}

	public Map<String, Event> getFamilyEventsMap() {
		return filteredFamilyEvents;
	}

	public void setFamilyEvents(ArrayList<Event> eventList) {
		for(Event event : eventList) {
			familyEvents.put(event.getEventID(), event);
			filteredFamilyEvents.put(event.getEventID(), event);
			if (eventsByPerson.containsKey(event.getPersonID())) {
				eventsByPerson.get(event.getPersonID()).add(event);
			} else {
				ArrayList<Event> eventArray = new ArrayList<>();
				eventArray.add(event);
				eventsByPerson.put(event.getPersonID(), eventArray);
				filteredEventsByPerson.put(event.getPersonID(), eventArray);
			}
		}
	}

	public Map<String, Event> getFilteredEvents() {
		return filteredFamilyEvents;
	}

	public Event getEventById(String eventId) {
		return filteredFamilyEvents.get(eventId);
	}

	public ArrayList<Event> getPersonEvents(String personId) {
		return filteredEventsByPerson.get(personId);
	}

	public Event getEarliestEvent(String personId) {
		ArrayList<Event> personEvents = getPersonEvents(personId);
		Event earliestEvent = null;

		if (personEvents != null) {
			for (Event event : personEvents) {
				if (earliestEvent == null) {
					earliestEvent = event;
				} else {
					if (event.getEventType().equalsIgnoreCase("birth")) {
						return event;
					} else if (event.getYear() < earliestEvent.getYear()) {
						earliestEvent = event;
					}
				}
			}
		}
		return earliestEvent;
	}

	public ArrayList<Event> getChronologicalPersonEvents(String personId) {
		ArrayList<Event> temp;
		if(filteredEventsByPerson.containsKey(personId)) {
			temp = new ArrayList<>(filteredEventsByPerson.get(personId));
		} else {
			temp = new ArrayList<>();
		}
		ArrayList<Event> sortedEvents = new ArrayList<Event>();
		while (!temp.isEmpty()) {
			Event earliestEvent = null;
			for (Event event : temp) {
				if (earliestEvent == null) {
					earliestEvent = event;
				} else {
					if (event.getEventType().equalsIgnoreCase("birth")) {
						earliestEvent = event;
					} else if (!event.getEventType().equalsIgnoreCase("death") && event.getYear() < earliestEvent.getYear()) {
						earliestEvent = event;
					}
				}
			}
			sortedEvents.add(earliestEvent);
			temp.remove(earliestEvent);
		}
		return sortedEvents;
	}

	public void filterEvents() {
		filteredFamilyEvents = new HashMap<>();
		filteredEventsByPerson = new HashMap<>();
		if (showFathersSideEvents && showMothersSideEvents && showMaleEvents && showFemaleEvents) {
			//No events are filtered out
			filteredFamilyEvents = new HashMap<>(familyEvents);
			filteredEventsByPerson = new HashMap<>(eventsByPerson);
		} else {
			if (showMaleEvents()) {
				if (userPerson.getGender() == 'm') {
					String userPersonId = userPerson.getPersonID();
					filteredEventsByPerson.put(userPersonId, eventsByPerson.get(userPersonId));
				} else {
					String spouseId = userPerson.getSpouseID();
					if (spouseId != null) {
						filteredEventsByPerson.put(spouseId, eventsByPerson.get(spouseId));
					}
				}
			}
			if (showFemaleEvents()) {
				if (userPerson.getGender() == 'f') {
					String userPersonId = userPerson.getPersonID();
					filteredEventsByPerson.put(userPersonId, eventsByPerson.get(userPersonId));
				} else {
					String spouseId = userPerson.getSpouseID();
					if (spouseId != null) {
						filteredEventsByPerson.put(spouseId, eventsByPerson.get(spouseId));
					}
				}
			}
			if (showFathersSideEvents) {
				if (showMaleEvents) {
					for (Person person : patrilinearMales) {
						filteredEventsByPerson.put(person.getPersonID(), eventsByPerson.get(person.getPersonID()));
					}
				}
				if (showFemaleEvents) {
					for (Person person : patrilinearFemales) {
						filteredEventsByPerson.put(person.getPersonID(), eventsByPerson.get(person.getPersonID()));
					}
				}
			}
			if (showMothersSideEvents) {
				if (showMaleEvents) {
					for (Person person : matrilinearMales) {
						filteredEventsByPerson.put(person.getPersonID(), eventsByPerson.get(person.getPersonID()));
					}
				}
				if (showFemaleEvents) {
					for (Person person : matrilinearFemales) {
						filteredEventsByPerson.put(person.getPersonID(), eventsByPerson.get(person.getPersonID()));
					}
				}
			}
			for (ArrayList<Event> events : filteredEventsByPerson.values()) {
				for (Event event : events) {
					filteredFamilyEvents.put(event.getEventID(), event);
				}
			}
		}
		if (selectedEvent == null || !filteredFamilyEvents.containsValue(selectedEvent.getEventID())) {
			selectedEvent = null;
		}
	}

	//Data Caching methods
	public void cachePersonData(BatchPersonResult personResults) {
		ArrayList<Person> persons = personResults.getData();

		userPerson = persons.get(0);
		familyMembers.put(userPerson.getPersonID(), userPerson);
		if (userPerson.getGender() == 'm') {
			patrilinearMales.add(userPerson);
			matrilinearMales.add(userPerson);
		} else {
			patrilinearFemales.add(userPerson);
			matrilinearFemales.add(userPerson);
		}

		for (int i = FIRST_ANCESTOR_INDEX; i < persons.size() / 2; ++i) {
			Person temp = persons.get(i);
			familyMembers.put(temp.getPersonID(), temp);
			if (temp.getGender() == 'm') {
				patrilinearMales.add(temp);
			} else {
				patrilinearFemales.add(temp);
			}
		}
		for (int i = persons.size() / 2; i < persons.size(); ++i) {
			Person temp = persons.get(i);
			familyMembers.put(temp.getPersonID(), temp);
			if (temp.getGender() == 'm') {
				matrilinearMales.add(temp);
			} else {
				matrilinearFemales.add(temp);
			}
		}
	}

	public void cacheEventData(BatchEventResult eventsResult) {
		ArrayList<Event> events = eventsResult.getData();
		for (int i = 0; i < events.size(); ++i) {
			Event temp = events.get(i);
			familyEvents.put(temp.getEventID(), temp);
			if (eventsByPerson.containsKey(temp.getPersonID())) {
				eventsByPerson.get(temp.getPersonID()).add(temp);
			} else {
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

	public void clear() {
		//Family
		familyMembers.clear();
		patrilinearMales.clear();
		patrilinearFemales.clear();
		matrilinearMales.clear();
		matrilinearFemales.clear();

		//Events
		familyEvents.clear();
		filteredFamilyEvents.clear();
		eventsByPerson.clear();
		filteredEventsByPerson.clear();
		eventTypes.clear();

		//Application State
		isLoggedIn = false;
		fromEventView = false;
		selectedEvent = null;

		//Settings
		showSpouseLines = true;
		showFamilyTreeLines = true;
		showLifeStoryLine = true;
		showFathersSideEvents = true;
		showMothersSideEvents = true;
		showMaleEvents = true;
		showFemaleEvents = true;

		//User
		userPerson = null;
	}
}
