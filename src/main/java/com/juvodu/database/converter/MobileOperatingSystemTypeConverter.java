package com.juvodu.database.converter;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.juvodu.database.model.MobileOperatingSystem;

/**
 * Custom mobile os converter, necessary for non standard types to be used by the DynamoDB converter
 *
 * @author Juvodu
 */
public class MobileOperatingSystemTypeConverter implements DynamoDBTypeConverter<String, MobileOperatingSystem> {

    @Override
    public String convert(MobileOperatingSystem mobileOperatingSystem) {
        return mobileOperatingSystem.toString();
    }

    @Override
    public MobileOperatingSystem unconvert(String mobileOperatingSystemString) {
        return MobileOperatingSystem.valueOf(mobileOperatingSystemString);
    }
}
