package com.juvodu.database.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGeneratedKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

/**
 * Model representing the user table
 *
 * @author Juvodu
 */
@DynamoDBTable(tableName = "user")
public class User {

    @DynamoDBHashKey
    @DynamoDBAutoGeneratedKey
    private String id;

    @DynamoDBAttribute
    private String userName;

    @DynamoDBAttribute
    private String email;

    @DynamoDBAttribute
    private String platformEndpointArn;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPlatformEndpointArn() {
        return platformEndpointArn;
    }

    public void setPlatformEndpointArn(String platformEndpointArn) {
        this.platformEndpointArn = platformEndpointArn;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
