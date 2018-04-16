package xyz.ankitsiva.teamcaesium;

import org.junit.Before;
import org.junit.Test;

import xyz.ankitsiva.teamcaesium.model.User;
import xyz.ankitsiva.teamcaesium.model.Vacancy;

import static org.junit.Assert.*;

/**
 * Created by chrissaad on 4/15/18.
 */

public class VacancyTest {

    private User user;

    private static final int BEDS = 50;
    private Vacancy vacancy;
    private static final int max = 200;

    @Before
    public void setUp() {
        user = new User("vacancytest", "password");
        vacancy = new Vacancy(200, 70);
    }

    /*
     * Aryaman Vinchhi test for Vacancy's claimBed() method
     */
    @Test
    public void claimBed() throws Exception {
        int numBeds = vacancy.getBeds(); //70
        boolean claim = vacancy.claimBed(30);
        assertEquals(vacancy.getBeds(), 40);
        assertEquals(claim, true);

        boolean claim2 = vacancy.claimBed(vacancy.getBeds() + 20);
        assertEquals(claim2, false);
        assertEquals(vacancy.getBeds(), 40);

        boolean claim3 = vacancy.claimBed(-5);
        assertEquals(claim3, false);
        assertEquals(vacancy.getBeds(), 40);
    }

    /*
     * Chris Saad tests for Vacancy's releaseBed() method
     */
    @Test
    public void releaseBed() throws Exception {
        int numBeds = vacancy.getBeds();
        vacancy.releaseBed(20);
        assertEquals(vacancy.getBeds(), 90);

        vacancy.releaseBed(200);
        assertEquals(vacancy.getBeds(), 90);

        vacancy.releaseBed(-20);
        assertEquals(vacancy.getBeds(), 90);
    }
}
