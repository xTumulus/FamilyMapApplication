package com.kitchdevelopment.familymapclient;

import com.kitchdevelopment.familymapclient.cache.DataCache;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import Models.Event;
import Models.Person;

public class PeopleEventDataTests {
	DataCache dataCache = DataCache.getInstance();
	Person person;
	Event event1;
	Event event2;
	ArrayList<Event> eventList;

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
	}

	@After
	public void tearDown() {
		System.out.println("****Tear Down****");
		person = null;
		event1 = null;
		event2 = null;
		eventList = null;
		dataCache.clear();
	}

	@Test
	public void getPerson() {
		Person test = dataCache.getPersonById("Adrian_Tepes");
		assertEquals(person, test);
	}

	@Test
	public void getNonexistentPerson() {
		Person test = dataCache.getPersonById("Count_Dracula");
		assertNull(test);
	}

	@Test
	public void getEventByEventId() {
		Event test;
		test = dataCache.getEventById("Arrived_Castle");
		assertEquals(event1, test);
	}

	@Test
	public void getPersonEvents() {
		ArrayList<Event> test = dataCache.getPersonEvents("Adrian_Tepes");
		assertEquals(eventList, test);
	}

	@Test
	public void getEarliestEvent() {
		Event test = dataCache.getEarliestEvent("Adrian_Tepes");
		assertEquals(event1, test);
	}

	@Test
	public void getNonexistentEventByEventId() {
		Event test = dataCache.getEventById("Game_Over");
		assertNull(test);
	}

	@Test
	public void getNonexistentPersonsEvents() {
		ArrayList<Event> test = dataCache.getPersonEvents("Count_Dracula");
		assertNull(test);
	}
}
