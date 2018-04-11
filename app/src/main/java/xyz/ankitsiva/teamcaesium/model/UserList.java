package xyz.ankitsiva.teamcaesium.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by team caesium on 2/13/2018.
 * An arraylist class to hold all of the valid user/password combinations
 */

public class UserList {
    public List<String> userList = new ArrayList<String>();

    public UserList(String firstUser) {
        super();
        userList.add(firstUser);
    }

    public UserList() {

    }

    /**
     * Adds a new user to the arraylist
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
