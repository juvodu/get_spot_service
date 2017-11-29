package com.juvodu.database.converter;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.juvodu.database.model.Continent;

/**
 * Custom continent converter, necessary for non standard types to be used by the DynamoDB converter
 *
 * @author Juvodu
 */
public class ContinentTypeConverter implements DynamoDBTypeConverter<String, Continent>{

    @Override
    public String convert(Continent continent) {

        return continent.getCode();
    }

    @Override
    public Continent unconvert(String continentStr) {

        Continent continent = null;
        switch (continentStr){

            case "AF":
                continent = Continent.AF;
                break;
            case "NA":
                continent = Continent.NA;
                break;
            case "OC":
                continent = Continent.OC;
                break;
            case "AS":
                continent = Continent.AS;
                break;
            case "EU":
                continent = Continent.EU;
                break;
            case "SA":
                continent = Continent.SA;
                break;
        }
        return continent;
    }
}
