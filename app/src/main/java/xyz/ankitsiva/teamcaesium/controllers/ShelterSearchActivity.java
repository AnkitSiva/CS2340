package xyz.ankitsiva.teamcaesium.controllers;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import xyz.ankitsiva.teamcaesium.R;
import xyz.ankitsiva.teamcaesium.model.AgeCategories;
import xyz.ankitsiva.teamcaesium.model.Gender;

/**
 * Created by ankitsiva on 3/4/18.
 */

public class ShelterSearchActivity extends ListActivity {
    public static final String TAG = ShelterSearchActivity.class.getName();
    public ArrayList<TextView> shelterViews;
    public TextView view;
    public Bundle bundle;
    private GenericTypeIndicator<ArrayList<HashMap<String, Object>>> t =
            new GenericTypeIndicator<ArrayList<HashMap<String, Object>>>() {};
    private ArrayList<HashMap<String, Object>> shelterList;
    private HashMap<String, Object> shelter;
    private DatabaseReference mDatabase;
    private Iterator<HashMap<String, Object>> dataIterator;
    private Iterator<TextView> viewIterator;
    private Spinner genderSpinner;
    private Spinner ageSpinner;

    private void searchShelters(String query, Object ageCategory, Object gender) {
        if (shelterList == null) {
            throw new RuntimeException("oopsies");
        }
        for (HashMap<String, Object> localShelter : shelterList) {
            if(((String) localShelter.get("Shelter Name")).contains(query)
                    && ((String) localShelter.get("Restrictions")).contains(ageCategory.toString())
                    && ((String) localShelter.get("Restrictions")).contains(gender.toString())) {
                setContentView(R.layout.activity_shelter_view);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle = new Bundle();
        setContentView(R.layout.activity_shelter_search);
        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            searchShelters(query, ageSpinner.getSelectedItem(), genderSpinner.getSelectedItem());
        }
        genderSpinner = (Spinner)  findViewById(R.id.gender);
        ageSpinner = (Spinner) findViewById(R.id.age_category);
        ArrayAdapter<String> genderAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, Gender.values());
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(genderAdapter);
        ArrayAdapter<String> ageAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, AgeCategories.values());
        ageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ageSpinner.setAdapter(ageAdapter);
        mDatabase = FirebaseDatabase.getInstance().getReferenceFromUrl(
                "https://cs2340-49af4.firebaseio.com/");

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                shelterList =  dataSnapshot.getValue(t);
                dataIterator = shelterList.iterator();
                while (viewIterator.hasNext()) {
                    view = viewIterator.next();
                    shelter = dataIterator.next();
                    view.setText((String) shelter.get("Shelter Name"));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", databaseError.toException());
            }
        });
    }
/*
    private void initializeViews() {
        shelterViews.add((TextView) findViewById(R.id.shelter));
        shelterViews.add((TextView) findViewById(R.id.shelter1));
        shelterViews.add((TextView) findViewById(R.id.shelter2));
        shelterViews.add((TextView) findViewById(R.id.shelter3));
        shelterViews.add((TextView) findViewById(R.id.shelter4));
        shelterViews.add((TextView) findViewById(R.id.shelter5));
        shelterViews.add((TextView) findViewById(R.id.shelter6));
        shelterViews.add((TextView) findViewById(R.id.shelter7));
        shelterViews.add((TextView) findViewById(R.id.shelter8));
        shelterViews.add((TextView) findViewById(R.id.shelter9));
        shelterViews.add((TextView) findViewById(R.id.shelter10));
        shelterViews.add((TextView) findViewById(R.id.shelter11));
        shelterViews.add((TextView) findViewById(R.id.shelter12));
    }
    //Will need bundle or extras along with the Intent to pass shelter information over
    public void Contacts(View listView) {
        int shelterNo = Integer.parseInt(listView.getTag().toString());
        shelter = shelterList.get(shelterNo);
        bundle.putString("Address", (String) shelter.get("Address"));
        bundle.putString("Capacity", (String) shelter.get("Capacity"));
        //Note: Latitude and longitude require that extra space for the key because someone messed up
        bundle.putString("Latitude", (String) shelter.get("Latitude "));
        bundle.putString("Longitude", (String) shelter.get("Longitude "));
        bundle.putString("Phone Number", (String) shelter.get("Phone Number"));
        bundle.putString("Restrictions", (String) shelter.get("Restrictions"));
        bundle.putString("Shelter Name", (String) shelter.get("Shelter Name"));
        bundle.putString("Special Notes", (String) shelter.get("Special Notes"));
        Intent intent = new Intent(this, ShelterContentActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
     }
     */
}
