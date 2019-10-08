package edu.byu.cs240.FamilyMap.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.List;

import edu.byu.cs240.FamilyMap.R;
import edu.byu.cs240.FamilyMap.model.Event;
import edu.byu.cs240.FamilyMap.model.Model;
import edu.byu.cs240.FamilyMap.model.Person;

public class SearchActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate widget tree
        setContentView(R.layout.activity_search);

        // Set up the RecyclerView
        recyclerView = findViewById(R.id.searchRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        final Model model = Model.getInstance();
        setRVAdapter(model.getAllUnfilteredPersons(), model.getAllUnfilteredEvents());

        EditText searchBar = findViewById(R.id.searchBarEditText);

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String pattern = s.toString().toLowerCase();
                setRVAdapter(model.getPersonsInSearch(model.getAllUnfilteredPersons(), pattern),
                             model.getEventsInSearch(model.getAllUnfilteredEvents(), pattern));
            }
        });
    }

    private void setRVAdapter(List<Person> personList, List<Event> eventList) {
        RecyclerView.Adapter adapter = new SearchAdapter(this, personList, eventList);
        recyclerView.setAdapter(adapter);
    }
}
