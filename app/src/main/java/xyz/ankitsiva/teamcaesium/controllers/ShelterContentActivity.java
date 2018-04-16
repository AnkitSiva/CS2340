package xyz.ankitsiva.teamcaesium.controllers;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import xyz.ankitsiva.teamcaesium.R;
import xyz.ankitsiva.teamcaesium.model.Shelter;
import xyz.ankitsiva.teamcaesium.model.User;

/**
 * shows each shelter's content and attributes
 */
public class ShelterContentActivity extends AppCompatActivity {

    private Intent intent;
    private Shelter shelter;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("this", "this");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shelter_content);

        intent = getIntent();
        shelter = intent.getParcelableExtra("Shelter");
        if (shelter != null) {
            initializeViews();
        }
        user = intent.getParcelableExtra("User");
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("hey", "rq code = " + Integer.toString(requestCode) + "result = " + resultCode );
        super.onActivityResult(requestCode, resultCode, data);

        if((requestCode == 1) && (resultCode == Activity.RESULT_OK)){
            shelter = data.getParcelableExtra("Shelter");
            user = data.getParcelableExtra("User");
            Log.d("Content", "shelter got updated");
            intent.putExtra("Shelter", shelter);
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

    private void initializeViews() {
        TextView mView;
        mView = findViewById(R.id.shelterName);
        mView.setText("Name:    " + shelter.getName());
        mView = findViewById(R.id.shelterAddress);
        mView.setText("Address:    " + shelter.getAddress());
        mView = findViewById(R.id.shelterCapacity);
        mView.setText("Capacity:    " + shelter.getCapacity());
        mView = findViewById(R.id.shelterPhone);
        mView.setText("Phone#:    " + shelter.getPhoneNumber());
        mView = findViewById(R.id.shelterLatitude);
        mView.setText("Latitude:    " + shelter.getLatitude());
        mView = findViewById(R.id.shelterLongitude);
        mView.setText("Longitude:    " + shelter.getLongitude());
        mView = findViewById(R.id.shelterRestrictions);
        mView.setText("Restrictions:    " + shelter.getRestrictions());
        mView = findViewById(R.id.shelterNotes);
        mView.setText("Special Notes:    " + shelter.getSpecialNotes());
    }

    /**
     * claim bed button for the shelter
     * @param view the view for the claim bed button
     */
    public void claimBed(View view) {
        Intent intent = new Intent(this, ClaimBedActivity.class);
        intent.putExtra("Shelter", shelter);
        intent.putExtra("User", user);
        startActivityForResult(intent, 1);
    }
}
