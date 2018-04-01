package xyz.ankitsiva.teamcaesium.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.android.gms.maps.SupportMapFragment;
import xyz.ankitsiva.teamcaesium.R;
import xyz.ankitsiva.teamcaesium.model.Shelter;
import xyz.ankitsiva.teamcaesium.model.User;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
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
    private ArrayList<Shelter> shelterList;
    private Shelter updShelter;
    private Intent intent;
    private User user;
    private EditText inputSearch;
    private Spinner ageSpinner;
    private Spinner genderSpinner;
    private ArrayAdapter<String> ageArrayAdapter;
    private ArrayAdapter<String> genderArrayAdapter;
    private GoogleMap classGoogleMap;

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
        listView = findViewById(R.id.listview);
        shelterList = new ArrayList<>();
        final ArrayList<Shelter> backup = shelterList;
        inputSearch = findViewById(R.id.inputSearch);
        mDatabase = FirebaseDatabase.getInstance().getReferenceFromUrl(
                "https://cs2340-49af4.firebaseio.com/");
        Log.d(TAG, "onCreate: 1");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                dataList =  dataSnapshot.child("shelters").getValue(t);
                dataIterator = dataList.iterator();
                while (dataIterator.hasNext()) {
                    Shelter shelter = new Shelter(dataIterator.next());
                    shelterList.add(shelter);
                    LatLng shelterCoords = new LatLng(Double.parseDouble(shelter.getLatitude()), Double.parseDouble(shelter.getLongitude()));
                    classGoogleMap.addMarker(new MarkerOptions().position(shelterCoords).title(shelter.getName()).snippet(shelter.getPhoneNumber()));
                }

                Log.d(TAG, "onDataChange: 2");
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", databaseError.toException());
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
}
