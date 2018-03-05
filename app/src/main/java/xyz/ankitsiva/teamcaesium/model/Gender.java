package xyz.ankitsiva.teamcaesium.model;

/**
 * Created by ankitsiva on 3/4/18.
 */

public enum Gender {
    MALE ("Male"),
    FEMALE ("Female");

    private final String gender;

    Gender(String gender) {this.gender = gender;}

    @Override
    public String toString() {
        return super.toString();
    }
}
