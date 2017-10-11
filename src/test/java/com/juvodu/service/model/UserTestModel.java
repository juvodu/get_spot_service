package com.juvodu.service.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.juvodu.database.model.User;

/**
 * Model representing the user table for testing
 *
 * @author Juvodu
 */
@DynamoDBTable(tableName = "user_test")
public class UserTestModel extends User {}
