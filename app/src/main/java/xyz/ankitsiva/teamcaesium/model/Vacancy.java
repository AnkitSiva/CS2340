package xyz.ankitsiva.teamcaesium.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kevin on 3/24/2018.
 */

public class Vacancy implements Parcelable{
    private final int maximum;
    private int beds;

    public Vacancy(int maximum, int beds) {
        this.maximum = maximum;
        this.beds = beds;
    }

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

    public boolean claimBed(int amount) {
        if ((beds - amount) >= 0) {
            beds = beds - amount;
            return true;
        } else {
            return false;
        }
    }

    public void releaseBed(int amount) {
        if ((beds + amount) <= maximum) {
            beds = beds + amount;
        } else {
        }
    }

    public int getMaximum() {
        return maximum;
    }

    public int getBeds() {
        return beds;
    }
}
