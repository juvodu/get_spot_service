package com.juvodu.service;

import com.juvodu.database.model.Continent;
import com.juvodu.database.model.Country;
import com.juvodu.database.model.Position;
import com.juvodu.database.model.Spot;
import com.juvodu.service.testmodel.SpotTestModel;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test suite for the SpotService
 *
 * @author Juvodu
 */
public class SpotServiceTest {

    // testmodel which takes ensures persisting all data to the test table "spot_test"
    private static SpotService<SpotTestModel> spotService;
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
    public void givenSpotWithNullPositionWhenSaveThenSuccess(){

        Spot spot = createSpot(Continent.NA, us, hermosa);
        spot.setPosition(null);

        //execute
        spotService.save(spot);
    }

    @Test
    public void givenSpotWhenSaveThenSuccess(){

        //execute
        spotService.save(createSpot(Continent.NA, us, hermosa));
    }

    @Test
    public void givenSavedSpotWhenDeleteThenSuccess(){

        //setup
        SpotTestModel spot = createSpot(Continent.EU, france, hossegor);
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
        Spot spotResult = spotService.getByHashKey(id);

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
        List<SpotTestModel> allSpots = spotService.findAll();

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
        Spot spot_result = spotService.getByHashKey(spot.getId());
        assertEquals("Updated spot", spot_result.getName());
        assertEquals("Updated spot description", spot_result.getDescription());
    }

    @Test
    public void givenNonExistingSpotWhenGetSpotsByIdsThenReturnEmptyList(){

        //setup
        List<String> ids = new ArrayList<>();
        ids.add("non-existent-id");

        // execute
        List<SpotTestModel> spots = spotService.getByIds(ids);

        // verify
        assertEquals(0, spots.size());
    }

    @Test
    public void given2ExistingSpotWhenGetSpotsByIdsThenSuccess(){

        //setup
        Spot spotEU = createSpot(Continent.EU, france, hossegor);
        Spot spotNA = createSpot(Continent.NA, us, hermosa);
        List<String> ids = new ArrayList<>();
        spotService.save(spotEU);
        spotService.save(spotNA);
        ids.add(spotEU.getId());
        ids.add(spotNA.getId());

        // execute
        List<SpotTestModel> spots = spotService.getByIds(ids);

        //verify
        assertEquals(2, spots.size());
        for(Spot spot : spots){
            assertNotNull(spot);
        }
    }

    @Test
    public void givenSpotWithContinentWhenFindByContinentThenReturnSpot(){

        //setup
        Spot spotEU = createSpot(Continent.EU, france, hossegor);
        Spot spotNA = createSpot(Continent.NA, us, hermosa);
        String id = spotService.save(spotEU);
        spotService.save(spotNA);

        //execute
        List<SpotTestModel> spots = spotService.findByContinent(Continent.EU, 1);

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
        List<SpotTestModel> spots = spotService.findByCountry(spot.getContinent(), spot.getCountry(), 1);

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
        List<SpotTestModel> spots = spotService.findByDistance(Continent.EU, spot1.getPosition(), 1, 100);

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

        //execute - distance between both spots is 210km
        List<SpotTestModel> spots = spotService.findByDistance(Continent.EU, spot1.getPosition(), 210, 100);

        //verify
        assertNotNull(spots);
        assertEquals(2, spots.size());
    }

    @Test
    public void givenUpdatedSpotWhenFindByCronDateThenReturnEmptyList(){

        //setup
        Calendar calYesterdayPlus1Min = Calendar.getInstance();
        calYesterdayPlus1Min.add(Calendar.HOUR, -23);
        calYesterdayPlus1Min.add(Calendar.MINUTE, -59);
        Spot spot = createSpot(Continent.EU, france, hossegor);
        spot.setCronDate(calYesterdayPlus1Min.getTime());
        spotService.save(spot);

        //execute
        List<SpotTestModel> spots = spotService.findByToBeUpdatedAndContinent(Continent.EU);

        //verify
        assertNotNull(spots);
        assertEquals(0, spots.size());
    }

    @Test
    public void givenOutdatedSpotWhenFindByCronDateThenReturnListWithSpot(){

        //setup
        Calendar cal2DaysAgo = Calendar.getInstance();
        cal2DaysAgo.add(Calendar.HOUR, -25);
        Spot spot = createSpot(Continent.EU, france, hossegor);
        spot.setCronDate(cal2DaysAgo.getTime());
        spotService.save(spot);

        //execute
        List<SpotTestModel> spots = spotService.findByToBeUpdatedAndContinent(Continent.EU);

        //verify
        assertNotNull(spots);
        assertEquals(1, spots.size());
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
    private SpotTestModel createSpot(Continent continent, Country country, Position position){

        SpotTestModel spotTestModel = new SpotTestModel();
        spotTestModel.setName("unit test name");
        spotTestModel.setDescription("unit test description");
        spotTestModel.setShortDescription("unit short description");
        spotTestModel.setContinent(continent);
        spotTestModel.setCountry(country);
        spotTestModel.setPosition(position);
        spotTestModel.setImage("TestImage");
        spotTestModel.setThumbnail("TestThumbnail");
        spotTestModel.setCronDate(new Date());

        return spotTestModel;
    }
}