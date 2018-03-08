package xyz.ankitsiva.teamcaesium.controllers;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shelter_view);
        listView = findViewById(R.id.listview);
        shelterList = new ArrayList<>();
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
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", databaseError.toException());
            }
        });

        ArrayAdapter<Shelter> shelterArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, shelterList);
        listView.setAdapter(shelterArrayAdapter);
    }
}
