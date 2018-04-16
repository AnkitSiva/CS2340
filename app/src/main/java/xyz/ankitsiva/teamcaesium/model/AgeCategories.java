package xyz.ankitsiva.teamcaesium.model;

/**
 * Created by ankitsiva on 3/4/18.
 */

public enum AgeCategories {
    NO_PREF ("No Preference"),
    FAM_NEWBORN ("Families With Newborns"),
    CHILDREN ("Children"),
    YA ("Young Adult");

    private final String ageCat;

    AgeCategories(String ageCat) {this.ageCat = ageCat;}

    /**
     * getter method for age category
     * @return the age category
     */
    public String getAgeCat() {return ageCat;}
}
