package xyz.ankitsiva.teamcaesium.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import java.util.Map;

/**
 * Created by kevin on 3/26/2018.
 */

public class User implements Parcelable{
    private final String username;
    private final String password;
    private String key;
    private String shelterKey;
    private int claimed;
    private String type;
    private String banned;

    @Nullable
    private Reservation reservation;

    /**
     * constructor for a user
     * @param username the user's username
     * @param password the user's password
     */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.type = "User";
        shelterKey = "-1";
        banned = "Not Banned";
    }

    private class Reservation {
        final Shelter shelter;
        final int beds;

        Reservation(Shelter shelter, int beds) {
            this.shelter = shelter;
            this.beds = beds;
        }

        private void releaseBeds() {
            shelter.releaseBed(beds);
        }
    }

    /**
     * adds a reservation to the user's account
     * @param shelter the shelter where the reservation is
     * @param beds the number of beds reserved
     */
    public void addReservation(Shelter shelter, int beds) {
        if (shelter != null) {
            reservation = new Reservation(shelter, beds);
            shelterKey = Integer.toString(shelter.getKey());
            claimed = beds;
        }
    }

    /**
     * releases the user's reserved beds
     */
    public void releaseBeds() {
        if (reservation != null) {
            reservation.releaseBeds();
            reservation = null;
            shelterKey = "-1";
            claimed = 0;
        }
    }

    /**
     * constructor for user from the database
     * @param user the user in the database
     */
    public User(Map<String, Object> user) {
        this.username = user.get("Username").toString();
        this.password = user.get("Password").toString();
        this.key = user.get("Key").toString();
        this.shelterKey = user.get("Shelter").toString();
        this.claimed = Integer.parseInt(user.get("Beds").toString());
        this.type = user.get("Type").toString();
        this.banned = user.get("Banned").toString();
    }

    private User(Parcel in) {
        username = in.readString();
        password = in.readString();
        key = in.readString();
        shelterKey = in.readString();
        claimed = in.readInt();
        type = in.readString();
        banned = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(username);
        out.writeString(password);
        out.writeString(key);
        out.writeString(shelterKey);
        out.writeInt(claimed);
        out.writeString(type);
        out.writeString(banned);
    }

    public static final Parcelable.Creator<User> CREATOR
            = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    /**
     * gets the user's username
     * @return this user's username
     */
    public String getUsername() {
        return username;
    }

    /**
     * gets this user's password
     * @return this user's password
     */
    public String getPassword() {
        return password;
    }

    /**
     * gets the shelter key of the user's reserved shelter
     * @return the shelter key
     */
    public String getShelterKey() {
        return shelterKey;
    }

    /**
     * gets the number of beds the user has claimed
     * @return number of beds user has claimed
     */
    public int getClaimed() {
        return claimed;
    }

    /**
     * gets the user's current reservation
     * @return the user's current reservation
     */
    @Nullable
    public Reservation getReservation() {
        return reservation;
    }

    /**
     * gets the user key for the database
     * @return the user key
     */
    public String getKey() {
        return key;
    }

    public String getType() {
        return type;
    }

    public String getBanned() {
        return banned;
    }
    /**
     * sets the user's key
     * @param key the user's new key
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * checks if a user's username is valid
     * @param username the username
     * @return true if username is valid, false if not
     */
    public boolean checkUser(String username) {
        return this.username.equals(username);
    }

    /**
     * checks if a user's password is valid
     * @param password the password
     * @return true if password is valid, false if not
     */
    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

}


