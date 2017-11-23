package com.juvodu.database.model;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.juvodu.database.converter.CountryTypeConverter;
import com.juvodu.database.converter.PositionTypeConverter;

/**
 * Model representing the partial Spot table for list views
 * on the client which do not require detailed data
 *
 * @author Juvodu
 */
@DynamoDBTable(tableName = "spot")
public class BaseSpot {

    @DynamoDBHashKey
    @DynamoDBAutoGeneratedKey
    private String id;

    @DynamoDBAttribute
    private String name;

    @DynamoDBAttribute
    private String shortDescription;

    @DynamoDBAttribute
    private String thumbnail;

    @DynamoDBAttribute
    private String swellHeight;

    @DynamoDBAttribute
    private String swellPeriod;

    @DynamoDBAttribute
    private String windspeedKmph;

    @DynamoDBAttribute
    private String winddir16Point;

    @DynamoDBTypeConverted(converter = PositionTypeConverter.class)
    @DynamoDBAttribute
    private Position position;

    @DynamoDBAttribute
    @DynamoDBTypeConverted(converter = CountryTypeConverter.class)
    private Country country;

    // will be calculated on runtime
    @DynamoDBIgnore
    private int distance;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }


    public String getSwellHeight() {
        return swellHeight;
    }

    public void setSwellHeight(String swellHeight) {
        this.swellHeight = swellHeight;
    }

    public String getSwellPeriod() {
        return swellPeriod;
    }

    public void setSwellPeriod(String swellPeriod) {
        this.swellPeriod = swellPeriod;
    }

    public String getWindspeedKmph() {
        return windspeedKmph;
    }

    public void setWindspeedKmph(String windspeedKmph) {
        this.windspeedKmph = windspeedKmph;
    }

    public String getWinddir16Point() {
        return winddir16Point;
    }

    public void setWinddir16Point(String winddir16Point) {
        this.winddir16Point = winddir16Point;
    }
}
