package xyz.ankitsiva.teamcaesium;

import org.junit.Before;
import org.junit.Test;

import xyz.ankitsiva.teamcaesium.model.Shelter;
import xyz.ankitsiva.teamcaesium.model.User;

import static org.junit.Assert.*;

/**
 * Created by kevin on 4/13/2018.
 */
public class UserTest {

	private User user;
	private Shelter shelter;
	private Shelter nullShelter;
	private static final int BEDS = 50;

	@Before
	public void setUp() {
		user = new User("Kevin", "Password");
		shelter = new Shelter();
		nullShelter = null;
	}

	/*
	Kevin Zhu's test for User's addReservation method
	 */
	@Test
	public void addReservation() throws Exception {

		user.addReservation(shelter, BEDS);
		assertNotNull(user.getReservation());
		assertEquals(user.getClaimed(), BEDS);
		assertEquals(user.getShelterKey(), Integer.toString(shelter.getKey()));

		user = new User("Kevin", "Password");
		user.addReservation(nullShelter, BEDS);
		assertNull(user.getReservation());
		assertEquals(user.getShelterKey(), "-1");
		assertEquals(user.getClaimed(), 0);
	}

	/**
	 * Mason Baughan test of User's releaseBeds() method
	 */
	@Test
	public void releaseBeds() throws Exception {

		user.addReservation(shelter, BEDS);
		user.releaseBeds();
		assertNull(user.getReservation());

	}

}