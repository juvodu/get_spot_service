package com.juvodu.service;

import com.juvodu.database.model.Spot;

/**
 * Service which evaluates surf conditions based on certain criteria
 *
 * @author Juvodu
 */
public class SwellAlertService {


    /**
     * Checks spot for certain conditions and returns true if met
     *
     * @param spot to be checked for swell conditions
     * @return true if surf conditions met
     */
    public boolean checkSwellAlertForSpot(Spot spot){

        float swellHeight = Float.parseFloat(spot.getSwellHeight());
        float swellPeriod = Float.parseFloat(spot.getSwellPeriod());
        float windspeedKmph = Float.parseFloat(spot.getWindspeedKmph());
        String windDir16Point = spot.getWinddir16Point();

        // TODO: swell rating by user preferences and spot properties incl. wind dir
        boolean isAlert = false;
        if (swellHeight > 1 && swellPeriod > 10 && windspeedKmph < 20) {
            isAlert = true;
        }

        return isAlert;
    }
}
