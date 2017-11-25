package com.juvodu.service;

import ch.hsr.geohash.GeoHash;
import ch.hsr.geohash.WGS84Point;
import ch.hsr.geohash.queries.GeoHashCircleQuery;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.juvodu.database.DatabaseHelper;
import com.juvodu.database.model.*;
import com.juvodu.util.Constants;
import com.juvodu.util.GeoHelper;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service for Spot retrieval and processing.
 *
 * @author Juvodu
 */
public class SpotService<T extends BaseSpot> {

    private final DynamoDBMapper mapper;
    private final Class<T> spotClass;
    private final DatabaseHelper<T> databaseHelper;

    public SpotService(Class<T> spotClass){

        this.spotClass = spotClass;
        this.databaseHelper = new DatabaseHelper<>();
        AmazonDynamoDB dynamoDB = DatabaseHelper.getDynamoDB();

        // configure dynamo DB mapper here
        DynamoDBMapperConfig mapperConfig = new DynamoDBMapperConfig.Builder()
                .withSaveBehavior(DynamoDBMapperConfig.SaveBehavior.UPDATE_SKIP_NULL_ATTRIBUTES) // null values do not delete values
                .build();

        this.mapper = new DynamoDBMapper(dynamoDB, mapperConfig);
    }

    /**
     * Retrieve a spot by its hash key
     *
     * @param id
     *          of the spot
     *
     * @return the spot testmodel populated with data
     */
    public T getSpotById(String id){

        return mapper.load(spotClass, id);
    }

    /**
     * Retrieve a list of spot by its hash keys
     *
     * @param ids
     *          of the spots
     * @return list of spots models
     */
    public List<T> getSpotsByIds(List<String> ids){

        List<T> spots = new ArrayList<>();
        for(String id : ids){
            spots.add(getSpotById(id));
        }
        return spots;
    }

    /**
     * Save or update a spot instance
     *
     * @param spot
     *          the spot to save, if an item with the same id exists it will be updated
     *
     * @return the generated id (UUID) of the spot
     */
    public String save(Spot spot){

        // create a geohash for each spot for fast queries based on position
        Position position = spot.getPosition();
        if(position != null) {
            String base32GeoHash = DatabaseHelper.createBinaryGeohash(position);
            spot.setGeohash(base32GeoHash);
        }

        // initialize cron date for new spots, spot will be populated with weather data after 24h max
        if(spot.getCronDate() == null) {
            spot.setCronDate(new Date());
        }

        // save does not return, instead it populates the generated id to the passed spot instance
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
     * @param limit
     *              the size of the returned result list
     *
     * @return list of spots in the continent
     */
    public List<T> findByContinent(Continent continent, int limit){

        String filterExpression = "continent = :val1";
        DynamoDBQueryExpression<T> queryExpression = databaseHelper.createIndexQueryExpression(continent.getCode(),
                null, Constants.CONTINENT_COUNTRY_INDEX, filterExpression, limit);
        return mapper.queryPage(spotClass, queryExpression).getResults();
    }

    /**
     * Find all spots for a given country
     *
     * @param continent
     *              needs to be specified as it is the partition key of the continent-index
     * @param country
     *              the country to filter for, can be used as it is the range key of the continent-index
     * @param limit
     *              the size of the returned result list
     *
     *
     * @return list of spots filtered by the specified country
     */
    public List<T> findByCountry(Continent continent, Country country, int limit){

        String filterExpression = "continent = :val1 and country = :val2";
        DynamoDBQueryExpression<T> queryExpression = databaseHelper.createIndexQueryExpression(continent.getCode(),
                country.getCode(), Constants.CONTINENT_COUNTRY_INDEX, filterExpression, limit);

        return mapper.queryPage(spotClass, queryExpression).getResults();
    }

    /**
     * Find all spots in a given radius
     *
     * @param continent
     *          the continent in which the search takes place (partition key of continent-geohash-index table)
     * @param position
     *          which is the center of the radius
     * @param searchRadius
     *          search radius in km
     * @param limit
 *              the size of the returned result list
     *
     * @return list of spots within the specifed radius
     */
    public List<T> findByDistance(Continent continent, Position position, int searchRadius, int limit){

        int searchRadiusMeter = searchRadius * 1000;
        List<T> spots = new LinkedList<>();
        GeoHashCircleQuery geoHashCircleQuery = new GeoHashCircleQuery(new WGS84Point(position.getLatitude(), position.getLongitude()), searchRadiusMeter);
        List<GeoHash> searchHashes = geoHashCircleQuery.getSearchHashes();

        for(GeoHash geoHash : searchHashes){

            //rough and fast filtering by geohash
            String binaryHashString = geoHash.toBinaryString();
            String filterExpression = "continent = :val1 and begins_with(geohash,:val2)";
            DynamoDBQueryExpression<T> queryExpression = databaseHelper.createIndexQueryExpression(continent.getCode(),
                    binaryHashString, Constants.CONTINENT_GEOHASH_INDEX, filterExpression, limit);
            spots.addAll(mapper.queryPage(spotClass, queryExpression).getResults());
        }

        // calculate distance to each spot in km
        spots.forEach(spot -> spot.setDistance(GeoHelper.getDistance(position, spot.getPosition())/1000));

        // fine filtering and sorting by distance
        spots = spots.stream()
                .filter(spot -> searchRadius >= spot.getDistance())
                .sorted(Comparator.comparing(T::getDistance))
                .collect(Collectors.toList());

        return spots;
    }

    /**
     * Find all spots to be updated by the cron job in the specified partition
     *
     * @param continent
     *              the continent in which the search takes place (partition key of continent-crondate-index table)
     * @return list of spots to be updated
     */
    public List<T> findByToBeUpdated(Continent continent){

        long oneDayAgoMilli = (new Date()).getTime() - (24L * 60L * 60L * 1000L);
        String filterExpression = "continent = :val1 and cronDate < :val2";
        DynamoDBQueryExpression<T> queryExpression = databaseHelper.createIndexQueryExpression(continent.getCode(),
                Long.toString(oneDayAgoMilli), Constants.CONTINENT_CRONDATE_INDEX, filterExpression, 1000);

        return mapper.queryPage(spotClass, queryExpression).getResults();
    }
}
