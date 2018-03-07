package xyz.ankitsiva.teamcaesium.model;

import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by kevin on 3/3/2018.
 */

public class ShelterData {
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReferenceFromUrl(
                "https://cs2340-49af4.firebaseio.com/");

}
