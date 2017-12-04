package com.juvodu.service;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.juvodu.database.DatabaseHelper;
import com.juvodu.database.model.Favorite;

import java.util.List;

/**
 * Service for storage, retrieval and processing of favorites
 *
 * @author Juvodu
 */
public class FavoriteService<T extends Favorite> extends GenericPersistenceService<T>{

    private final DatabaseHelper<T> databaseHelper;

    /**
     * Ctor
     *
     * @param persistenceClass
     *              defines model service works with to vary between dev and prod databases
     */
    public FavoriteService(Class<T> persistenceClass){

        super(persistenceClass, DynamoDBMapperConfig.SaveBehavior.UPDATE);
        this.databaseHelper = new DatabaseHelper();
    }

    /**
     * Get favorites by user
     *
     * @param userId
     *          the user the favorites belong to
     * @param limit
     *          the maximum results amount
     *
     * @return list of favorites
     */
    public List<T> getFavoritesByUser(String userId, int limit){

        String filterExpression = "userId = :val1";
        DynamoDBQueryExpression<T> queryExpression = databaseHelper.createQueryExpression(userId,
                null, filterExpression, limit);
        return mapper.queryPage(persistenceClass, queryExpression).getResults();
    }

    /**
     * Check if a spot is a user favorite
     *
     * @param userId
     *          the user the favorites belong to
     * @param spotId
     *           the referenced spot
     *
     * @return true if spot is a user favorite
     */
    public boolean isSpotUserFavorite(String userId, String spotId){

        T favorite = getByCompositeKey(userId, spotId);

        return favorite != null;
    }
}
