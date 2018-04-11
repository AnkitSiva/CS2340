package xyz.ankitsiva.teamcaesium.model;

import java.util.ArrayList;

/**
 * Created by team caesium on 2/13/2018.
 * An array list class to hold all of the valid user/password combinations
 */

public class UserList {
    private final ArrayList<String> userList = new ArrayList<>();

    public UserList(String firstUser) {
        super();
        userList.add(firstUser);
    }

    public UserList() {

    }

    /**
     * Adds a new user to the array list
     * @param user
     */
    public void addUser(String user) {
        userList.add(user);
    }

    /**
     * Array representation of the arrayList
     * @return an array representation
     */
    public String[] arrayRep() {
        String[] array = new String[userList.size()];
        for (int i = 0; i < userList.size(); i++) {
            array[i] = userList.get(i);
        }
        return array;
    }
}
