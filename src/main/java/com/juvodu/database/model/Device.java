package com.juvodu.database.model;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.juvodu.database.converter.DateConverter;

import java.util.Date;

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

    @DynamoDBAttribute
    private String operatingSystem;

    @DynamoDBAttribute
    @DynamoDBTypeConverted(converter = DateConverter.class)
    private Date createdDate;

    @DynamoDBAttribute
    @DynamoDBTypeConverted(converter = DateConverter.class)
    private Date updatedDate;

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

    public String getOperatingSystem() {
        return operatingSystem;
    }

    public void setOperatingSystem(String operatingSystem) {
        this.operatingSystem = operatingSystem;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }
}
