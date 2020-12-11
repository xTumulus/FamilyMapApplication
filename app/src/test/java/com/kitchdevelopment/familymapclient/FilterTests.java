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

public class FilterTests {
	DataCache dataCache = DataCache.getInstance();
	Event eventChild;
	Event eventSpouse;
	Event eventMother;
	Event eventFather;
	Event eventGPF;
	Event eventGMF;
	Event eventGPM;
	Event eventGMM;
	Person child;
	Person spouse;
	Person father;
	Person mother;
	Person grandfatherM;
	Person grandmotherM;
	Person grandfatherF;
	Person grandmotherF;
	Map<String, Event> empty = new HashMap<>();

	@Before
	public void init() {
		System.out.println("****Initializing Test****");
		child = new Person("John_Wyles", "Johnny",
				"John", "Wyles", 'm', "",
				"Johnny_Mom", "River_Wyles");
		spouse = new Person("River_Wyles", "River",
				"River", "Wyles", 'f', "John_Man",
				"", "John_Wyles");
		father = new Person("Johnny_Dad", "Johnny",
				"John", "Wyles", 'm', "Johnny_Grandpa_F",
				"Johnny_Grandma_F", "Johnny_Mom");
		mother = new Person("Johnny_Mom", "Mommy",
				"Jane", "Doe", 'f', "Johnny_Grandpa_M",
				"Johnny_Grandma_M", "Johnny_Dad");
		grandfatherF = new Person("Johnny_Grandpa_F", "Johnny",
				"John", "Wyles", 'm', "",
				"", "Johnny_Grandma_F");
		grandmotherF = new Person("Johnny_Grandma_F", "Johnny",
				"Jane", "Unknown", 'f', "",
				"", "Johnny_Grandpa_F");
		grandfatherM = new Person("Johnny_Grandpa_M", "Johnny",
				"John", "Doe", 'm', "",
				"", "Johnny_Grandma_M");
		grandmotherM = new Person("Johnny_Grandma_M", "Johnny",
				"Jane", "Unknown", 'f', "",
				"", "Johnny_Grandpa_M");
		dataCache.getFamilyMembersMap().put(child.getPersonID(), child);
		dataCache.getFamilyMembersMap().put(spouse.getPersonID(), spouse);
		dataCache.getFamilyMembersMap().put(father.getPersonID(), father);
		dataCache.getFamilyMembersMap().put(mother.getPersonID(), mother);
		dataCache.getFamilyMembersMap().put(grandfatherF.getPersonID(), grandfatherF);
		dataCache.getFamilyMembersMap().put(grandmotherF.getPersonID(), grandmotherF);
		dataCache.getFamilyMembersMap().put(grandfatherM.getPersonID(), grandfatherM);
		dataCache.getFamilyMembersMap().put(grandmotherM.getPersonID(), grandmotherM);

		dataCache.getPatrilinearMales().add(child);
		dataCache.getPatrilinearMales().add(father);
		dataCache.getPatrilinearMales().add(grandfatherF);
		dataCache.getPatrilinearFemales().add(grandmotherF);

		dataCache.getMatrilinearMales().add(child);
		dataCache.getMatrilinearFemales().add(mother);
		dataCache.getMatrilinearMales().add(grandfatherM);
		dataCache.getMatrilinearFemales().add(grandmotherM);

		eventChild = new Event("Met_River", "Johnny",
				"John_Wyles", 48.4304, 123.4476,
				"Canada", "Colwood", "meet", 2015);
		eventSpouse = new Event("Met_River_Again", "Johnny",
				"River_Wyles", 48.4304, 123.4476,
				"Canada", "Colwood", "meet", 2025);
		eventMother = new Event("Jimmy_Dies_Mom", "Johnny",
				"Johnny_Mom", 48.4304, 123.4476,
				"Canada", "Colwood", "death", 2015);
		eventFather = new Event("Twins_Born_Dad", "Johnny",
				"Johnny_Dad", 48.4304, 123.4476,
				"Canada", "Colwood", "birth", 2010);
		eventGPF = new Event("Married_GPF", "Johnny",
				"Johnny_Grandpa_F", 48.4304, 123.4476,
				"Canada", "Colwood", "marriage", 2000);
		eventGMF = new Event("Married_GMF", "Johnny",
				"Johnny_Grandma_F", 48.4304, 123.4476,
				"Canada", "Colwood", "marriage", 2000);
		eventGPM = new Event("Married_GPM", "Johnny",
				"Johnny_Grandpa_M", 48.4304, 123.4476,
				"Canada", "Colwood", "marriage", 2000);
		eventGMM = new Event("Married_GMM", "Johnny",
				"Johnny_Grandma_M", 48.4304, 123.4476,
				"Canada", "Colwood", "marriage", 2000);

		ArrayList<Event> temp = new ArrayList<>();
		temp.add(eventChild);
		temp.add(eventSpouse);
		temp.add(eventFather);
		temp.add(eventMother);
		temp.add(eventGMF);
		temp.add(eventGMM);
		temp.add(eventGPF);
		temp.add(eventGPM);
		dataCache.setFamilyEvents(temp);
		dataCache.setUserPerson(child);
	}

