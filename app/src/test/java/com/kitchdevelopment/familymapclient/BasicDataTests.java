package com.kitchdevelopment.familymapclient;

import com.kitchdevelopment.familymapclient.cache.DataCache;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Models.Event;
import Models.Person;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class BasicDataTests {
	DataCache dataCache = DataCache.getInstance();
	Person person;
	Event event1;
	Event event2;
	ArrayList<Event> eventList;
	HashMap<String, Person> personMap;
	HashMap<String, Event> eventMap;
	HashMap<String, Event> emptyMap = new HashMap<>();


	@Before
	public void init() {
		System.out.println("****Initializing Test****");
		person = new Person("Adrian_Tepes", "AlucardSOTN",
				"Adrian", "Tepes", 'm', "Count_Dracula",
				"Lisa_Human", "");
		event1 = new Event("Arrived_Castle", "AlucardSOTN",
				"Adrian_Tepes", 46.1841, 25.2224,
				"Romania", "Transylvania", "NewGame", 1797);
		event2 = new Event("Defeated_Dracula", "AlucardSOTN",
				"Adrian_Tepes", 46.1841, 25.2224,
				"Romania", "Transylvania", "BeatGame", 1797);
		eventList = new ArrayList<>();
		eventList.add(event1);
		eventList.add(event2);
		dataCache.getFamilyMembersMap().put(person.getPersonID(), person);
		dataCache.setFamilyEvents(eventList);
		personMap = new HashMap<>();
		personMap.put(person.getPersonID(), person);
		eventMap = new HashMap<>();
		eventMap.put(event1.getEventID(), event1);
		eventMap.put(event2.getEventID(), event2);
	}

	@After
	public void tearDown() {
		System.out.println("****Tear Down****");
		person = null;
		event1 = null;
		event2 = null;
		eventList = null;
		dataCache.getFamilyMembersMap().clear();
		dataCache.getFamilyEventsMap().clear();
	}

	@Test
	public void getPeopleData() {
		Map test = dataCache.getFamilyMembersMap();
		assertEquals(personMap, test);
	}

	@Test
	public void getEventData() {
		Map test = dataCache.getFamilyEventsMap();
		assertEquals(eventMap, test);
	}

	@Test
	public void handleEmptyPeopleData() {
		dataCache.clear();
		Map test = dataCache.getFamilyMembersMap();
		assertEquals(emptyMap, test);
	}

	@Test
	public void handleEmptyEventData() {
		dataCache.clear();
		Map test = dataCache.getFamilyEventsMap();
		assertEquals(emptyMap, test);
	}
}
