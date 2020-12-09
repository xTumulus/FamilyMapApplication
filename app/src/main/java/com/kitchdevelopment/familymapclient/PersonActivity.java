package com.kitchdevelopment.familymapclient;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.kitchdevelopment.familymapclient.cache.DataCache;
import com.kitchdevelopment.familymapclient.utils.FamilyMapListAdapter;
import com.kitchdevelopment.familymapclient.utils.ListItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Models.Event;
import Models.Person;

public class PersonActivity extends AppCompatActivity {
    private static final String MALE = "Male";
    private static final String FEMALE = "Female";

    Person person;
    DataCache dataCache = DataCache.getInstance();

    //View Elements
    TextView firstName = null;
    TextView lastName = null;
    TextView gender = null;

    //Expandable List View
    ExpandableListView personExpandableListView;
    FamilyMapListAdapter expandablelistAdapter;
    List<String> listDataHeaders;
    HashMap<String, List<ListItem>> listDataChildren;

    //Icons
    Drawable maleIcon;
    Drawable femaleIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Iconify.with(new FontAwesomeModule());
        setContentView(R.layout.activity_person);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String personId = getIntent().getStringExtra("personId");
        person = dataCache.getPersonById(personId);

        //View Elements
        firstName = findViewById(R.id.firstNameText);
        lastName = findViewById(R.id.lastNameText);
        gender = findViewById(R.id.genderText);
        personExpandableListView = (ExpandableListView) findViewById(R.id.personInfoListView);

        //Icons
        maleIcon = new IconDrawable(this, FontAwesomeIcons.fa_male).
                colorRes(R.color.male_icon).sizeDp(40);
        femaleIcon = new IconDrawable(this, FontAwesomeIcons.fa_female).
                colorRes(R.color.female_icon).sizeDp(40);

        //Set up Person View
        firstName.setText(person.getFirstName());
        lastName.setText(person.getLastName());
        char genderChar = person.getGender();
        if(genderChar == 'm') {
            gender.setText(MALE);
        }
        else {
            gender.setText(FEMALE);
        }

        //Set Up Expandable View
        prepareListData();
        expandablelistAdapter = new FamilyMapListAdapter(this, listDataHeaders, listDataChildren);
        personExpandableListView.setAdapter(expandablelistAdapter);
        personExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                if(groupPosition == 0) {
                    System.out.println("Clicked event");
                    ListItem item = (ListItem) expandablelistAdapter.getChild(groupPosition, childPosition);
                    Intent intent = new Intent(getApplicationContext(), EventActivity.class);
                    intent.putExtra("eventId", item.getId());
                    startActivity(intent);
                    return true;
                }
                if(groupPosition == 1) {
                    System.out.println("Clicked person");
                    ListItem item = (ListItem) expandablelistAdapter.getChild(groupPosition, childPosition);
                    Intent intent = new Intent(getApplicationContext(), PersonActivity.class);
                    intent.putExtra("personId", item.getId());
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });
    }

    private void prepareListData() {
        listDataHeaders = new ArrayList<String>();
        listDataChildren = new HashMap<String, List<ListItem>>();

        //Headers
        listDataHeaders.add("Life Events");
        listDataHeaders.add("Family");

        Map<String, ArrayList<String>> listData = new HashMap();

        //Events
        List<ListItem> lifeEvents = new ArrayList<ListItem>();
        ArrayList<Event> lifeEventsList = dataCache.getChronologicalPersonEvents(person.getPersonID());
        for(int i = 0; i < lifeEventsList.size(); ++i) {
            Event temp = lifeEventsList.get(i);
            Person tempPerson = dataCache.getPersonById(temp.getPersonID());
            ListItem item = new ListItem(temp.getEventID(),
                    temp.getEventType() + ": " + temp.getCity() + ", " + temp.getCountry()
                          + " (" + temp.getYear() + ")" + "\n" + tempPerson.getFirstName() + " " + tempPerson.getLastName());
            lifeEvents.add(item);
        }

        //Family
        List<ListItem> family = new ArrayList<ListItem>();
        Person father = dataCache.getPersonById(person.getFatherID());
        if(father != null) {
            family.add(new ListItem(father.getPersonID(), father.getFirstName() + " " + father.getLastName() + "\n" + "Father"));
        }
        Person mother = dataCache.getPersonById(person.getMotherID());
        if(mother != null) {
            family.add(new ListItem(mother.getPersonID(), mother.getFirstName() + " " + mother.getLastName() + "\n" + "Mother"));
        }
        Person spouse = dataCache.getPersonById(person.getSpouseID());
        if(spouse != null) {
            family.add(new ListItem(spouse.getPersonID(), spouse.getFirstName() + " " + spouse.getLastName() + "\n" + "Spouse"));
        }
        ArrayList<Person> children = dataCache.getChildren(person.getPersonID());
        for(int i = 0; i < children.size(); ++i) {
            Person child = children.get(i);
            family.add(new ListItem(child.getPersonID(), child.getFirstName() + " " + child.getLastName() + "\n" + "Child"));
        }

        listDataChildren.put(listDataHeaders.get(0), lifeEvents);
        listDataChildren.put(listDataHeaders.get(1), family);
    }
}
