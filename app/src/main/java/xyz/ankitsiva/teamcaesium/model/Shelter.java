package xyz.ankitsiva.teamcaesium.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Map;

/**
 * Created by ankitsiva on 3/7/18.
 */

public class Shelter implements Parcelable{
    private final String name;
    private final String address;
    private final String capacity;
    private final String phoneNumber;
    private final String latitude;
    private final String longitude;
    private final String restrictions;
    private final String specialNotes;
    private final int key;
    private final Vacancy vacancies;

    public Shelter(Map<String, Object> shelter) {
        this.name = shelter.get("Shelter Name").toString();
        this.address = shelter.get("Address").toString();
        this.capacity = shelter.get("Capacity").toString();
        this.phoneNumber = shelter.get("Phone Number").toString();
        this.latitude = shelter.get("Latitude ").toString();
        this.longitude = shelter.get("Longitude ").toString();
        this.restrictions = shelter.get("Restrictions").toString();
        this.specialNotes = shelter.get("Special Notes").toString();
        this.key = Integer.parseInt(shelter.get("Unique Key").toString());
        this.vacancies = new Vacancy(Integer.valueOf(shelter.get("Max Vacancies").toString()),
                Integer.valueOf(shelter.get("Vacancies").toString()));
    }

    //What the fuck?
    public Shelter() {
        this.name = "Shelter";
        this.address = "Default address";
        this.capacity = "100";
        this.phoneNumber = "555-555-5555";
        this.latitude = "0";
        this.longitude = "0";
        this.restrictions = "None";
        this.specialNotes = "Special Notes";
        this.key = -1;
        this.vacancies = new Vacancy(100, 100);
    }

    private Shelter(Parcel in) {
        name = in.readString();
        address = in.readString();
        capacity = in.readString();
        phoneNumber = in.readString();
        latitude = in.readString();
        longitude = in.readString();
        restrictions = in.readString();
        specialNotes = in.readString();
        key = in.readInt();
        vacancies = in.readParcelable(Vacancy.class.getClassLoader());
    }
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(name);
        out.writeString(address);
        out.writeString(capacity);
        out.writeString(phoneNumber);
        out.writeString(latitude);
        out.writeString(longitude);
        out.writeString(restrictions);
        out.writeString(specialNotes);
        out.writeInt(key);
        out.writeParcelable(vacancies, flags);
    }

    public static final Parcelable.Creator<Shelter> CREATOR
            = new Parcelable.Creator<Shelter>() {
        @Override
        public Shelter createFromParcel(Parcel in) {
            return new Shelter(in);
        }

        @Override
        public Shelter[] newArray(int size) {
            return new Shelter[size];
        }
    };

    public void releaseBed(int beds) {
        vacancies.releaseBed(beds);
    }

    public int getBeds() {
        return vacancies.getBeds();
    }

    public String getName() {
        return this.name;
    }

    public String getAddress() {
        return this.address;
    }

    public String getCapacity() {
        return this.capacity;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public String getLatitude() {
        return this.latitude;
    }

    public String getLongitude() {
        return this.longitude;
    }

    public String getRestrictions() {
        return this.restrictions;
    }

    public String getSpecialNotes() {
        return this.specialNotes;
    }

    public Vacancy getVacancies() { return this.vacancies; }

    public int getKey() { return this.key; }

    public String toString() { return this.getName();}
}
