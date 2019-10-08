package edu.byu.cs240.FamilyMap.ui;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import edu.byu.cs240.FamilyMap.R;
import edu.byu.cs240.FamilyMap.model.Model;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Model.getInstance().getAuthToken() == null) {
            // If we don't have an auth token that means the user hasn't logged in yet
            startLoginFragment();
        } else {
            startMapFragment();
        }
    }

    private void startLoginFragment() {
        FragmentManager fm = this.getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.mainActivityFragmentFrameLayout,
                new LoginFragment()).commit();
    }

    public void startMapFragment() {
        FragmentManager fm = this.getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.mainActivityFragmentFrameLayout,
                new MapFragment()).commit();
    }
}
