package edu.byu.cs240.FamilyMap.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;

import com.google.android.gms.maps.GoogleMap;

import java.util.Arrays;
import java.util.List;

import edu.byu.cs240.FamilyMap.R;
import edu.byu.cs240.FamilyMap.model.EventArray;
import edu.byu.cs240.FamilyMap.model.PersonArray;
import edu.byu.cs240.FamilyMap.utils.Deserializer;
import edu.byu.cs240.FamilyMap.model.Event;
import edu.byu.cs240.FamilyMap.model.Model;
import edu.byu.cs240.FamilyMap.model.Person;
import edu.byu.cs240.FamilyMap.net.ServerProxy;

public class SettingsActivity extends AppCompatActivity {

    private Switch spouseSwitch;
    private Switch familyTreeSwitch;
    private Switch lifeStorySwitch;

    private Spinner lifeStorySpinner;
    private Spinner familyTreeSpinner;
    private Spinner spouseSpinner;
    private Spinner mapSpinner;

    private LinearLayout resyncButton;
    private LinearLayout logoutButton;

    private final Integer red = Color.rgb(224, 55, 40);
    private final Integer orange = Color.rgb(255, 137, 28);
    private final Integer yellow = Color.rgb(255, 247, 28);
    private final Integer green = Color.rgb(42, 175, 40);
    private final Integer blue = Color.rgb(20, 177, 229);
    private final Integer indigo = Color.rgb(29, 0, 51);
    private final Integer violet = Color.rgb(155, 8, 178);

    private final List<Integer> lineColors = Arrays.asList(red, orange, yellow, green,
            blue, indigo, violet);

    private int getRGBOfPosition(int pos) {
        return lineColors.get(pos);
    }

    private void wireUpWidgets() {
        spouseSwitch = findViewById(R.id.showSpouseLinesSwitch);
        familyTreeSwitch = findViewById(R.id.showTreeLinesSwitch);
        lifeStorySwitch = findViewById(R.id.showLifeStoryLinesSwitch);

        lifeStorySpinner = findViewById(R.id.lifeStoryLinesColorSpinner);
        familyTreeSpinner = findViewById(R.id.treeLinesColorSpinner);
        spouseSpinner = findViewById(R.id.spouseLineColorSpinner);
        mapSpinner = findViewById(R.id.mapTypeSpinner);

        resyncButton = findViewById(R.id.resyncDataRowLinearLayout);
        logoutButton = findViewById(R.id.logoutRowLinearLayout);
    }

    private void setSwitchPositions() {
        lifeStorySwitch.setChecked(Model.getInstance().isShowLifeStoryLines());
        familyTreeSwitch.setChecked(Model.getInstance().isShowFamilyTreeLines());
        spouseSwitch.setChecked(Model.getInstance().isShowSpouseLines());
    }

    private void setSwitchListeners() {
        spouseSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Model.getInstance().setShowSpouseLines(isChecked);
            }
        });

        familyTreeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Model.getInstance().setShowFamilyTreeLines(isChecked);
            }
        });

        lifeStorySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Model.getInstance().setShowLifeStoryLines(isChecked);
            }
        });
    }

    private void setSpinnerListeners() {
        lifeStorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
                Model.getInstance().setLifeStoryLinesColorIndex(pos);
                Model.getInstance().setLifeStoryLinesColor(getRGBOfPosition(pos));
            }

            @Override
            public void onNothingSelected(AdapterView<?> av) {}
        });

        familyTreeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
                Model.getInstance().setFamilyTreeLinesColorIndex(pos);
                Model.getInstance().setFamilyTreeLinesColor(getRGBOfPosition(pos));
            }

            @Override
            public void onNothingSelected(AdapterView<?> av) {}
        });

        spouseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
                Model.getInstance().setSpouseLinesColorIndex(pos);
                Model.getInstance().setSpouseLinesColor(getRGBOfPosition(pos));
            }

            @Override
            public void onNothingSelected(AdapterView<?> av) {}
        });

        mapSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
                String desiredMapType = parent.getItemAtPosition(pos).toString();

                switch (desiredMapType) {
                    case "Normal":
                        Model.getInstance().setMapType(GoogleMap.MAP_TYPE_NORMAL);
                        break;
                    case "Hybrid":
                        Model.getInstance().setMapType(GoogleMap.MAP_TYPE_HYBRID);
                        break;
                    case "Satellite":
                        Model.getInstance().setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                        break;
                    case "Terrain":
                        Model.getInstance().setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> av) {}
        });
    }

    private void setSpinnerPositions() {
        lifeStorySpinner.setSelection(Model.getInstance().getLifeStoryLinesColorIndex());
        mapSpinner.setSelection(Model.getInstance().getMapType() - 1);
        familyTreeSpinner.setSelection(Model.getInstance().getFamilyTreeLinesColorIndex());
        spouseSpinner.setSelection(Model.getInstance().getSpouseLinesColorIndex());
    }

    private void setButtonListeners() {
        resyncButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Save the Auth Token and personID
                String authToken = Model.getInstance().getAuthToken();
                String userID = Model.getInstance().getUserID();
                // Reset the model
                Model.getInstance().reset();
                // Add auth token back in
                Model.getInstance().setAuthToken(authToken);
                Model.getInstance().setUserID(userID);

                // Do the get data task
                try {
                    Object result = new GetDataTask().execute("10.0.2.2", "8080", authToken).get();
                } catch (Exception e) {}

                // Go back to the map fragment
                Intent i = new Intent(SettingsActivity.this, MainActivity.class);
                startActivity(i);
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Model.getInstance().setAuthToken(null);
                Intent i = new Intent(SettingsActivity.this, MainActivity.class);

                Model.getInstance().reset();

                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
                        Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate widget tree
        setContentView(R.layout.activity_settings);

        getSupportActionBar().setTitle("Family Map: Settings");

        wireUpWidgets();

        setSwitchListeners();
        setSwitchPositions();

        setSpinnerListeners();
        setSpinnerPositions();

        setButtonListeners();
    }

    private class GetDataTask extends AsyncTask<String, Integer, Boolean> {

        protected Boolean doInBackground(String... requestData) {

            String serverHost = requestData[0];
            String serverPort = requestData[1];
            String authToken = requestData[2];

            ServerProxy proxy = new ServerProxy(serverHost, serverPort);
            Deserializer d = new Deserializer();

            String userEventsJson = proxy.getAllUserEvents(authToken);
            Event[] userEventsArray = ((EventArray)d.deserialize(userEventsJson,
                    EventArray.class)).getData();

            String userPersonsJson = proxy.getAllUserPersons(authToken);
            Person[] userPersonsArray = ((PersonArray)d.deserialize(userPersonsJson,
                    PersonArray.class)).getData();

            Model.getInstance().setUserEvents(Arrays.asList(userEventsArray));
            Model.getInstance().setUserPersons(Arrays.asList(userPersonsArray));

            return true;
        }
    }
}