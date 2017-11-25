package com.juvodu.service;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.juvodu.database.DatabaseHelper;
import com.juvodu.database.model.Subscription;

/**
 * TODO: description
 *
 * @author Juvodu
 */
public class SubscriptionService<T extends Subscription> {

    private final DynamoDBMapper mapper;
    private final Class<T> subscriptionClass;

    /**
     * Ctor
     *
     * @param subscriptionClass
     *              defines model service works with to vary between dev and prod databases
     */
    public SubscriptionService(Class<T> subscriptionClass){

        this.subscriptionClass = subscriptionClass;
        AmazonDynamoDB dynamoDB = DatabaseHelper.getDynamoDB();

        // configure dynamo DB mapper here
        DynamoDBMapperConfig mapperConfig = new DynamoDBMapperConfig.Builder()
                .withSaveBehavior(DynamoDBMapperConfig.SaveBehavior.UPDATE).build();
        this.mapper = new DynamoDBMapper(dynamoDB, mapperConfig);
    }



}
