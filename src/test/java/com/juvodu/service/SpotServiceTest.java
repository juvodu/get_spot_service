package com.juvodu.service;

import com.juvodu.database.model.Continent;
import com.juvodu.database.model.Spot;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test suite for the SpotService
 *
 * @author Juvodu
 */
public class SpotServiceTest {

    // model which takes ensures persisting all data to the test table "spot_test"
    private static SpotTestModel spot;
    private static SpotService spotService;


    @BeforeClass
    public static void beforeClass(){

        spotService = new SpotService(SpotTestModel.class);
        spot = new SpotTestModel();
        spot.setName("test name");
        spot.setDescription("unittest");
        spot.setContinent(Continent.EU);
    }

    @Before
    public void before(){

        spotService.deleteAll();
    }

    @Test
    public void givenSpotWhenSaveThenSuccess(){

        //execute
        spotService.save(spot);
    }

    @Test
    public void givenSavedSpotWhenDeleteThenSuccess(){

        //setup
        spotService.save(spot);

        //execute
        spotService.delete(spot);
    }

    @Test
    public void givenSavedSpotWhenGetSpotByIdThenReturnSpot(){

        //setup
        String id = spotService.save(spot);

        //execute
        Spot spotResult = spotService.getSpotById(id);

        //verify
        assertNotNull(spotResult);
        assertEquals(id, spotResult.getId());
    }

    @Test
    public void given2SavedSpotsWhenGetAllSpotsThenReturnBoth(){

        //setup
        SpotTestModel spot1 = new SpotTestModel();
        SpotTestModel spot2 = new SpotTestModel();
        spot1.setName("Spot 1");
        spot2.setName("Spot 2");
        spotService.save(spot1);
        spotService.save(spot2);

        //execute
        List<Spot> allSpots = spotService.findAll();

        //verify
        assertNotNull(allSpots);
        assertEquals(2, allSpots.size());
        System.out.println(allSpots.size());
    }

    @Test
    public void givenExistingSpotWhenUpdateThenSuccess(){

        //setup
        spotService.save(spot);
        spot.setName("Updated spot");
        spot.setDescription("Updated spot description");

        //execute
        spotService.save(spot);

        //verify
        Spot spot_result = spotService.getSpotById(spot.getId());
        assertEquals("Updated spot", spot_result.getName());
        assertEquals("Updated spot description", spot_result.getDescription());
    }

    @Test
    public void givenSpotWithContinentWhenFindByContinentThenReturnSpot(){

        //setup
        SpotTestModel spotEU = new SpotTestModel();
        SpotTestModel spotNA = new SpotTestModel();
        spotEU.setContinent(Continent.EU);
        spotNA.setContinent(Continent.NA);
        String id = spotService.save(spotEU);
        spotService.save(spotNA);

        //execute
        List<Spot> spots = spotService.findByContinent(Continent.EU);

        //verify
        assertNotNull(spots);
        assertEquals(1, spots.size());
        Spot resultSpot = spots.get(0);
        assertEquals(Continent.EU, resultSpot.getContinent());
        assertEquals(id, resultSpot.getId());
    }
}