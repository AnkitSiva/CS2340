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
    @Nullable
    private Reservation reservation;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        shelterKey = "-1";
    }

    private class Reservation {
        final Shelter shelter;
        final int beds;

        Reservation(Shelter shelter, int beds) {
            this.shelter = shelter;
            this.beds = beds;
        }
    }

    public void addReservation(Shelter shelter, int beds) {
        reservation = new Reservation(shelter, beds);
        shelterKey = Integer.toString(shelter.getKey());
        claimed = beds;
    }

    public Shelter releaseBeds() {
        if (reservation == null) {
            return null;
        } else {
            reservation.shelter.getVacancies().releaseBed(reservation.beds);
            Shelter cur = reservation.shelter;
            reservation = null;
            shelterKey = "-1";
            claimed = 0;
            return cur;
        }
    }

    public User(Map<String, Object> user) {
        this.username = user.get("Username").toString();
        this.password = user.get("Password").toString();
        this.key = user.get("Key").toString();
        this.shelterKey = user.get("Shelter").toString();
        this.claimed = Integer.parseInt(user.get("Beds").toString());
    }

    private User(Parcel in) {
        username = in.readString();
        password = in.readString();
        key = in.readString();
        shelterKey = in.readString();
        claimed = in.readInt();
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

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getShelterKey() {
        return shelterKey;
    }

    public int getClaimed() {
        return claimed;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setClaimed(int claimed) {
        this.claimed = claimed;
    }
    public boolean checkUser(String username) {
        return this.username.equals(username);
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

}


