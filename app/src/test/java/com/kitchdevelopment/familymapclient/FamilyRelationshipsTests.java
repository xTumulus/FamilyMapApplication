package com.kitchdevelopment.familymapclient;

import com.kitchdevelopment.familymapclient.cache.DataCache;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import Models.Person;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class FamilyRelationshipsTests {
	DataCache dataCache = DataCache.getInstance();
	Person person;
	Person father;
	Person mother;
	Person grandfatherM;
	Person grandmotherF;
	ArrayList<Person> children;
	ArrayList<Person> empty = new ArrayList<>();

	@Before
	public void init() {
		System.out.println("****Initializing Test****");
		person = new Person("Adrian_Tepes", "AlucardSOTN",
				"Adrian", "Tepes", 'm', "Count_Dracula",
				"Lisa_Human", "");
		father = new Person("Count_Dracula", "DraculaCV",
				"Vlad", "Tepes", 'm', "",
				"Dracula_Mom", "Lisa_Human");
		mother = new Person("Lisa_Human", "LisaCV",
				"Lisa", "Tepes", 'f', "John_Man",
				"", "Count_Dracula");
		grandfatherM = new Person("John_Man", "Johnny",
				"John", "Doe", 'm', "",
				"", "");
		grandmotherF = new Person("Dracula_Mom", "Mommy",
				"Jane", "Cronqvist", 'f', "",
				"", "");
		dataCache.getFamilyMembersMap().put(person.getPersonID(), person);
		dataCache.getFamilyMembersMap().put(father.getPersonID(), father);
		dataCache.getFamilyMembersMap().put(mother.getPersonID(), mother);
		dataCache.getFamilyMembersMap().put(grandfatherM.getPersonID(), grandfatherM);
		dataCache.getFamilyMembersMap().put(grandmotherF.getPersonID(), grandmotherF);
		children = new ArrayList<>();
		children.add(person);
	}

	@After
	public void tearDown() {
		System.out.println("****Tear Down****");
		dataCache.clear();
	}

	@Test
	public void getParents() {
		Person fTest = dataCache.getPersonById(person.getFatherID());
		Person mTest = dataCache.getPersonById(person.getMotherID());

		assertEquals(father, fTest);
		assertEquals(mother, mTest);
	}

	@Test
	public void getParentsEmpty() {
		Person fTest = dataCache.getPersonById(grandfatherM.getFatherID());
		Person mTest = dataCache.getPersonById(grandfatherM.getMotherID());

		assertNull(fTest);
		assertNull(mTest);
	}

	@Test
	public void getParentsFatherEmpty() {
		Person fTest = dataCache.getPersonById(father.getFatherID());
		Person mTest = dataCache.getPersonById(father.getMotherID());

		assertNull(fTest);
		assertEquals(grandmotherF, mTest);
	}

	@Test
	public void getParentsMotherEmpty() {
		Person fTest = dataCache.getPersonById(mother.getFatherID());
		Person mTest = dataCache.getPersonById(mother.getMotherID());

		assertEquals(grandfatherM, fTest);
		assertNull(mTest);
	}

	@Test
	public void getChildren() {
		ArrayList<Person> fTest = dataCache.getChildren(father.getPersonID());
		ArrayList<Person> mTest = dataCache.getChildren(mother.getPersonID());

		assertEquals(children, fTest);
		assertEquals(children, mTest);
	}

	@Test
	public void getChildrenEmpty() {
		ArrayList<Person> test = dataCache.getChildren(person.getPersonID());
		assertEquals(empty, test);
	}

	@Test
	public void getSpouse() {
		Person fTest = dataCache.getPersonById(father.getSpouseID());
		Person mTest = dataCache.getPersonById(mother.getSpouseID());

		assertEquals(mother, fTest);
		assertEquals(father, mTest);
	}

	@Test
	public void getSpouseEmpty() {
		Person test = dataCache.getPersonById(person.getSpouseID());
		assertNull(test);
	}

}
