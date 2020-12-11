package com.kitchdevelopment.familymapclient;

import com.kitchdevelopment.familymapclient.cache.DataCache;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import Models.Event;

import static org.junit.Assert.assertEquals;

public class ChronologicalEventTests {
	DataCache dataCache = DataCache.getInstance();
	String personId;
	String wrongPersonId;
	Event event1;
	Event event2;
	Event event3;
	Event event4;
	Event event5;
	ArrayList<Event> chronologicalList;
	ArrayList<Event> empty = new ArrayList<>();

	@Before
	public void init() {
		System.out.println("****Initializing Test****");
		personId = "John_Wyles";
		wrongPersonId= "River_Wyles";
		event1 = new Event("Met_River", "Johnny",
				"John_Wyles", 48.4304, 123.4476,
				"Canada", "Colwood", "meet", 2015);
		event2 = new Event("Met_River_Again", "Johnny",
				"John_Wyles", 48.4304, 123.4476,
				"Canada", "Colwood", "meet", 2025);
		event3 = new Event("Learned_Condition", "Johnny",
				"John_Wyles", 48.4304, 123.4476,
				"Canada", "Colwood", "medical", 2030);
		event4 = new Event("River_Passes", "Johnny",
				"John_Wyles", 48.4304, 123.4476,
				"Canada", "Colwood", "meet", 2055);
		event5 = new Event("Johnny_Passes", "Johnny",
				"John_Wyles", 48.4304, 123.4476,
				"Canada", "Colwood", "meet", 2058);
		ArrayList<Event> temp = new ArrayList<>();
		temp.add(event1);
		temp.add(event2);
		temp.add(event3);
		temp.add(event4);
		temp.add(event5);
		dataCache.setFamilyEvents(temp);

		chronologicalList = new ArrayList<>();
		chronologicalList.add(event1);
		chronologicalList.add(event2);
		chronologicalList.add(event3);
		chronologicalList.add(event4);
		chronologicalList.add(event5);
	}

	@After
	public void tearDown() {
		System.out.println("****Tear Down****");
		dataCache.clear();
	}

	@Test
	public void getChronologicalEvents() {
		ArrayList<Event> test = dataCache.getChronologicalPersonEvents(personId);
		assertEquals(chronologicalList, test);
	}

	@Test
	public void getEmptyChronologicalEvents() {
		ArrayList<Event> test = dataCache.getChronologicalPersonEvents(wrongPersonId);
		assertEquals(empty, test);
	}
}

