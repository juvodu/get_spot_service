package com.juvodu.util;

import com.juvodu.database.model.Position;

/**
 * Created by Juvodu on 01.09.17.
 */
public class GeoHelper {

    /**
     * Calculate distance between two positions
     *
     * @param pos1
     * @param pos2
     * @return distance in meter
     */
    public static int getDistance(Position pos1, Position pos2) {

        double earthRadius = 6371000; //meters
        double dLat = Math.toRadians(pos2.getLatitude()-pos1.getLatitude());
        double dLng = Math.toRadians(pos2.getLongitude()-pos1.getLongitude());
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(pos1.getLatitude())) * Math.cos(Math.toRadians(pos2.getLatitude())) *
                        Math.sin(dLng/2) * Math.sin(dLng/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return (int) (earthRadius * c);
    }

}
