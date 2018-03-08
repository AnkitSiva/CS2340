package xyz.ankitsiva.teamcaesium.model;

import java.util.HashMap;

/**
 * Created by ankitsiva on 3/7/18.
 */

public class Shelter {
    private String name;
    private String address;
    private String capacity;
    private String phoneNumber;
    private String latitude;
    private String longitude;
    private String restrictions;
    private String specialNotes;

    public Shelter(HashMap<String, Object> shelter) {
        this.name = shelter.get("Shelter Name").toString();
        this.address = shelter.get("Address").toString();
        this.capacity = shelter.get("Capacity").toString();
        this.phoneNumber = shelter.get("Phone Number").toString();
        this.latitude = shelter.get("Latitude ").toString();
        this.longitude = shelter.get("Longitude ").toString();
        this.restrictions = shelter.get("Restrictions").toString();
        this.specialNotes = shelter.get("Special Notes").toString();

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

    public String toString() {return this.getName();}
}
