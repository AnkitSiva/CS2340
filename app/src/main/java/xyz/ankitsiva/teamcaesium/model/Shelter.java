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

    /**
     * Shelter constructor
     * @param shelter a shelter from the database
     */
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

    /**
     * default shelter constructor
     */
    public Shelter() {
        name = "Default";
        address = "Address";
        capacity = "100";
        phoneNumber = "555-555-5555";
        latitude = "0";
        longitude = "0";
        restrictions = "None";
        specialNotes = "None";
        key = -2;
        vacancies = new Vacancy(100, 100);
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

    /**
     * releases beds at this shelter
     * @param beds the number of beds to be released
     */
    public void releaseBed(int beds) {
        vacancies.releaseBed(beds);
    }

    /**
     * gets the beds available at the shelter
     * @return the number of available beds at the shelter
     */
    public int getBeds() {
        return vacancies.getBeds();
    }

    /**
     * gets the shelter's name
     * @return the shelter's name
     */
    public String getName() {
        return this.name;
    }

    /**
     * gets the shelter's address
     * @return the shelter's address
     */
    public String getAddress() {
        return this.address;
    }

    /**
     * gets the shelter's capacity
     * @return the shelter's capacity
     */
    public String getCapacity() {
        return this.capacity;
    }

    /**
     * gets the shelter's phone number
     * @return the shelter's phone number
     */
    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    /**
     * gets the shelter's latitude
     * @return the shelter's latitude
     */
    public String getLatitude() {
        return this.latitude;
    }

    /**
     * gets the shelter's longitude
     * @return the shelter's longitude
     */
    public String getLongitude() {
        return this.longitude;
    }

    /**
     * gets the shelter's restrictions
     * @return the shelter's restrictions
     */
    public String getRestrictions() {
        return this.restrictions;
    }

    /**
     * gets the shelter's special notes
     * @return the shelter's special notes
     */
    public String getSpecialNotes() {
        return this.specialNotes;
    }

    /**
     * gets the shelter's vacancies
     * @return the shelter's vacancies
     */
    public Vacancy getVacancies() { return this.vacancies; }

    /**
     * gets the shelter's key for the database
     * @return the shelter's key
     */
    public int getKey() { return this.key; }

    @Override
    public String toString() { return this.getName();}
}
