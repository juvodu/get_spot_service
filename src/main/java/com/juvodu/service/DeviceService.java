package com.juvodu.service;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.juvodu.database.DatabaseHelper;
import com.juvodu.database.model.Device;

import java.util.List;

/**
 * Service for storage, retrieval and processing of devices
 *
 * @author Juvodu
 */
public class DeviceService <T extends Device> extends GenericPersistenceService<T>{

    private final DatabaseHelper<T> databaseHelper;

    /**
     * Ctor
     *
     * @param persistenceClass
     *              defines model service works with to vary between dev and prod databases
     */
    public DeviceService(Class<T> persistenceClass){

        super(persistenceClass, DynamoDBMapperConfig.SaveBehavior.UPDATE);
        this.databaseHelper = new DatabaseHelper();
    }

    /**
     * Get devices by user
     *
     * @param userId
     *          the user the devices belong to
     * @param limit
     *          the maximum results amount
     *
     * @return list of devices
     */
    public List<T> getDevicesByUser(String userId, int limit){

        String filterExpression = "userId = :val1";
        DynamoDBQueryExpression<T> queryExpression = databaseHelper.createQueryExpression(userId,
                null, filterExpression, limit);
        return mapper.queryPage(persistenceClass, queryExpression).getResults();
    }
}
