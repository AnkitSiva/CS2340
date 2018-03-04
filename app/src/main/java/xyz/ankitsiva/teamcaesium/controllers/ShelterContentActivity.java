package xyz.ankitsiva.teamcaesium.controllers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;

import xyz.ankitsiva.teamcaesium.R;

public class ShelterContentActivity extends AppCompatActivity {

    public ArrayList<TextView> viewList;
    public TextView mView;
    public Intent intent;
    public Bundle bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shelter_content);

        intent = getIntent();
        bundle = intent.getExtras();
        viewList = new ArrayList<TextView>();
        initializeViews();
    }

    private void initializeViews() {
        mView = findViewById(R.id.shelterName);
        mView.setText("Name:    " + bundle.get("Shelter Name"));
        mView = findViewById(R.id.shelterAddress);
        mView.setText("Address:    " + bundle.get("Address"));
        mView = findViewById(R.id.shelterCapacity);
        mView.setText("Capacity:    " + bundle.get("Capacity"));
        mView = findViewById(R.id.shelterPhone);
        mView.setText("Phone#:    " + bundle.get("Phone Number"));
        mView = findViewById(R.id.shelterLatitude);
        mView.setText("Latitude:    " + bundle.get("Latitude"));
        mView = findViewById(R.id.shelterLongitude);
        mView.setText("Longitude:    " + bundle.get("Longitude"));
        mView = findViewById(R.id.shelterRestrictions);
        mView.setText("Restrictions:    " + bundle.get("Restrictions"));
        mView = findViewById(R.id.shelterNotes);
        mView.setText("Special Notes:    " + bundle.get("Special Notes"));
    }
}
