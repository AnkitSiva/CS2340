package xyz.ankitsiva.teamcaesium;

import com.google.android.gms.maps.model.Marker;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import xyz.ankitsiva.teamcaesium.controllers.MapViewActivity;
import xyz.ankitsiva.teamcaesium.model.Shelter;

import static org.junit.Assert.*;

/**
 * Created by Ankit
 * Tests MapViewActivity's mutateMarkers()
 */
public class MapTest {
    MapViewActivity mva;
    private Map<Shelter, Marker> nullShelterMarkerMap;
    private Map<Shelter, Marker> emptyShelterMarkerMap;

    /**
     * Sets up the test
     */
	@Before
	public void setUp() {
		mva = new MapViewActivity();
	}

    /**
     * Tests with an empty marker map
     */
	@Test
	public void testEmptyMarkerMap() {
        emptyShelterMarkerMap = new HashMap<>();
        mva.setShelterMarkers(emptyShelterMarkerMap);
        assertArrayEquals(emptyShelterMarkerMap.keySet().toArray(),
                mva.getShelterMarkers().keySet().toArray());
    }

    /**
     * Tests with null gender choice
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullGenderChoice() {
	    String nullChoice = null;
	    mva.setChosenGender(nullChoice);
	    mva.mutateMarkers();

    }

    /**
     * Tests with null age choice
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullAgeChoice() {
        String nullChoice = null;
        mva.setChosenAge(nullChoice);
        mva.mutateMarkers();

    }

    /**
     * Tests with null marker map
     */
	@Test(expected = IllegalArgumentException.class)
    public void testNullMarkers() {
	    nullShelterMarkerMap = null;
	    mva.setShelterMarkers(nullShelterMarkerMap);
        mva.mutateMarkers();
	}
}
