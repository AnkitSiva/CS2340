package xyz.ankitsiva.teamcaesium.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by kevin on 3/3/2018.
 */

class ShelterData {
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReferenceFromUrl(
                "https://cs2340-49af4.firebaseio.com/");

}
