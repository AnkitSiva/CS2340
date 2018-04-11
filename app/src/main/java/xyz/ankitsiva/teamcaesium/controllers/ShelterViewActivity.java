package xyz.ankitsiva.teamcaesium.controllers;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;

import xyz.ankitsiva.teamcaesium.R;
import xyz.ankitsiva.teamcaesium.model.AgeCategories;
import xyz.ankitsiva.teamcaesium.model.Gender;
import xyz.ankitsiva.teamcaesium.model.Shelter;
import xyz.ankitsiva.teamcaesium.model.User;


/**
 * Controller for viewing shelters
 */
public class ShelterViewActivity extends AppCompatActivity {
    private static final String TAG = ShelterViewActivity.class.getName();
    private ListView listView;
    private final GenericTypeIndicator<ArrayList<HashMap<String, Object>>> t =
            new GenericTypeIndicator<>();
    private ArrayList<HashMap<String, Object>> dataList;
    private Iterator<HashMap<String, Object>> dataIterator;
    private ArrayList<Shelter> shelterList;
    private Intent intent;
    private User user;

    private final int NUM_AGE_CATEGORIES = 5;
    private final int NUM_GENDER_CATEGORIES = 3;

    /**
     * @param savedInstanceState Stuff that gets passed to the method
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intent = getIntent();
        user = intent.getParcelableExtra("User");
        setContentView(R.layout.activity_shelter_view);

        listView = findViewById(R.id.listview);
        shelterList = new ArrayList<>();
        final ArrayList<Shelter> backup = shelterList;
        EditText inputSearch = findViewById(R.id.inputSearch);

        Spinner ageSpinner = findViewById(R.id.ageSpinner);
        Spinner genderSpinner = findViewById(R.id.genderSpinner);

        ArrayList<String> ageCategoryStrings = new ArrayList<>(NUM_AGE_CATEGORIES);
        for (AgeCategories value: AgeCategories.values()) {
            ageCategoryStrings.add(value.getAgeCat());
        }

        ArrayList<String> genderCategoryStrings = new ArrayList<>(NUM_GENDER_CATEGORIES);
        for (Gender value: Gender.values()) {
            genderCategoryStrings.add(value.getGender());
        }

        ArrayAdapter<String> ageArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, ageCategoryStrings);
        ArrayAdapter<String> genderArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, genderCategoryStrings);

        ageSpinner.setAdapter(ageArrayAdapter);
        genderSpinner.setAdapter(genderArrayAdapter);

        final ArrayList<ArrayAdapter<Shelter>> shelterArrayAdapter = new ArrayList<>(1);

        shelterArrayAdapter.add(new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_list_item_1, backup));

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReferenceFromUrl(
                "https://cs2340-49af4.firebaseio.com/");
        Log.d(TAG, "onCreate: 1");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                dataList =  dataSnapshot.child("shelters").getValue(t);
                dataIterator = Objects.requireNonNull(dataList).iterator();
                while (dataIterator.hasNext()) {
                    Shelter shelter = new Shelter(dataIterator.next());
                    shelterList.add(shelter);
                }
                listView.setAdapter(shelterArrayAdapter.get(0));
                Log.d(TAG, "onDataChange: 2");
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", databaseError.toException());
            }
        });

        Log.d(TAG, "onCreate: 3");
        ageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ArrayList<Shelter> filteredShelterList = new ArrayList<>(backup);
                ArrayAdapter<Shelter> filteredShelterListAdapter;
                if (l == 0) {
                    for (Shelter shelter : new ArrayList<>(filteredShelterList)) {
                        if (shelter.getRestrictions().length() == 0) {
                            filteredShelterList.remove(shelter);
                        }
                    }
                } else if (l == 1) {
                    for (Shelter shelter : new ArrayList<>(filteredShelterList)) {
                        if(!shelter.getRestrictions().contains("Fam")) {
                            filteredShelterList.remove(shelter);
                        }
                    }
                } else if (l == 2) {
                    for (Shelter shelter : new ArrayList<>(filteredShelterList)) {
                        if(!shelter.getRestrictions().contains(AgeCategories.CHILDREN.getAgeCat()))
                        {
                            filteredShelterList.remove(shelter);
                        }
                    }
                } else if (l == 3) {
                    for (Shelter shelter : new ArrayList<>(filteredShelterList)) {
                        if(!shelter.getRestrictions().contains("You")) {
                            filteredShelterList.remove(shelter);
                        }
                    }
                }

                Log.d(TAG, "onItemSelected: 4");
                filteredShelterListAdapter = new ArrayAdapter<>(getApplicationContext(),
                        android.R.layout.simple_spinner_dropdown_item, filteredShelterList);
                shelterArrayAdapter.remove(0);
                shelterArrayAdapter.add(filteredShelterListAdapter);
                listView.setAdapter(shelterArrayAdapter.get(0));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Log.d(TAG, "onNothingSelected: 4");
                ArrayList<Shelter> filteredShelterList = new ArrayList<>(backup);
                ArrayAdapter<Shelter> filteredShelterListAdapter;
                filteredShelterListAdapter = new ArrayAdapter<>(getApplicationContext(),
                        android.R.layout.simple_spinner_dropdown_item, filteredShelterList);
                shelterArrayAdapter.remove(0);
                shelterArrayAdapter.add(filteredShelterListAdapter);
                listView.setAdapter(shelterArrayAdapter.get(0));
                return;
            }
        });

        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ArrayList<Shelter> filteredShelterList = new ArrayList<>(backup);
                ArrayAdapter<Shelter> filteredShelterListAdapter;
                if (l == 0) {
                    for (Shelter shelter: new ArrayList<>(filteredShelterList)) {
                        if(shelter.getRestrictions().length() == 0) {
                            filteredShelterList.remove(shelter);
                        }
                    }
                } else if (l == 1) {
                    for (Shelter shelter : new ArrayList<>(filteredShelterList)) {
                        if(!shelter.getRestrictions().contains("Men")) {
                            filteredShelterList.remove(shelter);
                        }
                    }
                } else if (l == 2) {
                    for (Shelter shelter : new ArrayList<>(filteredShelterList)) {
                        if (!shelter.getRestrictions().contains("Women")) {
                            filteredShelterList.remove(shelter);
                        }
                    }
                }
                Log.d(TAG, "onItemSelected: 5");
                filteredShelterListAdapter = new ArrayAdapter<>(getApplicationContext(),
                        android.R.layout.simple_spinner_dropdown_item, filteredShelterList);
                shelterArrayAdapter.remove(0);
                shelterArrayAdapter.add(filteredShelterListAdapter);
                listView.setAdapter(shelterArrayAdapter.get(0));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //No Change
                ArrayList<Shelter> filteredShelterList = new ArrayList<>(backup);
                ArrayAdapter<Shelter> filteredShelterListAdapter;
                filteredShelterListAdapter = new ArrayAdapter<>(getApplicationContext(),
                        android.R.layout.simple_spinner_dropdown_item, filteredShelterList);
                shelterArrayAdapter.remove(0);
                shelterArrayAdapter.add(filteredShelterListAdapter);
                listView.setAdapter(shelterArrayAdapter.get(0));
                Log.d(TAG, "onNothingSelected: 6");
                return;
            }
        });

        inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                Log.d(TAG, "onTextChanged: 7");
                shelterArrayAdapter.get(0).getFilter().filter(cs);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                Log.d(TAG, "beforeTextChanged: 8");
                return;
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                Log.d(TAG, "afterTextChanged: 8");
                return;
            }
        });

        Log.d(TAG, "onCreate: 9");
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Shelter selectedShelter = (Shelter) parent.getItemAtPosition(position);
                Intent intent = new Intent(getApplicationContext(), ShelterContentActivity.class);
                intent.putExtra("Shelter", selectedShelter);
                intent.putExtra("User", user);
                startActivityForResult(intent, 1);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("ViewActivity", "rq code = " + Integer.toString(requestCode) +
                "result = " + resultCode );
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == Activity.RESULT_OK){
            user = data.getParcelableExtra("User");
            Log.d("ViewActivity", "User got updated");
            Shelter updShelter = data.getParcelableExtra("Shelter");
            intent.putExtra("User", user);
        }
    }

    @Override
    public void onBackPressed() {
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
}
