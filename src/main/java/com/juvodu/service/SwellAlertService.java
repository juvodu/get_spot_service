package com.juvodu.service;

import com.juvodu.database.model.Spot;

/**
 * Service which evaluates surf conditions based on certain criteria
 *
 * @author Juvodu
 */
public class SwellAlertService {


    public boolean checkSwellAlertForSpot(Spot spot){

        float swellHeight = Float.parseFloat(spot.getSwellHeight());
        float swellPeriod = Float.parseFloat(spot.getSwellPeriod());
        float windspeedKmph = Float.parseFloat(spot.getWindspeedKmph());
        float windDir16Point = Float.parseFloat(spot.getWinddir16Point());

        // TODO: evalute swell condition, if alert required return true
        return true;
    }
}
