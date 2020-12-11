package com.kitchdevelopment.familymapclient;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.kitchdevelopment.familymapclient.cache.DataCache;

import java.util.ArrayList;
import java.util.List;

import Models.Event;
import Models.Person;

public class SearchActivity extends AppCompatActivity {

	private static final int PERSON_VIEW_TYPE = 0;
	private static final int EVENT_VIEW_TYPE = 1;

	DataCache dataCache = DataCache.getInstance();

	String searchString;
	ArrayList<Person> peopleSearchResults;
	ArrayList<Event> eventSearchResults;

	//View Elements
	ImageView searchIconView;
	EditText searchBar;
	ImageView searchCancelButton;
	RecyclerView recyclerView;

	//Recycler View Elements
	FamilyMapSearchAdapter adapter;

	//Icons
	Drawable searchIcon;
	Drawable cancelIcon;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		//View Elements
		searchIconView = findViewById(R.id.search_bar_icon);
		searchBar = findViewById(R.id.search_bar);
		searchCancelButton = findViewById(R.id.search_cancel_button);
		recyclerView = (RecyclerView) findViewById(R.id.searchListView);

		//Set Icons
		searchIcon = new IconDrawable(this, FontAwesomeIcons.fa_search)
				.colorRes(R.color.black).sizeDp(30);
		cancelIcon = new IconDrawable(this, FontAwesomeIcons.fa_times)
				.colorRes(R.color.black).sizeDp(30);
		searchIconView.setImageDrawable(searchIcon);
		searchCancelButton.setImageDrawable(cancelIcon);

		//Initialize data
		eventSearchResults = new ArrayList<>();
		peopleSearchResults = new ArrayList<>();

		adapter = new FamilyMapSearchAdapter(peopleSearchResults, eventSearchResults);
		recyclerView.setAdapter(adapter);
		recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

		//Text listener
		TextWatcher searchStringWatcher = new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {};

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {};

			@Override
			public void afterTextChanged(Editable s) {
				searchString = searchBar.getText().toString().toLowerCase();
				if (searchString != "") {
					eventSearchResults = searchEvents();
					peopleSearchResults = searchPeople();
					adapter = new FamilyMapSearchAdapter(peopleSearchResults, eventSearchResults);
					recyclerView.setAdapter(adapter);
					recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
				}
			}
		};
		searchBar.addTextChangedListener(searchStringWatcher);

		//Button Listener
		searchCancelButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				searchBar.setText("");
			}
		});
	}

	private ArrayList<Person> searchPeople() {
		ArrayList<Person> peopleFound = new ArrayList<>();
		for (Person person : dataCache.getFamilyMembersMap().values()) {
			if (person.getFirstName().toLowerCase().contains(searchString)
					|| person.getLastName().toLowerCase().contains(searchString)) {
				peopleFound.add(person);
			}
		}
		return peopleFound;
	}

	private ArrayList<Event> searchEvents() {
		ArrayList<Event> eventsFound = new ArrayList<>();
		for (Event event : dataCache.getFamilyEventsMap().values()) {
			if (event.getCountry().toLowerCase().contains(searchString)
					|| event.getCity().toLowerCase().contains(searchString)
					|| event.getEventType().toLowerCase().contains(searchString)
					|| Integer.toString(event.getYear()).contains(searchString)) {
				eventsFound.add(event);
			}
		}
		return eventsFound;
	}

	//RecyclerView Sub Classes
	private class FamilyMapSearchAdapter extends RecyclerView.Adapter<FamilyMapSearchAdapter.ViewHolder> {

		private final List<Person> personList;
		private final List<Event> eventList;

		FamilyMapSearchAdapter(List<Person> personList, List<Event> eventList) {
			this.personList = personList;
			this.eventList = eventList;
		}

		@NonNull
		@Override
		public FamilyMapSearchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
			LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
			View v = inflater.inflate(R.layout.search_result, parent, false);

			return new FamilyMapSearchAdapter.ViewHolder(v, viewType);
		}

		@Override
		public void onBindViewHolder(@NonNull FamilyMapSearchAdapter.ViewHolder holder, int position) {
			if (position < personList.size()) {
				holder.bind(personList.get(position));
			} else {
				holder.bind(eventList.get(position - personList.size()));
			}
		}

		@Override
		public int getItemViewType(int position) {
			return position < personList.size() ? PERSON_VIEW_TYPE : EVENT_VIEW_TYPE;
		}

		@Override
		public int getItemCount() {
			return personList.size() + eventList.size();
		}

		private class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

			private final int viewType;
			private final ImageView icon;
			private final TextView title;
			private final TextView description;
			Drawable maleIcon;
			Drawable femaleIcon;
			Drawable eventIcon;
			private Person person;
			private Event event;

			ViewHolder(@NonNull View itemView, int viewType) {
				super(itemView);
				this.viewType = viewType;

				//Icons
				maleIcon = new IconDrawable(getApplicationContext(), FontAwesomeIcons.fa_male).
						colorRes(R.color.male_icon).sizeDp(40);
				femaleIcon = new IconDrawable(getApplicationContext(), FontAwesomeIcons.fa_female).
						colorRes(R.color.female_icon).sizeDp(40);
				eventIcon = new IconDrawable(getApplicationContext(), FontAwesomeIcons.fa_map_marker)
						.colorRes(R.color.black).sizeDp(40);

				itemView.setOnClickListener(this);

				icon = (ImageView) itemView.findViewById(R.id.search_result_icon);
				title = (TextView) itemView.findViewById(R.id.search_result_title);
				description = (TextView) itemView.findViewById(R.id.search_result_description);
			}

			void bind(Person person) {
				this.person = person;
				title.setText(person.getFirstName() + " " + person.getLastName());
				String gender;
				if (person.getGender() == 'm') {
					gender = "Male";
					icon.setImageDrawable(maleIcon);
				} else {
					gender = "Female";
					icon.setImageDrawable(femaleIcon);
				}
				description.setText(gender);
			}

			void bind(Event event) {
				icon.setImageDrawable(eventIcon);
				this.event = event;
				title.setText(event.getEventType() + ": " + event.getCity() + " " + event.getCountry() + "(" + event.getYear() + ")");
				Person person = dataCache.getPersonById(event.getPersonID());
				description.setText(person.getFirstName() + " " + person.getLastName());
			}

			@Override
			public void onClick(View v) {
				Intent intent;
				if (viewType == PERSON_VIEW_TYPE) {
					intent = new Intent(getApplicationContext(), PersonActivity.class);
					intent.putExtra("personId", person.getPersonID());
				} else {
					intent = new Intent(getApplicationContext(), EventActivity.class);
					intent.putExtra("eventId", event.getEventID());
				}
				getApplicationContext().startActivity(intent);
			}
		}
	}
}
