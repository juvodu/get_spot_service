package com.juvodu.service;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.juvodu.database.model.BaseSpot;
import com.juvodu.database.model.User;

import java.util.List;

/**
 * Service for user retrieval and processing
 *
 * @author Juvodu
 */
public class UserService<T extends User> {

    private final DynamoDBMapper mapper;
    private final Class<T> userClass;

    public UserService(Class<T> userClass){

        this.userClass = userClass;
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
                .withRegion(Regions.EU_CENTRAL_1)
                .build();

        // configure dynamo DB mapper here
        DynamoDBMapperConfig mapperConfig = new DynamoDBMapperConfig.Builder()
                .withSaveBehavior(DynamoDBMapperConfig.SaveBehavior.UPDATE)
                .build();

        this.mapper = new DynamoDBMapper(client, mapperConfig);
    }

    /**
     * Retrieve a user by its hash key
     *
     * @param id
     *          of the user
     *
     * @return the user model populated with data
     */
    public T getUserById(String id){

        return mapper.load(userClass, id);
    }

    /**
     * Save or update a user instance
     *
     * @param user
     *          the user to save, if an item with the same id exists it will be updated
     *
     * @return the generated id (UUID) of the user
     */
    public String save(User user){

        // save does not return, instead it populates the generated id to the passed spot instance
        mapper.save(user);

        return user.getId();
    }

    /**
     * Delete the user instance
     *
     * @param user
     *          the user instance to delete
     */
    public void delete(T user){

        mapper.delete(user);
    }

    /**
     * Delete all table entries - for testing purposes only
     */
    public void deleteAll(){

        for(T user : findAll()){
            delete(user);
        }
    }

    /**
     * Return all available users, scan requests are potentially slow
     *
     * @return list of users saved in the DB
     */
    public List<T> findAll(){

        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
        return mapper.scan(userClass, scanExpression);
    }

    /**
     * @return the ARN the app was registered under previously, or null if no
     *         platform endpoint ARN is stored.
     */
    public String retrieveEndpointArnByUserId(String userId) {

        String arn = null;
        User user = getUserById(userId);
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
            User user = userClass.newInstance();
            user.setId(userId);
            user.setPlatformEndpointArn(endpointArn);
            save(user);
        } catch (InstantiationException | IllegalAccessException  e) {
            e.printStackTrace();
        }
    }
}
