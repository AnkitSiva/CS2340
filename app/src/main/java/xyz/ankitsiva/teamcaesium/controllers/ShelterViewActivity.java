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
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

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
            new GenericTypeIndicator<ArrayList<HashMap<String, Object>>>() {};
    private ArrayList<HashMap<String, Object>> dataList;
    private Iterator<HashMap<String, Object>> dataIterator;
    private ArrayList<Shelter> shelterList;
    private Intent intent;
    private User user;

    private final String[] chosenAge = new String[1];
    private final String[] chosenGender = new String[1];
    private final CharSequence[] userQuery = new CharSequence[1];

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
        userQuery[0] = "";
        final ArrayList<Shelter> backup = shelterList;
        EditText inputSearch = findViewById(R.id.inputSearch);

        Spinner ageSpinner = findViewById(R.id.ageSpinner);
        Spinner genderSpinner = findViewById(R.id.genderSpinner);

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

        final ArrayAdapter<Shelter> shelterArrayAdapter = new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_list_item_1, backup);

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReferenceFromUrl(
                "https://cs2340-49af4.firebaseio.com/");
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
                mutateList();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Failed to read value
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
                mutateList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                chosenAge[0] = "";
                mutateList();
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
                mutateList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //No Change
                chosenGender[0] = "";
                mutateList();
            }
        });

        inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                userQuery[0] = cs;
                mutateList();
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                userQuery[0] = "";
                mutateList();
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                mutateList();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("ViewActivity", "rq code = " + Integer.toString(requestCode) +
                "result = " + resultCode );
        super.onActivityResult(requestCode, resultCode, data);

        if((requestCode == 1) && (resultCode == Activity.RESULT_OK)){
            user = data.getParcelableExtra("User");
            Log.d("ViewActivity", "User got updated");
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

    private void mutateList() {
        List<Shelter> tempShelterList = new ArrayList<>();
        Log.w(TAG, "mutateList: " + userQuery[0]);
        for (Shelter shelter : shelterList) {
            if (shelter.getRestrictions().contains(chosenAge[0]) &&
                    shelter.getRestrictions().contains(chosenGender[0]) &&
                    shelter.getName().contains(userQuery[0])) {
                tempShelterList.add(shelter);
            }
        }
        ListAdapter shelterArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, tempShelterList);
        listView.setAdapter(shelterArrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                android.os.Parcelable selectedShelter = (Shelter) parent.getItemAtPosition(position);
                Intent intent = new Intent(getApplicationContext(), ShelterContentActivity.class);
                intent.putExtra("Shelter", selectedShelter);
                intent.putExtra("User", user);
                startActivityForResult(intent, 1);
            }
        });
    }
}
