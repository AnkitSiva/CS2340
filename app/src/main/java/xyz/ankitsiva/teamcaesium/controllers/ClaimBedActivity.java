package xyz.ankitsiva.teamcaesium.controllers;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import xyz.ankitsiva.teamcaesium.R;
import xyz.ankitsiva.teamcaesium.model.Shelter;
import xyz.ankitsiva.teamcaesium.model.Vacancy;

public class ClaimBedActivity extends AppCompatActivity {


    private Intent intent;
    private Shelter shelter;
    private TextView mView;
    private Vacancy vacancy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_claim_bed);
        intent = getIntent();
        shelter = intent.getParcelableExtra("Shelter");
        vacancy = shelter.getVacancies();
        mView = findViewById(R.id.Vacancies);
        mView.setText("Vacancies:   " + vacancy.getBeds());
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
}
