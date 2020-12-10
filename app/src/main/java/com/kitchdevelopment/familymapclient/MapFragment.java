package com.kitchdevelopment.familymapclient;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.kitchdevelopment.familymapclient.cache.DataCache;

import java.util.ArrayList;
import java.util.Map;

import Models.Event;
import Models.Person;

public class MapFragment extends Fragment implements OnMapReadyCallback {

	private static final String MAP_PROMPT = "Click on a marker to see event details";
	private static final int ICON_COLOR_FACTOR = 20;
	private static final int ICON_COLOR_MAX_VALUE = 360;
	private static final int DEFAULT_LINE_WIDTH = 8;
	private static final int FAMILY_LINE_WIDTH = 15;
	private static final int FAMILY_LINE_REDUCTION = 4;

	private GoogleMap mMap;
	private SupportMapFragment mapFragment;
	private DataCache dataCache = DataCache.getInstance();
	private Person selectedPerson;
	private Event selectedEvent;

	//View Elements
	ImageView genderIcon = null;
	TextView nameText = null;
	TextView eventText = null;
	ArrayList<Polyline> currentPolylines = new ArrayList<>();

	//Icons
	Drawable maleIcon;
	Drawable femaleIcon;
	Drawable androidIcon;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle bundle) {
		super.onCreateView(inflater, container, bundle);
		View v = inflater.inflate(R.layout.fragment_map, container, false);

		//View Elements
		genderIcon = v.findViewById(R.id.genderIcon);
		nameText = v.findViewById(R.id.name);
		eventText = v.findViewById(R.id.event);

		//Icons
		maleIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_male).
				colorRes(R.color.male_icon).sizeDp(40);
		femaleIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_female).
				colorRes(R.color.female_icon).sizeDp(40);
		androidIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_android)
				.colorRes(R.color.android_icon).sizeDp(40);

		//Info Button Listener
		final RelativeLayout eventDetails = (RelativeLayout) v.findViewById(R.id.eventLayout);
		eventDetails.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), PersonActivity.class);
				intent.putExtra("personId", selectedPerson.getPersonID());
				startActivity(intent);
			}
		});

		mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.familyMap);
		mapFragment.getMapAsync(this);
		return v;
	}

	@Override
	public void onResume() {
		if (selectedEvent != null) {
			//Set to null if the event was filtered out
			if (dataCache.getEventById(selectedEvent.getEventID()) == null) {
				selectedEvent = null;
				genderIcon.setImageDrawable(androidIcon);
				eventText.setText(MAP_PROMPT);
			}
		}
		if (mMap != null) {
			addMarkers(mMap);
		}
		if (selectedEvent != null) {
			drawLines();
		}
		super.onResume();
	}

	public void onSelectEvent(Event event) {
		selectedEvent = event;
		mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(event.getLatitude(), event.getLongitude())));
		Person person = dataCache.getPersonById(event.getPersonID());
		selectedPerson = person;

		if (person.getGender() == 'm') {
			genderIcon.setImageDrawable(maleIcon);
		} else {
			genderIcon.setImageDrawable(femaleIcon);
		}

		nameText.setText(person.getFirstName() + " " + person.getLastName());
		eventText.setText(event.getEventType() + ": " + event.getCity() + " " + event.getCountry() + "(" + event.getYear() + ")");
		drawLines();
	}

	private void clearLines() {
		for (Polyline line : currentPolylines) {
			line.remove();
		}
		currentPolylines.clear();
	}

	@Override
	public void onMapReady(GoogleMap googleMap) {
		mMap = googleMap;
		addMarkers(mMap);

		if (dataCache.isFromEventView()) {
			onSelectEvent(dataCache.getSelectedEvent());
			dataCache.setFromEventView(false);
		} else {
			genderIcon.setImageDrawable(androidIcon);
		}
	}

	private void drawLines() {
		if (currentPolylines != null) {
			clearLines();
		}
		if (selectedPerson != null && selectedEvent != null) {
			if (dataCache.showSpouseLines()) {
				if (selectedPerson.getSpouseID() != null) {
					LatLng currentEventPosition = new LatLng(selectedEvent.getLatitude(), selectedEvent.getLongitude());
					Event spouseEvent = dataCache.getEarliestEvent(selectedPerson.getSpouseID());
					if (spouseEvent != null) {
						LatLng spouseEventPosition = new LatLng(spouseEvent.getLatitude(), spouseEvent.getLongitude());

						PolylineOptions line = new PolylineOptions()
								.add(currentEventPosition, spouseEventPosition)
								.width(DEFAULT_LINE_WIDTH)
								.color(Color.RED);

						currentPolylines.add(mMap.addPolyline(line));
					}
				}
			}
			if (dataCache.showLifeStoryLine()) {
				ArrayList<Event> personEvents = dataCache.getChronologicalPersonEvents(selectedPerson.getPersonID());
				if (!personEvents.isEmpty() && personEvents.size() > 1) {
					PolylineOptions line = new PolylineOptions();
					for (int i = 0; i < personEvents.size() - 1; ++i) {
						Event currentEvent = personEvents.get(i);
						LatLng currentEventPosition = new LatLng(currentEvent.getLatitude(), currentEvent.getLongitude());
						Event nextEvent = personEvents.get(i + 1);
						LatLng nextEventPosition = new LatLng(nextEvent.getLatitude(), nextEvent.getLongitude());

						line.add(currentEventPosition, nextEventPosition)
								.width(DEFAULT_LINE_WIDTH)
								.color(Color.GREEN);

						currentPolylines.add(mMap.addPolyline(line));
					}
				}
			}

			if (dataCache.showFamilyTreeLines()) {
				createFamilyTreeLines(selectedPerson, FAMILY_LINE_WIDTH);
			}
		}
	}

	private void createFamilyTreeLines(Person person, int lineWidth) {
		PolylineOptions line = new PolylineOptions();
		Event childEvent = dataCache.getEarliestEvent(person.getPersonID());
		if (childEvent != null) {
			LatLng childEventPosition = new LatLng(childEvent.getLatitude(), childEvent.getLongitude());

			String fatherId = person.getFatherID();
			Person father = dataCache.getPersonById(fatherId);
			if (fatherId != null && father != null && dataCache.showMaleEvents()) {
				Event fatherEvent = dataCache.getEarliestEvent(fatherId);
				if (fatherEvent != null) {
					LatLng fatherEventPosition = new LatLng(fatherEvent.getLatitude(), fatherEvent.getLongitude());

					line.add(childEventPosition, fatherEventPosition)
							.width(lineWidth)
							.color(Color.BLUE);

					currentPolylines.add(mMap.addPolyline(line));
					createFamilyTreeLines(father, lineWidth - FAMILY_LINE_REDUCTION);
				}
			}

			String motherId = person.getMotherID();
			Person mother = dataCache.getPersonById(motherId);
			if (motherId != null && mother != null && dataCache.showFemaleEvents()) {
				Event motherEvent = dataCache.getEarliestEvent(motherId);
				if (motherEvent != null) {
					LatLng motherEventPosition = new LatLng(motherEvent.getLatitude(), motherEvent.getLongitude());

					line.add(childEventPosition, motherEventPosition)
							.width(lineWidth)
							.color(Color.BLUE);

					currentPolylines.add(mMap.addPolyline(line));
					createFamilyTreeLines(mother, lineWidth - FAMILY_LINE_REDUCTION);
				}
			}
		}
	}

	public void addMarkers(GoogleMap googleMap) {
		mMap = googleMap;
		mMap.clear();

		Map<String, Event> familyEvents = dataCache.getFamilyEventsMap();
		for (Map.Entry<String, Event> entry : familyEvents.entrySet()) {
			final Event temp = entry.getValue();
			int iconColor = dataCache.getEventTypes().indexOf(temp.getEventType().toLowerCase()) * ICON_COLOR_FACTOR;
			if (iconColor > ICON_COLOR_MAX_VALUE) {
				iconColor = iconColor - ICON_COLOR_MAX_VALUE;
			}
			mMap.addMarker(new MarkerOptions()
					.icon(BitmapDescriptorFactory.defaultMarker(iconColor))
					.title(temp.getEventID())
					.position(new LatLng(temp.getLatitude(), temp.getLongitude())));
			mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
				@Override
				public boolean onMarkerClick(Marker marker) {
					mMap.moveCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
					onSelectEvent(dataCache.getEventById(marker.getTitle()));
					return true;
				}
			});
		}
	}
}
