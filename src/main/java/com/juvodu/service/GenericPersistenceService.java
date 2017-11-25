package com.juvodu.service;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.juvodu.database.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Service providing basic persistence operations
 *
 * @author Juvodu
 */
public class GenericPersistenceService<T> {

    protected final DynamoDBMapper mapper;
    protected final Class<T> persistenceClass;

    /**
     * Ctor
     *
     * @param persistenceClass
     *              representing the table to be used
     * @param saveBehavior
     *               defines overwrite/null behaviour
     */
    public GenericPersistenceService(Class<T> persistenceClass, DynamoDBMapperConfig.SaveBehavior saveBehavior){

        this.persistenceClass = persistenceClass;
        AmazonDynamoDB dynamoDB = DatabaseHelper.getDynamoDB();
        DynamoDBMapperConfig mapperConfig = new DynamoDBMapperConfig.Builder()
                .withSaveBehavior(saveBehavior)
                .build();

        this.mapper = new DynamoDBMapper(dynamoDB, mapperConfig);
    }

    /**
     * Save or updates a record
     *
     * @param record
     *          the record to save, if an item with the same id exists it will be updated
     *
     * @return the generated id (UUID) of the record
     */
    public void save(T record){

        // save does not return, instead it populates the generated id to the passed record instance
        mapper.save(record);
    }

    /**
     * Retrieve a record by its hash key - only works on tables without a range key
     *
     * @param id
     *          of the record
     *
     * @return the retrieved record
     */
    public T getById(String id){

        return mapper.load(persistenceClass, id);
    }

    /**
     * Retrieve a list of records by its hash keys - only works on tables without a range key
     *
     * @param ids
     *          of the records
     * @return list of record models
     */
    public List<T> getByIds(List<String> ids){

        List<T> records = new ArrayList<>();
        for(String id : ids){
            records.add(getById(id));
        }
        return records;
    }

    /**
     * Delete the record instance
     *
     * @param record
     *          the record instance to delete
     */
    public void delete(T record){

        mapper.delete(record);
    }

    /**
     * Delete all records - for testing purposes only
     */
    public void deleteAll(){

        for(T record : findAll()){
            delete(record);
        }
    }

    /**
     * Return all available records, scan requests are potentially slow and expensive - only run on test data
     *
     * @return list of records saved in the DB
     */
    public List<T> findAll(){

        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
        return mapper.scan(persistenceClass, scanExpression);
    }
}
