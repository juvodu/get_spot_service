package com.juvodu.database;

import ch.hsr.geohash.GeoHash;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.juvodu.database.model.Position;

import java.util.HashMap;
import java.util.Map;

/**
 * Class used by service layer, helps to access database layer by creating geohash index, queries etc.
 *
 * @author Juvodu
 */
public class DatabaseHelper<T> {

    /**
     * Get the dynamo db client, situated in eu_central_1 (frankfurt) region
     * @return
     */
    public static AmazonDynamoDB getDynamoDB(){

        return AmazonDynamoDBClientBuilder.standard()
                .withRegion(Regions.EU_CENTRAL_1)
                .build();
    }

    /**
     * Creates a binary geohash for the position
     *
     * @param position
     *          for which the geohash will be created
     *
     * @return binary geohash as a string
     */
    public static String createBinaryGeohash(Position position){

        GeoHash geoHash = GeoHash.withBitPrecision(position.getLatitude(), position.getLongitude(), 64);
        return geoHash.toBinaryString();
    }

    /**
     * Create a query expression on an index table
     *
     * @param partitionKey
     *          the partition key to filter for
     * @param sortKey
     *          optional search key to filter for
     * @param index
     *          name of index table on which query will run
     * @param conditionExpression
     *          contains the actual operators used for filtering
     * @param limit
     *          max amount of results
     *
     * @return prepared query to run on dynamo db table
     */
    public DynamoDBQueryExpression<T> createIndexQueryExpression(String partitionKey, String sortKey, String index, String conditionExpression, int limit){

        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":val1", new AttributeValue().withS(partitionKey));

        //optional sort key
        if(sortKey != null) {
            eav.put(":val2", new AttributeValue().withS(sortKey));
        }

        DynamoDBQueryExpression<T> queryExpression = new DynamoDBQueryExpression<T>()
                .withKeyConditionExpression(conditionExpression)
                .withIndexName(index)
                .withConsistentRead(false)
                .withExpressionAttributeValues(eav)
                .withLimit(limit);

        return queryExpression;
    }
}
