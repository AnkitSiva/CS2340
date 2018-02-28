package xyz.ankitsiva.teamcaesium.controllers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import xyz.ankitsiva.teamcaesium.R;

public class ShelterViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shelter_view);
    }

    //Will need bundle or extras along with the Intent to pass shelter information over
    public void Contacts(View view) {
        Intent intent = new Intent(this, ShelterContentActivity.class);
        startActivity(intent);
    }
}
