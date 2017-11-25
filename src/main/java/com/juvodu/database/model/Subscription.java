package com.juvodu.database.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGeneratedKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

/**
 * Subscription stored when a user subscribes to notifications of a topic
 *
 * @author Juvodu
 */
@DynamoDBTable(tableName = "subscription")
public class Subscription {

    @DynamoDBHashKey
    @DynamoDBAutoGeneratedKey
    private String id;

    /** the user which subscribed. */
    @DynamoDBAttribute
    private String userId;

    /** the sns topic to which the user subscribed. */
    @DynamoDBAttribute
    private String topicArn;

    /** the endpoint of the subscription representing the user device. */
    @DynamoDBAttribute
    private String endpointArn;

    /** the arn of the subscription. */
    @DynamoDBAttribute
    private String subscriptionArn;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTopicArn() {
        return topicArn;
    }

    public void setTopicArn(String topicArn) {
        this.topicArn = topicArn;
    }

    public String getEndpointArn() {
        return endpointArn;
    }

    public void setEndpointArn(String endpointArn) {
        this.endpointArn = endpointArn;
    }

    public String getSubscriptionArn() {
        return subscriptionArn;
    }

    public void setSubscriptionArn(String subscriptionArn) {
        this.subscriptionArn = subscriptionArn;
    }
}
