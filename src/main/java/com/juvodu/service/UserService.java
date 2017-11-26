package com.juvodu.service;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.juvodu.database.model.User;

/**
 * Service for storage, retrieval and processing of users
 *
 * @author Juvodu
 */
public class UserService<T extends User> extends GenericPersistenceService<T>{

    public UserService(Class<T> persistenceClass){
        super(persistenceClass, DynamoDBMapperConfig.SaveBehavior.UPDATE);
    }
}
