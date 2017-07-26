package com.juvodu.database.converter;

import ch.hsr.geohash.WGS84Point;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import org.apache.commons.lang3.StringUtils;

/**
 * Custom Latitude and Longitude position converter, necessary for non standard types to be used by the DynamoDB converter
 *
 * @author Juvodu
 */
public class PositionTypeConverter implements DynamoDBTypeConverter<String, WGS84Point> {

    @Override
    public String convert(WGS84Point position) {

        return String.format("%s ; %s", position.getLatitude(), position.getLongitude());
    }

    @Override
    public WGS84Point unconvert(String positionStr) {

        double latitude = 0;
        double longitude = 0;
        try {
            if (StringUtils.isNotBlank(positionStr) && positionStr.contains(";")) {
                String[] data = positionStr.split(";");
                latitude = Double.valueOf(data[0].trim());
                longitude = Double.valueOf(data[1].trim());
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return new WGS84Point(latitude, longitude);
    }
}

