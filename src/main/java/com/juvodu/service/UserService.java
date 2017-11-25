package com.juvodu.service;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.juvodu.database.model.User;

/**
 * Service for user retrieval and processing
 *
 * @author Juvodu
 */
public class UserService<T extends User> extends GenericPersistenceService<T>{

    public UserService(Class<T> persistenceClass){
        super(persistenceClass, DynamoDBMapperConfig.SaveBehavior.UPDATE);
    }

    /**
     * @return the ARN the app was registered under previously, or null if no
     *         platform endpoint ARN is stored.
     */
    public String retrieveEndpointArnByUserId(String userId) {

        String arn = null;
        User user = getById(userId);
        if(user != null){
            arn = user.getPlatformEndpointArn();
        }

        return arn;
    }

    /**
     * Stores the platform endpoint ARN in permanent storage for lookup next time.
     */
    public void storeEndpointArn(String userId, String endpointArn) {

        try {
            T user = persistenceClass.newInstance();
            user.setId(userId);
            user.setPlatformEndpointArn(endpointArn);
            save(user);
        } catch (InstantiationException | IllegalAccessException  e) {
            e.printStackTrace();
        }
    }
}
