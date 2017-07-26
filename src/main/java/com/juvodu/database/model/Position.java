package com.juvodu.database.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Model wrapping latitude and longitude data of a position
 *
 * @author Juvodu
 */
public class Position {

    private double latitude;
    private double longitude;

    @JsonCreator
    public Position(@JsonProperty("latitude") double latitude, @JsonProperty("longitude") double longitude){
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Position) {
            Position other = (Position) obj;
            return latitude == other.latitude && longitude == other.longitude;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int result = 42;
        long latBits = Double.doubleToLongBits(latitude);
        long lonBits = Double.doubleToLongBits(longitude);
        result = 31 * result + (int) (latBits ^ (latBits >>> 32));
        result = 31 * result + (int) (lonBits ^ (lonBits >>> 32));
        return result;
    }
}
