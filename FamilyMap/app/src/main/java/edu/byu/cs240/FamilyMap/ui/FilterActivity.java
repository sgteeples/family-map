package edu.byu.cs240.FamilyMap.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs240.FamilyMap.R;
import edu.byu.cs240.FamilyMap.model.Model;

public class FilterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        List<String> filters = getFilters();

        // Inflate widget tree
        setContentView(R.layout.activity_filter);

        // Set up recycler view
        RecyclerView recyclerView = findViewById(R.id.filterRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView.Adapter adapter = new FilterAdapter(this, filters);
        recyclerView.setAdapter(adapter);
    }

    private List<String> getFilters() {
        List<String> filters = new ArrayList<>();

        for (String eventType : Model.getInstance().getEventTypes()) {
            filters.add(eventType);
        }
        filters.add("Male");
        filters.add("Female");
        filters.add("Father's Side");
        filters.add("Mother's Side");

        return filters;
    }
}
