package com.kitchdevelopment.familymapclient;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
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
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.kitchdevelopment.familymapclient.cache.DataCache;

import java.util.ArrayList;
import java.util.Map;

import Models.Event;
import Models.Person;
import Models.User;

public class MapFragment extends Fragment implements OnMapReadyCallback {
    private GoogleMap mMap;
    private SupportMapFragment mapFragment;
    private DataCache dataCache = DataCache.getInstance();
    private Person selectedPerson;

    private static final int ICON_COLOR_FACTOR = 20;
    private static final int ICON_COLOR_MAX_VALUE = 360;

    //View Elements
    ImageView genderIcon = null;
    TextView nameText = null;
    TextView eventText = null;

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
    public void onResume()
    {
        super.onResume();
    }

    public void onSelectEvent(Event event) {
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(event.getLatitude(), event.getLongitude())));
        Person person = dataCache.getPersonById(event.getPersonID());
        selectedPerson = person;

        if(person.getGender() == 'm') {
            genderIcon.setImageDrawable(maleIcon);
        }
        else {
            genderIcon.setImageDrawable(femaleIcon);
        }

        nameText.setText(person.getFirstName() + " " + person.getLastName());
        eventText.setText(event.getEventType() + ": " + event.getCity() + " " + event.getCountry() + "(" + event.getYear() + ")");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        Map<String, Event> familyEvents = dataCache.getFamilyEventsMap();
        for(Map.Entry<String, Event> entry : familyEvents.entrySet()) {
            final Event temp = entry.getValue();
            int iconColor = dataCache.getEventTypes().indexOf(temp.getEventType().toLowerCase()) * ICON_COLOR_FACTOR;
            if(iconColor > ICON_COLOR_MAX_VALUE) {
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
                    System.out.println("clicked event: " + marker.getTitle());
                    onSelectEvent(dataCache.getEventById(marker.getTitle()));

                    return true;
                }
            });
        }
        genderIcon.setImageDrawable(androidIcon);
    }
}
