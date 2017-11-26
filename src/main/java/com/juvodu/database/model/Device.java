package com.juvodu.database.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

/**
 * Model representing the a user device, one user can have multiple devices
 *
 * @author Juvodu
 */
@DynamoDBTable(tableName = "device")
public class Device {

    @DynamoDBHashKey
    private String userId;

    @DynamoDBRangeKey
    private String deviceToken;

    @DynamoDBAttribute
    private String platformEndpointArn;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getPlatformEndpointArn() {
        return platformEndpointArn;
    }

    public void setPlatformEndpointArn(String platformEndpointArn) {
        this.platformEndpointArn = platformEndpointArn;
    }
}
