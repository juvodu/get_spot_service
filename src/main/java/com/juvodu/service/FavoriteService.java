package com.juvodu.service;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.juvodu.database.DatabaseHelper;
import com.juvodu.database.model.Favorite;

/**
 * TODO: description
 *
 * @author Juvodu
 */
public class FavoriteService<T extends Favorite> {

    private final DynamoDBMapper mapper;
    private final Class<T> favoriteClass;

    /**
     * Ctor
     *
     * @param favoriteClass
     *              defines model service works with to vary between dev and prod databases
     */
    public FavoriteService(Class<T> favoriteClass){

        this.favoriteClass = favoriteClass;
        AmazonDynamoDB dynamoDB = DatabaseHelper.getDynamoDB();

        // configure dynamo DB mapper here
        DynamoDBMapperConfig mapperConfig = new DynamoDBMapperConfig.Builder()
                .withSaveBehavior(DynamoDBMapperConfig.SaveBehavior.UPDATE).build();
        this.mapper = new DynamoDBMapper(dynamoDB, mapperConfig);
    }

    public void getFavorite(String userId, String spotId){

        mapper.load(favoriteClass, userId);
    }
}
