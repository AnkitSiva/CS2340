package xyz.ankitsiva.teamcaesium.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kevin on 3/24/2018.
 */

public class Vacancy implements Parcelable{
    private final int maximum;
    private int beds;

    /**
     * constructor for a vacancy
     * @param maximum the max number of beds allowed
     * @param beds the beds currently available
     */
    public Vacancy(int maximum, int beds) {
        this.maximum = maximum;
        this.beds = beds;
    }

    /**
     * reads in vacancy
     * @param in input from another source
     */
    public Vacancy(Parcel in) {
        maximum = in.readInt();
        beds = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(maximum);
        out.writeInt(beds);
    }

    public static final Parcelable.Creator<Vacancy> CREATOR
            = new Parcelable.Creator<Vacancy>() {
        @Override
        public Vacancy createFromParcel(Parcel in) {
            return new Vacancy(in);
        }
        @Override
        public Vacancy[] newArray(int size) {
            return new Vacancy[size];
        }
    };

    /**
     * claims beds from this vacancy
     * @param amount the amount of beds to be claimed
     * @return true if the beds were claimed, false if not
     */
    public boolean claimBed(int amount) {
        if (amount <= 0) {
            return false;
        }
        if ((beds - amount) >= 0) {
            beds = beds - amount;
            return true;
        } else {
            return false;
        }
    }

    /**
     * releases reserved beds from this vacancy
     * @param amount amount of beds to be released
     */
    public void releaseBed(int amount) {
        if (amount <= 0) {
            return;
        }
        if ((beds + amount) <= maximum) {
            beds = beds + amount;
        }
    }

    /**
     * gets the number of beds available in this vacancy
     * @return number of beds available
     */
    public int getBeds() {
        return beds;
    }
}
