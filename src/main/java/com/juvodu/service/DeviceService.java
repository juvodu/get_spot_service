package com.juvodu.service;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.juvodu.database.model.Device;

/**
 * Service for storage, retrieval and processing of devices
 *
 * @author Juvodu
 */
public class DeviceService <T extends Device> extends GenericPersistenceService<T>{

    /**
     * Ctor
     *
     * @param persistenceClass
     *              defines model service works with to vary between dev and prod databases
     */
    public DeviceService(Class<T> persistenceClass){

        super(persistenceClass, DynamoDBMapperConfig.SaveBehavior.UPDATE);
    }
}
