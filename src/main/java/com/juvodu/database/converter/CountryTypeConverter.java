package com.juvodu.database.converter;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.juvodu.database.model.Country;

import java.util.Locale;

/**
 * Custom country converter, necessary for non standard types to be used by the DynamoDB converter
 *
 * @author Juvodu
 */
public class CountryTypeConverter implements DynamoDBTypeConverter<String, Country> {

    @Override
    public String convert(Country country) {

        return country.getCode();
    }

    @Override
    public Country unconvert(String countryString) {

        Locale locale = new Locale("", countryString);
        return new Country(locale.getCountry(), locale.getDisplayName());
    }
}

