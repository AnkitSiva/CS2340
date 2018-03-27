package xyz.ankitsiva.teamcaesium.controllers;

import android.app.Activity;
import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;

import xyz.ankitsiva.teamcaesium.R;
import xyz.ankitsiva.teamcaesium.model.Shelter;
import xyz.ankitsiva.teamcaesium.model.User;
import xyz.ankitsiva.teamcaesium.model.Vacancy;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ClaimBedActivity extends AppCompatActivity {


    private Intent intent;
    private Shelter shelter;
    private User user;
    private TextView mView;
    private Vacancy vacancy;
    private EditText mEdit;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_claim_bed);
        intent = getIntent();
        shelter = intent.getParcelableExtra("Shelter");
        user = intent.getParcelableExtra("User");
        vacancy = shelter.getVacancies();
        mView = findViewById(R.id.Vacancies);
        mView.setText("Vacancies:   " + vacancy.getBeds());
        mEdit = findViewById(R.id.NumberBeds);
        mDatabase = FirebaseDatabase.getInstance().getReferenceFromUrl(
                "https://cs2340-49af4.firebaseio.com/");

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



    public void claimBed(View view) {
        String text = mEdit.getText().toString();
        int num = 0;
        try {
            num = Integer.parseInt(text);
        } catch (Exception e) {
            Context context = getApplicationContext();
            CharSequence text2 = "Unable to claim beds";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text2, duration);
            toast.show();
        }
        if (!vacancy.claimBed(num)) {
            Context context = getApplicationContext();
            CharSequence text2 = "Unable to claim beds";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text2, duration);
            toast.show();
        } else if (!user.getShelterKey().equals("-1")){
            Context context = getApplicationContext();
            CharSequence text2 = "You already have a reservation for a shelter";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text2, duration);
            toast.show();
        } else {
            //shelter.writeToParcel(Parcel.obtain(), Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
            user.addReservation(shelter, num);
            writeVacancy(Integer.toString(shelter.getKey()), vacancy.getBeds(), user.getKey(), user.getClaimed());
            Context context = getApplicationContext();
            CharSequence text2 = "Beds claimed!";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text2, duration);
            toast.show();
            Intent newIntent = new Intent(this, MainActivity.class);
            newIntent.putExtra("Shelter", shelter);
            newIntent.putExtra("User", user);
            startActivity(newIntent);
            finish();
        }
    }

    private void writeVacancy(String key, int beds, String userKey, int claimed) {
        mDatabase.child("shelters").child(key).child("Vacancies").setValue(beds);
        mDatabase.child("users").child(userKey).child("Shelter").setValue(key);
        mDatabase.child("users").child(userKey).child("Beds").setValue(claimed);
    }
}
