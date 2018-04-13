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

	@Before
	public void setUp() {
		user = new User("Kevin", "Password");
		shelter = new Shelter();
	}

	/*
	Kevin Zhu's test for User's addReservation method
	 */
	@Test
	public void addReservation() throws Exception {
		int beds = 50;
		user.addReservation(shelter, beds);
		assertNotNull(user.getReservation());
		assertEquals(user.getClaimed(), beds);
		assertEquals(user.getShelterKey(), Integer.toString(shelter.getKey()));

		user = new User("Kevin", "Password");
		user.addReservation(nullShelter, beds);
		assertNull(user.getReservation());
		assertEquals(user.getShelterKey(), "-1");
		assertEquals(user.getClaimed(), 0);
	}

}