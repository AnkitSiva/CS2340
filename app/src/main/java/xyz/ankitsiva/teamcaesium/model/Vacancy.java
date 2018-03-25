package xyz.ankitsiva.teamcaesium.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kevin on 3/24/2018.
 */

public class Vacancy implements Parcelable{
    private int maximum;
    private int beds;

    public Vacancy(int maximum, int beds) {
        this.maximum = maximum;
        this.beds = beds;
    }

    public Vacancy(Parcel in) {
        maximum = in.readInt();
        beds = in.readInt();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(maximum);
        out.writeInt(beds);
    }

    public static final Parcelable.Creator<Vacancy> CREATOR
            = new Parcelable.Creator<Vacancy>() {
        public Vacancy createFromParcel(Parcel in) {
            return new Vacancy(in);
        }
        public Vacancy[] newArray(int size) {
            return new Vacancy[size];
        }
    };

    public boolean claimBed(int amount) {
        if (beds - amount >= 0) {
            beds = beds = amount;
            return true;
        } else {
            return false;
        }
    }

    public boolean releaseBed(int amount) {
        if (beds + amount <= maximum) {
            beds = beds + amount;
            return true;
        } else {
            return false;
        }
    }

    public int getMaximum() {
        return maximum;
    }

    public int getBeds() {
        return beds;
    }
}
