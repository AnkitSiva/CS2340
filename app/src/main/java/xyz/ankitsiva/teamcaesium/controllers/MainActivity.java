package xyz.ankitsiva.teamcaesium.controllers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import xyz.ankitsiva.teamcaesium.R;
import xyz.ankitsiva.teamcaesium.model.UserList;

public class MainActivity extends AppCompatActivity {

    private UserList users;
    private TextView mText;
    private Intent intent;
    private Bundle bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        users = RegisterActivity.users;
        intent = getIntent();
        bundle = intent.getExtras();
        mText = findViewById(R.id.welcomeText);
        mText.setText("Welcome " + bundle.get("name") + " - " + bundle.get("userType"));
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


}
