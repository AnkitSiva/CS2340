package xyz.ankitsiva.teamcaesium.controllers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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
import xyz.ankitsiva.teamcaesium.model.Shelter;


public class ShelterViewActivity extends AppCompatActivity {


    public static final String TAG = ShelterViewActivity.class.getName();
    public ArrayList<TextView> shelterViews;
    public ListView listView;
    public Bundle bundle;
    private GenericTypeIndicator<ArrayList<HashMap<String, Object>>> t =
            new GenericTypeIndicator<ArrayList<HashMap<String, Object>>>() {};
    private ArrayList<HashMap<String, Object>> dataList;
    private HashMap<String, Object> shelter;
    private DatabaseReference mDatabase;
    private Iterator<HashMap<String, Object>> dataIterator;
    private Iterator<ListView> viewIterator;
    private ArrayList<Shelter> shelterList;
    private boolean isDone = false;
    //private ArrayList<TextView> shelters = new ArrayList<TextView>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shelter_view);
        listView = findViewById(R.id.listview);
        shelterList = new ArrayList<>();
        //initializeViews();
        //viewIterator = shelterViews.iterator();
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
                    //dataIterator = dataIterator.next();
                }
                /*while (viewIterator.hasNext()) {
                    listView = viewIterator.next();
                    shelter = dataIterator.next();
                    //listView.setText((String) shelter.get("Shelter Name"));
                }*/
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", databaseError.toException());
            }
        });

        ArrayAdapter<Shelter> shelterArrayAdapter = new ArrayAdapter<>(this, R.layout.activity_shelter_search, shelterList);
        listView.setAdapter(shelterArrayAdapter);
    }

    /*private void initializeViews() {
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
    }*/


}
