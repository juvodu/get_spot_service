package com.juvodu.database.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

/**
 * Model for storing favorite spots of a user
 *
 * @author Juvodu
 */
@DynamoDBTable(tableName = "favorite")
public class Favorite {

    /** the id of the user which the favorite belongs to. */
    @DynamoDBHashKey
    private String userId;

    /** the id of the spot the user likes. */
    @DynamoDBRangeKey
    private String spotId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String usedId) {
        this.userId = usedId;
    }

    public String getSpotId() {
        return spotId;
    }

    public void setSpotId(String spotId) {
        this.spotId = spotId;
    }
}