	@After
	public void tearDown() {
		System.out.println("****Tear Down****");
		dataCache.clear();
	}

	@Test
	public void filterFathersSide() {
		dataCache.setShowFathersSideEvents(false);
		Map<String, Event> expected = new HashMap<>();
		expected.put(eventMother.getEventID(), eventMother);
		expected.put(eventChild.getEventID(), eventChild);
		expected.put(eventGPM.getEventID(), eventGPM);
		expected.put(eventSpouse.getEventID(), eventSpouse);
		expected.put(eventGMM.getEventID(), eventGMM);

		dataCache.filterEvents();
		Map<String, Event> test = dataCache.getFilteredEvents();

		assertEquals(expected, test);
	}

	@Test
	public void filterMothersSide() {
		dataCache.setShowMothersSideEvents(false);
		Map<String, Event> expected = new HashMap<>();
		expected.put(eventChild.getEventID(), eventChild);
		expected.put(eventSpouse.getEventID(), eventSpouse);
		expected.put(eventFather.getEventID(), eventFather);
		expected.put(eventGPF.getEventID(), eventGPF);
		expected.put(eventGMF.getEventID(), eventGMF);

		dataCache.filterEvents();
		Map<String, Event> test = dataCache.getFilteredEvents();

		assertEquals(expected, test);
	}

	@Test
	public void filterMales() {
		dataCache.setShowMaleEvents(false);
		Map<String, Event> expected = new HashMap<>();
		expected.put(eventSpouse.getEventID(), eventSpouse);
		expected.put(eventMother.getEventID(), eventMother);
		expected.put(eventGMF.getEventID(), eventGMF);
		expected.put(eventGMM.getEventID(), eventGMM);

		dataCache.filterEvents();
		Map<String, Event> test = dataCache.getFilteredEvents();

		assertEquals(expected, test);
	}

	@Test
	public void filterFemales() {
		dataCache.setShowFemaleEvents(false);
		Map<String, Event> expected = new HashMap<>();
		expected.put(eventChild.getEventID(), eventChild);
		expected.put(eventFather.getEventID(), eventFather);
		expected.put(eventGPF.getEventID(), eventGPF);
		expected.put(eventGPM.getEventID(), eventGPM);

		dataCache.filterEvents();
		Map<String, Event> test = dataCache.getFilteredEvents();

		assertEquals(expected, test);
	}

	@Test
	public void filterMotherFather() {
		dataCache.setShowMothersSideEvents(false);
		dataCache.setShowFathersSideEvents(false);
		Map<String, Event> expected = new HashMap<>();
		expected.put(eventChild.getEventID(), eventChild);
		expected.put(eventSpouse.getEventID(), eventSpouse);

		dataCache.filterEvents();
		Map<String, Event> test = dataCache.getFilteredEvents();

		assertEquals(expected, test);
	}

	@Test
	public void filterMaleFemale() {
		dataCache.setShowFemaleEvents(false);
		dataCache.setShowMaleEvents(false);

		dataCache.filterEvents();
		Map<String, Event> test = dataCache.getFilteredEvents();

		assertEquals(empty, test);
	}
}
