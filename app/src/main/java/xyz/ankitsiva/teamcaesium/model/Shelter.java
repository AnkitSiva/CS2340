package xyz.ankitsiva.teamcaesium.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;

/**
 * Created by ankitsiva on 3/7/18.
 */

public class Shelter implements Parcelable{
    private String name;
    private String address;
    private String capacity;
    private String phoneNumber;
    private String latitude;
    private String longitude;
    private String restrictions;
    private String specialNotes;
    private Vacancy vacancies;

    public Shelter(HashMap<String, Object> shelter) {
        this.name = shelter.get("Shelter Name").toString();
        this.address = shelter.get("Address").toString();
        this.capacity = shelter.get("Capacity").toString();
        this.phoneNumber = shelter.get("Phone Number").toString();
        this.latitude = shelter.get("Latitude ").toString();
        this.longitude = shelter.get("Longitude ").toString();
        this.restrictions = shelter.get("Restrictions").toString();
        this.specialNotes = shelter.get("Special Notes").toString();
        this.vacancies = new Vacancy(Integer.valueOf(shelter.get("Max Vacancies").toString()),
                Integer.valueOf(shelter.get("Vacancies").toString()));
    }

    public Shelter(boolean ye) {
        if(ye) {
            this.name =
            this.address =
            this.capacity =
            this.phoneNumber =
            this.latitude =
            this.longitude =
            this.restrictions =
            this.specialNotes = "Special Notes";
        }
    }

    public Shelter(Parcel in) {
        name = in.readString();
        address = in.readString();
        capacity = in.readString();
        phoneNumber = in.readString();
        latitude = in.readString();
        longitude = in.readString();
        restrictions = in.readString();
        specialNotes = in.readString();
        vacancies = in.readParcelable(Vacancy.class.getClassLoader());
    }
    public int describeContents() {
        return 0;
    }
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(name);
        out.writeString(address);
        out.writeString(capacity);
        out.writeString(phoneNumber);
        out.writeString(latitude);
        out.writeString(longitude);
        out.writeString(restrictions);
        out.writeString(specialNotes);
        out.writeParcelable(vacancies, flags);
    }

    public static final Parcelable.Creator<Shelter> CREATOR
            = new Parcelable.Creator<Shelter>() {
        public Shelter createFromParcel(Parcel in) {
            return new Shelter(in);
        }

        public Shelter[] newArray(int size) {
            return new Shelter[size];
        }
    };

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

    public String toString() { return this.getName();}
}
