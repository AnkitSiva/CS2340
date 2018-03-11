package xyz.ankitsiva.teamcaesium.controllers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import xyz.ankitsiva.teamcaesium.R;
import xyz.ankitsiva.teamcaesium.model.AgeCategories;
import xyz.ankitsiva.teamcaesium.model.Gender;
import xyz.ankitsiva.teamcaesium.model.Shelter;


public class ShelterViewActivity extends AppCompatActivity {
    public static final String TAG = ShelterViewActivity.class.getName();
    public ListView listView;
    private GenericTypeIndicator<ArrayList<HashMap<String, Object>>> t =
            new GenericTypeIndicator<ArrayList<HashMap<String, Object>>>() {};
    private ArrayList<HashMap<String, Object>> dataList;
    private DatabaseReference mDatabase;
    private Iterator<HashMap<String, Object>> dataIterator;
    private ArrayList<Shelter> shelterList;
    private Intent intent;
    private Bundle bundle;
    private EditText inputSearch;
    private Spinner ageSpinner;
    private Spinner genderSpinner;
    private ArrayAdapter<String> ageArrayAdapter;
    private ArrayAdapter<String> genderArrayAdapter;

    final int NUM_AGE_CATEGORIES = 5;
    final int NUM_GENDER_CATEGORIES = 3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intent = getIntent();
        bundle = intent.getExtras();

        setContentView(R.layout.activity_shelter_view);

        listView = findViewById(R.id.listview);
        shelterList = new ArrayList<>();
        inputSearch = findViewById(R.id.inputSearch);

        ageSpinner = findViewById(R.id.ageSpinner);
        genderSpinner = findViewById(R.id.genderSpinner);

        ArrayList<String> ageCategoryStrings = new ArrayList<>(NUM_AGE_CATEGORIES);
        for (AgeCategories value: AgeCategories.values()) {
            ageCategoryStrings.add(value.getAgeCat());
        }

        ArrayList<String> genderCategoryStrings = new ArrayList<>(NUM_GENDER_CATEGORIES);
        for (Gender value: Gender.values()) {
            genderCategoryStrings.add(value.getGender());
        }

        ageArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, ageCategoryStrings);
        genderArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, genderCategoryStrings);

        ageSpinner.setAdapter(ageArrayAdapter);
        genderSpinner.setAdapter(genderArrayAdapter);
        final ArrayAdapter<Shelter> shelterArrayAdapter = new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_list_item_1, shelterList);
        ageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ArrayList<Shelter> filteredShelterList = shelterList;
                ArrayAdapter<Shelter> filteredShelterListAdapter;
                if (l == 0) {
                    return;
                } else if (l == 1) {
                    for (Shelter shelter : shelterList) {
                        if(shelter.getRestrictions().contains(AgeCategories.FAM_NEWBORN.getAgeCat())) {
                            filteredShelterList.add(shelter);
                        }
                    }
                } else if (l == 2) {
                    for (Shelter shelter : shelterList) {
                        if(shelter.getRestrictions().contains(AgeCategories.CHILDREN.getAgeCat())) {
                            filteredShelterList.add(shelter);
                        }
                    }
                }else if (l == 3) {
                    for (Shelter shelter : shelterList) {
                        if(shelter.getRestrictions().contains(AgeCategories.YA.getAgeCat())) {
                            filteredShelterList.add(shelter);
                        }
                    }
                }else if (l == 4) {
                    for (Shelter shelter : shelterList) {
                        if(shelter.getRestrictions().contains(AgeCategories.ANYONE.getAgeCat())) {
                            filteredShelterList.add(shelter);
                        }
                    }
                }

                filteredShelterListAdapter = new ArrayAdapter<Shelter>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, filteredShelterList);
                listView.setAdapter(filteredShelterListAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //No Change
                return;
            }
        });
        inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                shelterArrayAdapter.getFilter().filter(cs);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                return;
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                return;
            }
        });
        mDatabase = FirebaseDatabase.getInstance().getReferenceFromUrl(
                "https://cs2340-49af4.firebaseio.com/");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                dataList =  dataSnapshot.getValue(t);
                dataIterator = dataList.iterator();
                while (dataIterator.hasNext()) {
                    Shelter shelter = new Shelter(dataIterator.next());
                    shelterList.add(shelter);
                }
                listView.setAdapter(shelterArrayAdapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", databaseError.toException());
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                bundle = new Bundle();
                Shelter selectedShelter = (Shelter) parent.getItemAtPosition(position);
                Intent intent = new Intent(getApplicationContext(), ShelterContentActivity.class);
                bundle.putString("Address", selectedShelter.getAddress());
                bundle.putString("Capacity", selectedShelter.getCapacity());
                //Note: Latitude and longitude require that extra space for the key because someone messed up
                bundle.putString("Latitude", selectedShelter.getLatitude());
                bundle.putString("Longitude", selectedShelter.getLongitude());
                bundle.putString("Phone Number", selectedShelter.getPhoneNumber());
                bundle.putString("Restrictions", selectedShelter.getRestrictions());
                bundle.putString("Shelter Name", selectedShelter.getName());
                bundle.putString("Special Notes", selectedShelter.getSpecialNotes());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }
}
