package com.juvodu.service;

import com.juvodu.database.model.Continent;
import com.juvodu.database.model.Country;
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
    private static SpotService spotService;
    private final Country france = new Country("FR", "France");
    private final Country us =  new Country("US", "United States");

    @BeforeClass
    public static void beforeClass(){

        spotService = new SpotService(SpotTestModel.class);
    }

    @Before
    public void before(){

        spotService.deleteAll();
    }

    @Test
    public void givenSpotWhenSaveThenSuccess(){

        //execute
        spotService.save(createSpot(Continent.EU, france));
    }

    @Test
    public void givenSavedSpotWhenDeleteThenSuccess(){

        //setup
        Spot spot = createSpot(Continent.EU, france);
        spotService.save(spot);

        //execute
        spotService.delete(spot);
    }

    @Test
    public void givenSavedSpotWhenGetSpotByIdThenReturnSpot(){

        //setup
        Spot spot = createSpot(Continent.EU, france);
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
        Spot spot1 = createSpot(Continent.EU, france);
        Spot spot2 = createSpot(Continent.NA, us);
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
        Spot spot = createSpot(Continent.EU, france);
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
        SpotTestModel spotEU = createSpot(Continent.EU, france);
        SpotTestModel spotNA = createSpot(Continent.NA, us);
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

    @Test
    public void givenSpotWithCountryWhenFindByCountryThenReturnSpot(){

        //setup
        Spot spot = createSpot(Continent.EU, france);
        spotService.save(spot);

        //execute
        List<Spot> spots = spotService.findByCountry(spot.getContinent(), spot.getCountry());

        //verify
        assertNotNull(spots);
        assertEquals(1, spots.size());
        Spot spotResult = spots.get(0);
        assertEquals(Continent.EU, spotResult.getContinent());
        assertEquals(france.getCode(), spotResult.getCountry().getCode());
    }

    /**
     * Helper function to create a spot
     *
     * @param continent
     *            where the spot is located
     * @param country
     *            where the spot is located
     *
     * @return the created instance of the spot
     */
    private SpotTestModel createSpot(Continent continent, Country country){

        SpotTestModel spotTestModel = new SpotTestModel();
        spotTestModel.setName("unit test name");
        spotTestModel.setDescription("unit test description");
        spotTestModel.setContinent(continent);
        spotTestModel.setCountry(country);

        return spotTestModel;
    }
}