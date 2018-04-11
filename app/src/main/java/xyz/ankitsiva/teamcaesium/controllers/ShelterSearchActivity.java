package xyz.ankitsiva.teamcaesium.controllers;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import java.util.List;

import xyz.ankitsiva.teamcaesium.R;
import xyz.ankitsiva.teamcaesium.model.AgeCategories;
import xyz.ankitsiva.teamcaesium.model.Gender;

/**
 * Created by ankitsiva on 3/4/18.
 */

public class ShelterSearchActivity extends ListActivity {
    private static final String TAG = ShelterSearchActivity.class.getName();
    private TextView view;
    private Bundle bundle;
    private final GenericTypeIndicator<ArrayList<HashMap<String, Object>>> t =
            new GenericTypeIndicator<ArrayList<HashMap<String, Object>>>() {};
    private List<HashMap<String, Object>> shelterList;
    private HashMap<String, Object> shelter;
    private Iterator<HashMap<String, Object>> dataIterator;
    private Iterator<TextView> viewIterator;
    private Spinner genderSpinner;
    private Spinner ageSpinner;

    private void searchShelters(CharSequence query, Object ageCategory, Object gender) {
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
        genderSpinner = findViewById(R.id.gender);
        ageSpinner = findViewById(R.id.age_category);
        ArrayAdapter<String> genderAdapter = new ArrayAdapter(
                this, android.R.layout.simple_spinner_item, Gender.values());
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(genderAdapter);
        ArrayAdapter<String> ageAdapter = new ArrayAdapter(
                this, android.R.layout.simple_spinner_item, AgeCategories.values());
        ageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ageSpinner.setAdapter(ageAdapter);
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReferenceFromUrl(
                "https://cs2340-49af4.firebaseio.com/");

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                shelterList =  dataSnapshot.child("shelters").getValue(t);
                try {
                    assert shelterList != null;
                    dataIterator = shelterList.iterator();
                } catch (Exception e) {

                }
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
        setContentView(R.layout.activity_shelter_view);
    }
}
