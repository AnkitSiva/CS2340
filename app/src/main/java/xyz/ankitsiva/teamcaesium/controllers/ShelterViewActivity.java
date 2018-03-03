package xyz.ankitsiva.teamcaesium.controllers;

import android.content.Intent;
import android.nfc.Tag;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import xyz.ankitsiva.teamcaesium.R;


public class ShelterViewActivity extends AppCompatActivity {

    public static class Post {

        public String name;

        public Post(String name) {
            this.name = name;
        }

    }
    public static final String TAG = "ShelterViewActivity";
    GenericTypeIndicator<ArrayList<HashMap<String, Object>>> t =
            new GenericTypeIndicator<ArrayList<HashMap<String, Object>>>() {};
    private DatabaseReference mDatabase;
    //private ArrayList<TextView> shelters = new ArrayList<TextView>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shelter_view);

        mDatabase = FirebaseDatabase.getInstance().getReferenceFromUrl(
                "https://cs2340-49af4.firebaseio.com/");

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                ArrayList<HashMap<String, Object>> values =  dataSnapshot.getValue(t);
                Log.d(TAG, "Value is: " + values);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", databaseError.toException());
            }
        });

    }

    //Will need bundle or extras along with the Intent to pass shelter information over
    public void Contacts(View view) {
        Intent intent = new Intent(this, ShelterContentActivity.class);
        startActivity(intent);
    }


}
