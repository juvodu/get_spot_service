package com.juvodu.service;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.juvodu.database.model.Continent;
import com.juvodu.database.model.Country;
import com.juvodu.database.model.Spot;
import com.juvodu.util.Constants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service for Spot retrieval and processing.
 *
 * @author Juvodu
 */
public class SpotService<T extends Spot> {

    private final DynamoDBMapper mapper;
    private final Class<T> spotClass;

    public SpotService(Class<T> spotClass){

        this.spotClass = spotClass;
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
                .withRegion(Regions.EU_CENTRAL_1)
                .build();
        this.mapper = new DynamoDBMapper(client);
    }

    /**
     * Retrieve a spot by its hash key
     *
     * @param id
     *          of the spot
     *
     * @return the spot model populated with data
     */
    public T getSpotById(String id){

        return mapper.load(spotClass, id);
    }

    /**
     * Save or update a spot instance
     *
     * @param spot
     *          the spot to save, if an item with the same id exists it will be updated
     *
     * @return the generated id (UUID) of the spot item
     */
    public String save(Spot spot){

        // save does not return anything, but populates the generated id
        mapper.save(spot);
        return spot.getId();
    }

    /**
     * Delete the spot instance
     *
     * @param spot
     *          the spot instance to delete
     */
    public void delete(T spot){

        mapper.delete(spot);
    }

    /**
     * Delete all table entries - for testing purposes only
     */
    public void deleteAll(){

        for(T spot : findAll()){
            delete(spot);
        }
    }

    /**
     * Return all available spots, scan requests are potentially slow
     *
     * @return list of spots saved in the DB
     */
    public List<T> findAll(){

        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
        return mapper.scan(spotClass, scanExpression);
    }

    /**
     * Find all spots for a given continent
     *
     * @param continent
     *          used to filter spots
     *
     * @return list of spots in the continent
     */
    public List<T> findByContinent(Continent continent){

        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":val1", new AttributeValue().withS(continent.getCode()));

        DynamoDBQueryExpression<T> queryExpression = new DynamoDBQueryExpression<T>()
                .withKeyConditionExpression("continent = :val1")
                .withIndexName(Constants.CONTINENT_COUNTRY_INDEX)
                .withConsistentRead(false)
                .withExpressionAttributeValues(eav);

        return mapper.query(spotClass, queryExpression);
    }

    /**
     * Find all spots for a given country
     *
     * @param continent
     *              needs to be specified as it is the partition key of the continent-index
     * @param country
     *              the country to filter for, can be used as it is the range key of the continent-index
     *
     * @return list of spots filtered by the specified country
     */
    public List<T> findByCountry(Continent continent, Country country){

        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":val1", new AttributeValue().withS(continent.getCode()));
        eav.put(":val2", new AttributeValue().withS(country.getCode()));

        DynamoDBQueryExpression<T> queryExpression = new DynamoDBQueryExpression<T>()
                .withKeyConditionExpression("continent = :val1 and country = :val2")
                .withIndexName(Constants.CONTINENT_COUNTRY_INDEX)
                .withConsistentRead(false)
                .withExpressionAttributeValues(eav);

        return mapper.query(spotClass, queryExpression);
    }
}
