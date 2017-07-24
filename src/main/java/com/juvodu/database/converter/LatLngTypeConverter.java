package com.juvodu.database.converter;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.javadocmd.simplelatlng.LatLng;
import org.apache.commons.lang3.StringUtils;

/**
 * Custom Latitude and Longitude position converter, necessary for non standard types to be used by the DynamoDB converter
 *
 * @author Juvodu
 */
public class LatLngTypeConverter implements DynamoDBTypeConverter<String, LatLng> {

    @Override
    public String convert(LatLng position) {

        return String.format("%s ; %s", position.getLatitude(), position.getLongitude());
    }

    @Override
    public LatLng unconvert(String s) {

        double latitude = 0;
        double longitude = 0;

        if (StringUtils.isNotBlank(s) && s.contains(";")) {
            String[] data = s.split(";");
            latitude = Double.valueOf(data[0].trim());
            longitude = Double.valueOf(data[1].trim());
        }

        return new LatLng(latitude, longitude);
    }
}

