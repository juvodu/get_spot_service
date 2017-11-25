package com.juvodu.service;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.juvodu.database.model.Favorite;

/**
 * TODO: description
 *
 * @author Juvodu
 */
public class FavoriteService<T extends Favorite> extends GenericPersistenceService<T>{

    /**
     * Ctor
     *
     * @param persistenceClass
     *              defines model service works with to vary between dev and prod databases
     */
    public FavoriteService(Class<T> persistenceClass){

        super(persistenceClass, DynamoDBMapperConfig.SaveBehavior.UPDATE);
    }
}
