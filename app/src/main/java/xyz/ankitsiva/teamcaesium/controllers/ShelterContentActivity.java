package xyz.ankitsiva.teamcaesium.controllers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import xyz.ankitsiva.teamcaesium.R;
import xyz.ankitsiva.teamcaesium.model.Shelter;

public class ShelterContentActivity extends AppCompatActivity {

    public ArrayList<TextView> viewList;
    public TextView mView;
    public Intent intent;
    public Shelter shelter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shelter_content);

        intent = getIntent();
        shelter = intent.getParcelableExtra("Shelter");
        viewList = new ArrayList<TextView>();
        initializeViews();
    }

    private void initializeViews() {
        mView = findViewById(R.id.shelterName);
        mView.setText("Name:    " + shelter.getName());
        mView = findViewById(R.id.shelterAddress);
        mView.setText("Address:    " + shelter.getAddress());
        mView = findViewById(R.id.shelterCapacity);
        mView.setText("Capacity:    " + shelter.getCapacity());
        mView = findViewById(R.id.shelterPhone);
        mView.setText("Phone#:    " + shelter.getPhoneNumber());
        mView = findViewById(R.id.shelterLatitude);
        mView.setText("Latitude:    " + shelter.getLatitude());
        mView = findViewById(R.id.shelterLongitude);
        mView.setText("Longitude:    " + shelter.getLongitude());
        mView = findViewById(R.id.shelterRestrictions);
        mView.setText("Restrictions:    " + shelter.getRestrictions());
        mView = findViewById(R.id.shelterNotes);
        mView.setText("Special Notes:    " + shelter.getSpecialNotes());
    }

    public void claimBed(View view) {
        Intent intent = new Intent(this, ClaimBedActivity.class);
        intent.putExtra("Shelter", shelter);
        startActivity(intent);
    }
}
