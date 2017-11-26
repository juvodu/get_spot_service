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
     * Find subscription of a specific user to a topic
     *
     * @param userId
     *           the user who subscribed
     * @param topicArn
     *            the topic the subscription relates to
     * @return
     */
    public T getByUserAndTopic(String userId, String topicArn){

        String filterExpression = "userId = :val1 and topicArn = :val2";
        DynamoDBQueryExpression<T> queryExpression = databaseHelper.createIndexQueryExpression(userId,
                topicArn, Constants.USER_TOPIC_INDEX, filterExpression, 1);

        List<T> subscriptions = mapper.queryPage(persistenceClass, queryExpression).getResults();

        T subscription = null;
        if(subscriptions.size() > 0){
            subscription = subscriptions.get(0);
        }

        return subscription;
    }
}
