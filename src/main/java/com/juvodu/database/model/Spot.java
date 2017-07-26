package com.juvodu.database.model;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.juvodu.database.converter.ContinentTypeConverter;
import com.juvodu.database.converter.CountryTypeConverter;
import com.juvodu.database.converter.PositionTypeConverter;

/**
 * Model representing the Spot table
 */
@DynamoDBTable(tableName = "spot")
public class Spot {

    @DynamoDBHashKey
    @DynamoDBAutoGeneratedKey
    private String id;

    @DynamoDBAttribute
    private String name;

    @DynamoDBAttribute
    private String description;

    @DynamoDBAttribute
    @DynamoDBTypeConverted(converter = ContinentTypeConverter.class)
    private Continent continent;

    @DynamoDBAttribute
    @DynamoDBTypeConverted(converter = CountryTypeConverter.class)
    private Country country;

    @DynamoDBTypeConverted(converter = PositionTypeConverter.class)
    @DynamoDBAttribute
    private Position position;

    @JsonIgnore // no need to return the geohash as JSON, its for internal use only
    @DynamoDBAttribute
    private String geohash;

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
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public Continent getContinent() {
        return continent;
    }

    public void setContinent(Continent continent) {
        this.continent = continent;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public String getGeohash() {
        return geohash;
    }

    public void setGeohash(String geohash) {
        this.geohash = geohash;
    }
}
