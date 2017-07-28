package com.juvodu.service;

import com.juvodu.database.model.Continent;
import com.juvodu.database.model.Country;
import com.juvodu.database.model.Position;
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
    private final Country spain = new Country("ES", "Spain");
    private final Position hossegor = new Position(43.671223, -1.441445);
    private final Position hermosa = new Position(33.863329, -118.403169);
    private final Position liencres = new Position(43.452663, -3.963651);

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
        spotService.save(createSpot(Continent.NA, us, hermosa));
    }

    @Test
    public void givenSavedSpotWhenDeleteThenSuccess(){

        //setup
        Spot spot = createSpot(Continent.EU, france, hossegor);
        spotService.save(spot);

        //execute
        spotService.delete(spot);
    }

    @Test
    public void givenSavedSpotWhenGetSpotByIdThenReturnSpot(){

        //setup
        Spot spot = createSpot(Continent.EU, france, hossegor);
        String id = spotService.save(spot);

        //execute
        Spot spotResult = spotService.getSpotById(id);

        //verify
        assertNotNull(spotResult);
        assertEquals(id, spotResult.getId());
        assertEquals(hossegor, spotResult.getPosition());
    }

    @Test
    public void given2SavedSpotsWhenGetAllSpotsThenReturnBoth(){

        //setup
        Spot spot1 = createSpot(Continent.EU, france, hossegor);
        Spot spot2 = createSpot(Continent.NA, us, hermosa);
        spotService.save(spot1);
        spotService.save(spot2);

        //execute
        List<Spot> allSpots = spotService.findAll();

        //verify
        assertNotNull(allSpots);
        assertEquals(2, allSpots.size());
    }

    @Test
    public void givenExistingSpotWhenUpdateThenSuccess(){

        //setup
        Spot spot = createSpot(Continent.EU, france, hossegor);
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
        Spot spotEU = createSpot(Continent.EU, france, hossegor);
        Spot spotNA = createSpot(Continent.NA, us, hermosa);
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
        Spot spot = createSpot(Continent.EU, france, hossegor);
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

    @Test
    public void givenSearchRadiusAndOneSpotInRadiusWhenFindInRadiusThenReturnOneSpot(){

        //setup
        Spot spot1 = createSpot(Continent.EU, france, hossegor);
        Spot spot2 = createSpot(Continent.NA, us, hermosa);
        Spot spot3 = createSpot(Continent.EU, spain, liencres);
        spotService.save(spot1);
        spotService.save(spot2);
        spotService.save(spot3);

        //execute
        List<Spot> spots = spotService.findByDistance(Continent.EU, spot1.getPosition(), 1);

        //verify
        assertNotNull(spots);
        assertEquals(1, spots.size());
        Spot spotResult = spots.get(0);
        assertEquals(Continent.EU, spotResult.getContinent());
        assertEquals(france.getCode(), spotResult.getCountry().getCode());
    }

    @Test
    public void givenSearchRadiusTwoSpotsInRadiusWhenFindInRadiusThenReturnTwoSpots(){

        //setup
        Spot spot1 = createSpot(Continent.EU, france, hossegor);
        Spot spot2 = createSpot(Continent.NA, us, hermosa);
        Spot spot3 = createSpot(Continent.EU, spain, liencres);
        spotService.save(spot1);
        spotService.save(spot2);
        spotService.save(spot3);

        //execute
        List<Spot> spots = spotService.findByDistance(Continent.EU, spot1.getPosition(), 120000);

        //verify
        assertNotNull(spots);
        assertEquals(2, spots.size());
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
    private Spot createSpot(Continent continent, Country country, Position position){

        Spot spotTestModel = new SpotTestModel();
        spotTestModel.setName("unit test name");
        spotTestModel.setDescription("unit test description");
        spotTestModel.setContinent(continent);
        spotTestModel.setCountry(country);
        spotTestModel.setPosition(position);

        return spotTestModel;
    }
}