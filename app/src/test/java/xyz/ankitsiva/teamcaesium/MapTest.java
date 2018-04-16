package xyz.ankitsiva.teamcaesium;

import com.google.android.gms.maps.model.Marker;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import xyz.ankitsiva.teamcaesium.controllers.MapViewActivity;
import xyz.ankitsiva.teamcaesium.model.Shelter;
import xyz.ankitsiva.teamcaesium.model.User;

import static org.junit.Assert.*;

public class MapTest {
    MapViewActivity mva;
	private User user;
	private Shelter shelter;
	private Shelter nullShelter;
	private Map<Shelter, Marker> nullShelterMarkerMap;
    private Map<Shelter, Marker> emptyShelterMarkerMap;

	@Before
	public void setUp() {
		mva = new MapViewActivity();
	}

	@Test
	public void testEmptyMarkerMap() {
        emptyShelterMarkerMap = new HashMap<>();
        mva.setShelterMarkers(emptyShelterMarkerMap);
        assertArrayEquals(emptyShelterMarkerMap.keySet().toArray(),
                mva.getShelterMarkers().keySet().toArray());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullGenderChoice() {
	    String nullChoice = null;
	    mva.setChosenGender(nullChoice);
	    mva.mutateMarkers();

    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullAgeChoice() {
        String nullChoice = null;
        mva.setChosenAge(nullChoice);
        mva.mutateMarkers();

    }

	@Test(expected = IllegalArgumentException.class)
    public void testNullMarkers() {
	    nullShelterMarkerMap = null;
	    mva.setShelterMarkers(nullShelterMarkerMap);
        mva.mutateMarkers();
	}
}
