package com.juvodu.service;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.juvodu.database.DatabaseHelper;
import com.juvodu.database.model.Subscription;
import com.juvodu.util.Constants;

import java.util.List;

/**
 * Service for storage, retrieval and processing of subscription
 *
 * @author Juvodu
 */
public class SubscriptionService<T extends Subscription> extends GenericPersistenceService<T>{

    private final DatabaseHelper<T> databaseHelper;

    /**
     * Ctor
     *
     * @param persistenceClass
     *              defines model service works with to vary between dev and prod databases
     */
    public SubscriptionService(Class<T> persistenceClass){

        super(persistenceClass, DynamoDBMapperConfig.SaveBehavior.UPDATE);
        this.databaseHelper = new DatabaseHelper<>();
    }

    /**
     * Find all subscriptions of a specific user to a topic
     * (a user can have multiple devices and therefore multiple subscription to the same topic)
     *
     * @param username
     *           the user who subscribed
     * @param topicArn
     *            the topic the subscription relates to
     * @param limit
     *          the maximum results amount
     *
     * @return list of subscriptions
     */
    public List<T> getByUserAndTopic(String username, String topicArn, int limit){

        String filterExpression = "username = :val1 and topicArn = :val2";
        DynamoDBQueryExpression<T> queryExpression = databaseHelper.createIndexQueryExpression(username,
                topicArn, Constants.USERNAME_TOPIC_INDEX, filterExpression, limit);

        return mapper.queryPage(persistenceClass, queryExpression).getResults();
    }
}
