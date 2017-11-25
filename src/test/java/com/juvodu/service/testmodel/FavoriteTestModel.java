package com.juvodu.service.testmodel;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.juvodu.database.model.Favorite;

/**
 * Model representing the Favorite table for testing
 *
 * @author Juvodu
 */
@DynamoDBTable(tableName = "favorite_test")
public class FavoriteTestModel extends Favorite{
}
