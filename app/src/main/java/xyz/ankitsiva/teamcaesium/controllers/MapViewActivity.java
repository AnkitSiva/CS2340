package xyz.ankitsiva.teamcaesium.controllers;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.android.gms.maps.SupportMapFragment;
import xyz.ankitsiva.teamcaesium.R;
import xyz.ankitsiva.teamcaesium.model.AgeCategories;
import xyz.ankitsiva.teamcaesium.model.Gender;
import xyz.ankitsiva.teamcaesium.model.Shelter;
import xyz.ankitsiva.teamcaesium.model.User;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;


public class MapViewActivity extends AppCompatActivity implements OnMapReadyCallback{
    public ListView listView;
    public static final String TAG = ShelterViewActivity.class.getName();
    private GenericTypeIndicator<ArrayList<HashMap<String, Object>>> t =
            new GenericTypeIndicator<ArrayList<HashMap<String, Object>>>() {};
    private ArrayList<HashMap<String, Object>> dataList;
    private DatabaseReference mDatabase;
    private Iterator<HashMap<String, Object>> dataIterator;
    private Intent intent;
    private User user;
    private Spinner ageSpinner;
    private Spinner genderSpinner;
    private ArrayAdapter<String> ageArrayAdapter;
    private ArrayAdapter<String> genderArrayAdapter;
    private GoogleMap classGoogleMap;
    private HashMap<Shelter, Marker> shelterMarkers;
    private Shelter[] unusedShelterList;

    private final String[] chosenGender = new String[1];
    private final String[] chosenAge = new String[1];

    final int NUM_AGE_CATEGORIES = 5;
    final int NUM_GENDER_CATEGORIES = 3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Retrieve the content view that renders the map.
        setContentView(R.layout.activity_map);
        // Get the SupportMapFragment and request notification
        // when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        intent = getIntent();
        user = intent.getParcelableExtra("User");
        mDatabase = FirebaseDatabase.getInstance().getReferenceFromUrl(
                "https://cs2340-49af4.firebaseio.com/");

        ageSpinner = findViewById(R.id.ageSpinner);
        genderSpinner = findViewById(R.id.genderSpinner);

        shelterMarkers = new HashMap<>();

        chosenGender[0] = "";
        chosenAge[0] = "";

        ArrayList<String> ageCategoryStrings = new ArrayList<>(NUM_AGE_CATEGORIES);
        for (AgeCategories value: AgeCategories.values()) {
            ageCategoryStrings.add(value.getAgeCat());
        }

        ArrayList<String> genderCategoryStrings = new ArrayList<>(NUM_GENDER_CATEGORIES);
        for (Gender value: Gender.values()) {
            genderCategoryStrings.add(value.getGender());
        }

        ageArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, ageCategoryStrings);
        genderArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, genderCategoryStrings);

        ageSpinner.setAdapter(ageArrayAdapter);
        genderSpinner.setAdapter(genderArrayAdapter);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                dataList =  dataSnapshot.child("shelters").getValue(t);
                dataIterator = dataList.iterator();
                while (dataIterator.hasNext()) {
                    Shelter shelter = new Shelter(dataIterator.next());
                    LatLng shelterCoords = new LatLng(Double.parseDouble(shelter.getLatitude()), Double.parseDouble(shelter.getLongitude()));
                    Marker curr = classGoogleMap.addMarker(new MarkerOptions().position(shelterCoords).title(shelter.getName()).snippet(shelter.getPhoneNumber()));
                    shelterMarkers.put(shelter, curr);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", databaseError.toException());
            }
        });
        ageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (l == 0) {
                    chosenAge[0] = "";
                } else if (l == 1) {
                    chosenAge[0] = "Fam";
                } else if (l == 2) {
                    chosenAge[0] = "Chi";
                } else if (l == 3) {
                    chosenAge[0] = "You";
                }
                mutateMarkers();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                chosenAge[0] = "";
                mutateMarkers();
                return;
            }
        });

        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (l == 0) {
                    chosenGender[0] = "";
                } else if (l == 1) {
                    chosenGender[0] = "Men";
                } else if (l == 2) {
                    chosenGender[0] = "Wom";
                }
                mutateMarkers();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //No Change
                chosenGender[0] = "";
                mutateMarkers();
                return;
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        classGoogleMap = googleMap;
        // Add a marker in Sydney, Australia,
        // and move the map's camera to the same location.
        LatLng atl = new LatLng(33.749, -84.388);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(atl));
    }

    private void mutateMarkers() {
        Log.w(TAG, shelterMarkers.isEmpty()? "True": "False");
        for (Object object : shelterMarkers.keySet().toArray()) {
            Shelter shelter = (Shelter) object;
            Marker curr = shelterMarkers.get(shelter);
            curr.setVisible(shelter.getRestrictions().contains(chosenAge[0]) && shelter.getRestrictions().contains(chosenGender[0]));
            shelterMarkers.put(shelter, curr);
        }
        return;

    }
    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_OK, intent);
        super.onBackPressed();
    }
}

