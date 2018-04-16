package xyz.ankitsiva.teamcaesium.controllers;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

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
import com.google.android.gms.maps.model.LatLngBounds;
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
import java.util.List;
import java.util.Map;


/**
 * Controller for Map View
 */
public class MapViewActivity extends AppCompatActivity implements OnMapReadyCallback{
    private static final double ATLATITUDE = 33.749;
    private static final double ATLONGITUDE = -84.388;
    private static final String TAG = ShelterViewActivity.class.getName();
    private final GenericTypeIndicator<ArrayList<HashMap<String, Object>>> t =
            new GenericTypeIndicator<ArrayList<HashMap<String, Object>>>() {};
    private List<HashMap<String, Object>> dataList;
    private Iterator<HashMap<String, Object>> dataIterator;
    private Intent intent;
    private User user;
    private GoogleMap classGoogleMap;
    private Map<Shelter, Marker> shelterMarkers;
    private LatLngBounds cameraBounds;

    private final String[] chosenGender = new String[1];
    private final String[] chosenAge = new String[1];

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
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReferenceFromUrl(
                "https://cs2340-49af4.firebaseio.com/");

        Spinner ageSpinner = findViewById(R.id.ageSpinner);
        Spinner genderSpinner = findViewById(R.id.genderSpinner);
        final LatLngBounds.Builder cameraBuilder = new LatLngBounds.Builder();

        shelterMarkers = new HashMap<>();

        chosenGender[0] = "";
        chosenAge[0] = "";

        int NUM_AGE_CATEGORIES = 5;
        ArrayList<String> ageCategoryStrings = new ArrayList<>(NUM_AGE_CATEGORIES);
        for (AgeCategories value: AgeCategories.values()) {
            ageCategoryStrings.add(value.getAgeCat());
        }

        int NUM_GENDER_CATEGORIES = 3;
        ArrayList<String> genderCategoryStrings = new ArrayList<>(NUM_GENDER_CATEGORIES);
        for (Gender value: Gender.values()) {
            genderCategoryStrings.add(value.getGender());
        }

        SpinnerAdapter ageArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, ageCategoryStrings);
        SpinnerAdapter genderArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, genderCategoryStrings);

        ageSpinner.setAdapter(ageArrayAdapter);
        genderSpinner.setAdapter(genderArrayAdapter);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                dataList =  dataSnapshot.child("shelters").getValue(t);
                assert dataList != null;
                dataIterator = dataList.iterator();
                while (dataIterator.hasNext()) {
                    Shelter shelter = new Shelter(dataIterator.next());
                    LatLng shelterCoordinates =
                            new LatLng(Double.parseDouble(shelter.getLatitude()),
                                    Double.parseDouble(shelter.getLongitude()));
                    cameraBuilder.include(shelterCoordinates);
                    Marker curr = classGoogleMap.addMarker(new MarkerOptions().
                            position(shelterCoordinates).title(shelter.getName()).
                            snippet(shelter.getPhoneNumber()));
                    shelterMarkers.put(shelter, curr);
                }
                cameraBounds = cameraBuilder.build();
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
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        classGoogleMap = googleMap;
        // Add a marker in Sydney, Australia,
        // and move the map's camera to the same location.

        LatLng atl = new LatLng(ATLATITUDE, ATLONGITUDE);
        if(cameraBounds == null) {
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(atl));
        } else {
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(cameraBounds.getCenter()));
        }
    }

    /**
     * Mutates markers and sets appropriate markers to visible/invisible
     * @throws IllegalArgumentException
     */
    public void mutateMarkers() throws IllegalArgumentException{
        if (shelterMarkers == null) {
            throw new IllegalArgumentException("Shelter markers not currently set. " +
                    "Create them and re-request");
        }

        if (chosenAge[0] == null) {
            throw new IllegalArgumentException("Chosen age not correctly set. " +
                    "Create them and re-request");
        }

        if (chosenGender[0] == null) {
            throw new IllegalArgumentException("Chosen gender not correctly set. " +
                    "Create them and re-request");
        }

        for (Object object : shelterMarkers.keySet().toArray()) {
            Shelter shelter = (Shelter) object;
            Marker curr = shelterMarkers.get(shelter);
            curr.setVisible(shelter.getRestrictions().contains(chosenAge[0]) &&
                    shelter.getRestrictions().contains(chosenGender[0]));
            shelterMarkers.put(shelter, curr);
        }

    }

    /**
     * Sets the shelterMarker in case of saving the user's last searched shelters
     * @param shelterMarkers
     */
    public void setShelterMarkers(Map<Shelter, Marker> shelterMarkers) {
        this.shelterMarkers = shelterMarkers;
    }

    /**
     * Returns the shelterMarker in case of saving the user's last searched shelters
     */
    public Map<Shelter, Marker> getShelterMarkers() {
        return shelterMarkers;
    }

    @Override
    public void onBackPressed() {
        intent.putExtra("User", user);
        setResult(Activity.RESULT_OK, intent);
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }

    /**
     * Sets user's chosen gender
     * @param choice
     */
    public void setChosenGender(String choice) {
        chosenGender[0] = choice;
    }

    /**
     * Sets user's chosen age
     * @param choice
     */
    public void setChosenAge(String choice) {
        chosenAge[0] = choice;
    }
}

