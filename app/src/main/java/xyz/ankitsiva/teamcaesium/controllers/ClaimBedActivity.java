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
import xyz.ankitsiva.teamcaesium.model.Vacancy;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ClaimBedActivity extends AppCompatActivity {


    private Intent intent;
    private Shelter shelter;
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
        Log.d("HELLO", "ASDAFDSFSDF");
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

        } else {
            //shelter.writeToParcel(Parcel.obtain(), Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
            writeVacancy(Integer.toString(shelter.getKey()), vacancy.getBeds());
            intent.putExtra("Shelter", shelter);
            Context context = getApplicationContext();
            CharSequence text2 = "Beds claimed!";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text2, duration);
            toast.show();
            onBackPressed();
        }
    }

    private void writeVacancy(String key, int beds) {
        mDatabase.child("shelters").child(key).child("Vacancies").setValue(beds);
    }
}
