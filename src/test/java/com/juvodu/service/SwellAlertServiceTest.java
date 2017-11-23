package com.juvodu.service;

import com.juvodu.database.model.Spot;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test suite for the SwellAlertService
 *
 * @author Juvodu
 */
public class SwellAlertServiceTest {

    private static SwellAlertService swellAlertService;

    @BeforeClass
    public static void beforeClass() {

        swellAlertService = new SwellAlertService();
    }

    @Test
    public void givenSpotWithAlertConditionsWhenCheckSwellAlertForSpotReturnTrue() {

        // setup
        Spot spot = new Spot();
        spot.setSwellHeight("2");
        spot.setSwellPeriod("14");
        spot.setWinddir16Point("S");
        spot.setWindspeedKmph("0");

        // execute
        boolean isAlert = swellAlertService.checkSwellAlertForSpot(spot);

        // verify
        assertTrue(isAlert);
    }

    @Test
    public void givenSpotWithNonAlertConditionsWhenCheckSwellAlertForSpotReturnFalse() {

        // setup
        Spot spot = new Spot();
        spot.setSwellHeight("2");
        spot.setSwellPeriod("14");
        spot.setWinddir16Point("S");
        spot.setWindspeedKmph("30");

        // execute
        boolean isAlert = swellAlertService.checkSwellAlertForSpot(spot);

        // verify
        assertFalse(isAlert);
    }
}
