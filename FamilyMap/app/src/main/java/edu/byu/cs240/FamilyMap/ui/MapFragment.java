package edu.byu.cs240.FamilyMap.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.Arrays;
import java.util.List;

import edu.byu.cs240.FamilyMap.R;
import edu.byu.cs240.FamilyMap.utils.Deserializer;
import edu.byu.cs240.FamilyMap.model.Event;
import edu.byu.cs240.FamilyMap.model.Model;
import edu.byu.cs240.FamilyMap.model.Person;
import edu.byu.cs240.FamilyMap.utils.Serializer;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap = null;
    private TextView topRowTextView;
    private TextView bottomRowTextView;
    private ImageView iconImageView;
    private Person currentPerson;
    private Event currentlySelectedEvent = null;
    private Event focusEvent = null;

    private final int lineWidth = 25;

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        super.onCreateView(layoutInflater, viewGroup, bundle);

        // Inflate the widget tree to get a view
        View v = layoutInflater.inflate(R.layout.fragment_map, viewGroup, false);

        // If we are supposed to start the map with a focus event we retrieve it
        if (this.getArguments() != null) {
            String focusEventJSON = this.getArguments().getString("focusEvent");
            focusEvent = (Event)(new Deserializer().deserialize(focusEventJSON, Event.class));
        }

        // There is an option menu for the main activity but not for the event activity
        setHasOptionsMenu(getContext().getClass() == edu.byu.cs240.FamilyMap.ui.MainActivity.class);

        // Wiring up widgets
        topRowTextView = v.findViewById(R.id.mapFragmentInfoTopRow);
        bottomRowTextView = v.findViewById(R.id.mapFragmentInfoBottomRow);
        iconImageView = v.findViewById(R.id.mapFragmentBottomInfoIcon);
        LinearLayout bottomInfoLinearLayout = v.findViewById(R.id.mapFragmentBottomInfoLinearLayout);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().
                findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

        bottomInfoLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the person activity if the person clicks on the bottom info
                if (Model.getInstance().getCurrentlySelectedPersonID() != null) {
                    Intent i = new Intent(getActivity(), PersonActivity.class);
                    i.putExtra("activityPerson", new Serializer().serialize(currentPerson));
                    startActivity(i);
                }
            }
        });

        return v;
    }

    @Override
    public void onResume()
    {
        super.onResume();

        if (mMap != null) {
            mMap.clear();
            mMap.setMapType(Model.getInstance().getMapType());
            drawMapMarkers();
            // We may have filtered out the event we were supposed to go back to by default
            if (!Model.getInstance().getAllUnfilteredEvents().contains(currentlySelectedEvent)) {
                removeEventInfo();
                return;
            }
            drawMapLines(currentlySelectedEvent);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.map_fragment_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Class newActivityClass = null;

        switch(item.toString()) {
            case "Search":
                newActivityClass = SearchActivity.class;
                break;
            case "Filter":
                newActivityClass = FilterActivity.class;
                break;
            case "Settings":
                newActivityClass = SettingsActivity.class;
                break;
        }

        Intent i = new Intent(getActivity(), newActivityClass);
        startActivity(i);

        return true;
    }

    private final List<Float> markerColors = Arrays.asList(
            BitmapDescriptorFactory.HUE_AZURE,
            BitmapDescriptorFactory.HUE_BLUE,
            BitmapDescriptorFactory.HUE_CYAN,
            BitmapDescriptorFactory.HUE_GREEN,
            BitmapDescriptorFactory.HUE_MAGENTA,
            BitmapDescriptorFactory.HUE_ORANGE,
            BitmapDescriptorFactory.HUE_RED,
            BitmapDescriptorFactory.HUE_ROSE,
            BitmapDescriptorFactory.HUE_VIOLET,
            BitmapDescriptorFactory.HUE_RED
    );

    private BitmapDescriptor getEventMarkerIcon(String eventType) {
        // Get the position of the eventType in the list of all events
        int eventTypePos = Model.getInstance().getEventTypes().indexOf(eventType.toLowerCase());

        return BitmapDescriptorFactory.defaultMarker(markerColors.get(eventTypePos % 10));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMapType(Model.getInstance().getMapType());

        if (focusEvent != null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(focusEvent.getLatitude(),
                    focusEvent.getLongitude())));

            showEventInfo(focusEvent);

            drawMapLines(focusEvent);
        }

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Event markerEvent = (Event)marker.getTag();
                Model.getInstance().setCurrentlySelectedPersonID(markerEvent.getPersonID());
                mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(markerEvent.getLatitude(),
                        markerEvent.getLongitude())));
                showEventInfo(markerEvent);

                drawMapLines(markerEvent);

                return true;
            }
        });

        drawMapMarkers();
    }

    private void drawMapMarkers() {
        for (Event event : Model.getInstance().getAllUnfilteredEvents()) {
            Marker m = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(event.getLatitude(), event.getLongitude()))
                    .icon(getEventMarkerIcon(event.getEventType())));
            m.setTag(event);
        }
    }

    private void drawMapLines(Event clickedEvent) {
        this.currentlySelectedEvent = clickedEvent;
        Model.getInstance().removeAllCurrentPolylines();

        if (Model.getInstance().isShowSpouseLines()) {
            drawSpouseLines(clickedEvent, Model.getInstance().getSpouseLinesColor());
        }

        if (Model.getInstance().isShowFamilyTreeLines()) {
            drawFamilyTreeLines(clickedEvent, Model.getInstance().getFamilyTreeLinesColor());
        }

        if (Model.getInstance().isShowLifeStoryLines()) {
            drawStoryLines(clickedEvent, Model.getInstance().getLifeStoryLinesColor());
        }
    }

    private void drawStoryLines(Event markerEvent, int lineColor) {
        if (markerEvent == null) {
            return;
        }
        // Draw life story line (magenta temporarily)
        List<Event> storyLineEvents = Model.getInstance().getOrderedPersonEvents(markerEvent.getPersonID());
        if (storyLineEvents.size() != 0) {
            PolylineOptions storyLineOptions = new PolylineOptions().clickable(false).
                    color(lineColor).width(lineWidth);

            for (Event e : storyLineEvents) {
                storyLineOptions.add(new LatLng(e.getLatitude(), e.getLongitude()));
            }

            Polyline storyLine = mMap.addPolyline(storyLineOptions);
            Model.getInstance().addCurrentPolyLine(storyLine);
        }
    }

    private void drawFamilyTreeLine(Event e, Person p, int gens, int lineColor) {
        // Event e is for the start of the line
        // Person p is for the end of the line
        Event earliestEvent = Model.getInstance().getPersonEarliestEvent(p.getPersonID());

        if (earliestEvent != null) {
            int treeLineWidthStart = 50;
            // How much to decrease size by with each generation
            int treeLineWidthDecrement = 10;
            Polyline pl = mMap.addPolyline(new PolylineOptions().clickable(false).add(
                    new LatLng(e.getLatitude(), e.getLongitude()))
                    .add(new LatLng(earliestEvent.getLatitude(), earliestEvent.getLongitude()))
                    .color(lineColor).width(treeLineWidthStart - treeLineWidthDecrement * gens));
            Model.getInstance().addCurrentPolyLine(pl);

            // Draw family tree line to father if exists
            if (p.getFather() != null) {
                drawFamilyTreeLine(earliestEvent, Model.getInstance().
                        getPersonByPersonID(p.getFather()), gens + 1, lineColor);
            }

            // Draw family tree line to mother if exists
            if (p.getMother() != null) {
                drawFamilyTreeLine(earliestEvent, Model.getInstance().
                        getPersonByPersonID(p.getMother()), gens + 1, lineColor);
            }
        }
    }

    private void drawSpouseLines(Event markerEvent, int lineColor) {
        if (markerEvent == null) {
            return;
        }

        Event spouseLineEnd = getSpouseLineEnd(markerEvent.getPersonID());
        if (spouseLineEnd != null) {
            Polyline spouseLine = mMap.addPolyline(new PolylineOptions().clickable(false).add(
                    new LatLng(markerEvent.getLatitude(), markerEvent.getLongitude()))
                    .add(new LatLng(spouseLineEnd.getLatitude(), spouseLineEnd.getLongitude()))
                    .color(lineColor).width(lineWidth));
            Model.getInstance().addCurrentPolyLine(spouseLine);
        }
    }

    private void drawFamilyTreeLines(Event markerEvent, int lineColor) {
        if (markerEvent == null) {
            return;
        }

        Model myModel = Model.getInstance();

        Person markerPerson = Model.getInstance().getPersonByPersonID(markerEvent.getPersonID());
        if (markerPerson.getFather() != null) {
            drawFamilyTreeLine(markerEvent, myModel.getPersonByPersonID(markerPerson.getFather()),
                    0, lineColor);
        }
        if (markerPerson.getMother() != null) {
            drawFamilyTreeLine(markerEvent, myModel.getPersonByPersonID(markerPerson.getMother()),
                    0, lineColor);
        }
    }

    private void removeEventInfo() {
        topRowTextView.setText("Click on a marker");
        bottomRowTextView.setText("to see event details");
        iconImageView.setImageResource(R.drawable.android_icon);
    }

    private void showEventInfo(Event e) {
        Model myModel = Model.getInstance();
        myModel.setCurrentlySelectedPersonID(e.getPersonID());
        Person eventPerson = myModel.getPersonMap().get(e.getPersonID());
        currentPerson = eventPerson;
        topRowTextView.setText(eventPerson.getFirstName() + " " + eventPerson.getLastName());
        bottomRowTextView.setText(e.getEventType() + ": " +
                e.getCity() + ", " + e.getCountry() + " (" +
                Integer.toString(e.getYear()) + ")");
        if (eventPerson.getGender().equals("m")) {
            iconImageView.setImageResource(R.drawable.male_icon);
        } else {
            iconImageView.setImageResource(R.drawable.female_icon);
        }
    }

    private Event getSpouseLineEnd(String personID) {
        Event endEvent = null;

        // Get person associated with the personID
        Person p = Model.getInstance().getPersonByPersonID(personID);

        // Check that the person has a spouse and that the spouse has events
        if (p.getSpouse() != null) {
            return Model.getInstance().getPersonEarliestEvent(p.getSpouse());
        }

        return null;
    }
}