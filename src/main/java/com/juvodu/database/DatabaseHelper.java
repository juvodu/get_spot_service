package com.juvodu.database;

import ch.hsr.geohash.GeoHash;
import ch.hsr.geohash.WGS84Point;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import java.util.HashMap;
import java.util.Map;

/**
 * Class used by service layer, helps to access database layer by creating geohash index, queries etc.
 *
 * @author Juvodu
 */
public class DatabaseHelper<T> {

    /**
     * Creates a binary geohash for the position
     *
     * @param position
     *          for which the geohash will be created
     *
     * @return binary geohash as a string
     */
    public String createBinaryGeohash(WGS84Point position){

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
     *
     * @return prepared query to run on dynamo db table
     */
    public DynamoDBQueryExpression<T> createQueryExpression(String partitionKey, String sortKey, String index, String conditionExpression){

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
                .withExpressionAttributeValues(eav);

        return queryExpression;
    }
}
