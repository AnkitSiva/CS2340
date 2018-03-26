package xyz.ankitsiva.teamcaesium.controllers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import xyz.ankitsiva.teamcaesium.R;
import xyz.ankitsiva.teamcaesium.model.User;
import xyz.ankitsiva.teamcaesium.model.UserList;

public class MainActivity extends AppCompatActivity {

    private User user;
    private TextView mText;
    private Intent intent;
    private String name, userType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        intent = getIntent();
        user = intent.getParcelableExtra("User");
        mText = findViewById(R.id.welcomeText);
        name = user.getUsername();
        userType = "user";
        mText.setText("Welcome " + name + " - " + userType);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("name", name);
        outState.putString("userType", userType);


        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        name = savedInstanceState.getString("name");
        userType = savedInstanceState.getString("userType");
    }

    public void logout(View view) {
        Intent intent = new Intent(this, WelcomeActivity.class);
        startActivity(intent);
        finish();
    }

    public void viewShelterList(View view) {
        Intent intent = new Intent(this, ShelterViewActivity.class);
        startActivity(intent);
    }

    public void searchShelters(View view) {
        Intent intent = new Intent(this, ShelterSearchActivity.class);
        startActivity(intent);
    }
}
