package edu.byu.cs240.FamilyMap.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import edu.byu.cs240.FamilyMap.R;

public class EventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate widget tree
        setContentView(R.layout.activity_main);

        // Get the event to focus on from the intent
        String focusEventJSON = getIntent().getStringExtra("focusEvent");

        // Pass the JSON of the event to focus on forward to the map fragment in a bundle
        Bundle bundle = new Bundle();
        bundle.putString("focusEvent", focusEventJSON);
        MapFragment mapFragment = new MapFragment();
        mapFragment.setArguments(bundle);

        // Switch to map fragment in the fragment frame of the main activity
        FragmentManager fm = this.getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.mainActivityFragmentFrameLayout, mapFragment).commit();
    }
}
