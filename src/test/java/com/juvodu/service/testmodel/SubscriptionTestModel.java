package com.juvodu.service.testmodel;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.juvodu.database.model.Subscription;

/**
 * Model representing the Subscription table for testing
 *
 * @author Juvodu
 */
@DynamoDBTable(tableName = "subscription_test")
public class SubscriptionTestModel extends Subscription{
}
