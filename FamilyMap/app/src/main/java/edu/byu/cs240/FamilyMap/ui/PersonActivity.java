package edu.byu.cs240.FamilyMap.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.byu.cs240.FamilyMap.R;
import edu.byu.cs240.FamilyMap.utils.Deserializer;
import edu.byu.cs240.FamilyMap.model.Event;
import edu.byu.cs240.FamilyMap.model.Model;
import edu.byu.cs240.FamilyMap.model.Person;

public class PersonActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate the widget tree
        setContentView(R.layout.activity_person);

        getSupportActionBar().setTitle("Family Map: Personal Details");

        // Person to be focused on in the activity
        String activityPersonJSON = getIntent().getStringExtra("activityPerson");
        Person activityPerson = (Person)(new Deserializer().deserialize(activityPersonJSON, Person.class));

        // Wire up the widgets
        TextView firstNameTextView = findViewById(R.id.personActivityFirstNameTextView);
        TextView lastNameTextView = findViewById(R.id.lastNameTextView);
        TextView genderTextView = findViewById(R.id.genderTextView);
        firstNameTextView.setText(activityPerson.getFirstName());
        lastNameTextView.setText(activityPerson.getLastName());

        // Make gender label correct
        if (activityPerson.getGender().equals("m")) {
            genderTextView.setText("Male");
        } else {
            genderTextView.setText("Female");
        }

        // Set up expandable list view
        ExpandableListView expListView = findViewById(R.id.personActivityExpandableListView);
        Model model = Model.getInstance();
        List<Event> lifeEventsData = model.getOrderedPersonEvents(activityPerson.getPersonID());
        List<Person> familyData = model.getPersonFilteredFamily(activityPerson.getPersonID());
        ArrayList<String> headers = new ArrayList<>(Arrays.asList("LIFE EVENTS", "FAMILY"));

        PersonExpListAdapter listAdapter = new PersonExpListAdapter(this, headers,
                lifeEventsData, familyData);

        expListView.setAdapter(listAdapter);
    }
}