package xyz.ankitsiva.teamcaesium.model;

/**
 * Created by ankitsiva on 3/4/18.
 */

public enum Gender {
    NO_PREF ("No Preference"),
    MALE ("Male"),
    FEMALE ("Female");

    private final String gender;

    Gender(String gender) {this.gender = gender;}

    public String getGender() {
        return gender;
    }
}
