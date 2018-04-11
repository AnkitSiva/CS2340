package xyz.ankitsiva.teamcaesium.controllers;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
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
import java.util.Iterator;
import java.util.List;

import xyz.ankitsiva.teamcaesium.R;
import xyz.ankitsiva.teamcaesium.model.Shelter;
import xyz.ankitsiva.teamcaesium.model.User;

public class MainActivity extends AppCompatActivity {

    private User user;
    private TextView mText;
    private TextView mVacancyText;
    private Intent intent;
    @Nullable
    private Shelter shelter;
    private String name, userType;
    private List<HashMap<String, Object>> dataList;
    private Iterator<HashMap<String, Object>> dataIterator;
    private GenericTypeIndicator<ArrayList<HashMap<String, Object>>> t =
            new GenericTypeIndicator<ArrayList<HashMap<String, Object>>>() {};
    private List<Shelter> shelterList;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        intent = getIntent();
        user = intent.getParcelableExtra("User");
        Log.d("MainActivity", "User is " + user);
        mText = findViewById(R.id.welcomeText);
        mVacancyText = findViewById(R.id.ReservationText);
        name = user.getUsername();
        userType = "user";
        shelterList = new ArrayList<>();
        mDatabase = FirebaseDatabase.getInstance().getReferenceFromUrl(
                "https://cs2340-49af4.firebaseio.com/");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Log.d("MainActivity", "Database is called");
                dataList = dataSnapshot.child("shelters").getValue(t);
                try {
                    dataIterator = dataList.iterator();
                } catch (Exception e) {

                }
                while (dataIterator.hasNext()) {
                    Shelter shelter = new Shelter(dataIterator.next());
                    shelterList.add(shelter);
                }
                if (!user.getShelterKey().equals("-1")) {
                    shelter = shelterList.get(Integer.parseInt(user.getShelterKey()));
                }
                setAllText();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Failed to read value
                Log.d("MainActivity", "Failed to read value.", databaseError.toException());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("MainActivity", "rq code = " + Integer.toString(requestCode) + "result = " + resultCode );
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == Activity.RESULT_OK){
            user = data.getParcelableExtra("User");
            Log.d("MainActivity", "User got updated");
            intent.putExtra("User", user);
            if (!user.getShelterKey().equals("-1")) {
                shelter = shelterList.get(Integer.parseInt(user.getShelterKey()));
            }
        }
        setAllText();
    }

    public void logout(View view) {
        Intent intent = new Intent(this, WelcomeActivity.class);
        startActivity(intent);
        finish();
    }

    public void releaseBeds(View view) {
        if (shelter != null) {
            user.addReservation(shelter, user.getClaimed());
            user.releaseBeds();
            writeUpdate(Integer.toString(shelter.getKey()), shelter.getVacancies().getBeds(), user.getKey());
            shelter = null;
        }
        Intent refresh = new Intent(this, MainActivity.class);
        refresh.putExtra("User", user);
        startActivity(refresh);
        this.finish();
    }

    public void writeUpdate(String shelterKey, int beds, String userKey) {
        mDatabase.child("shelters").child(shelterKey).child("Vacancies").setValue(beds);
        mDatabase.child("users").child(userKey).child("Shelter").setValue(-1);
        mDatabase.child("users").child(userKey).child("Beds").setValue(0);
    }

    public void viewShelterList(View view) {
        Intent intent = new Intent(this, ShelterViewActivity.class);
        intent.putExtra("User", user);
        startActivityForResult(intent, 1);
    }

    public void searchShelters(View view) {
        Intent intent = new Intent(this, ShelterSearchActivity.class);
        startActivity(intent);
    }

    public void setAllText() {
        mText.setText("Welcome " + name + " - " + userType);
        Log.d("MainActivity", "Shelter key is " + user.getShelterKey());
        if (shelter != null) {
            mVacancyText.setText("Current Reservation: " + user.getClaimed() +
                    " beds at " + shelter.getName());
        } else {
            mVacancyText.setText("No current reservations.");
        }
    }

    public void viewShelterMap(View view) {
        Intent intent = new Intent(this, MapViewActivity.class);
        intent.putExtra("User", user);
        startActivityForResult(intent, 1);
    }
}
