package com.juvodu.service;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.juvodu.database.model.Spot;

/**
 * Model representing the Spot table for testing
 *
 * @author Juvodu
 */
@DynamoDBTable(tableName = "spot_test")
public class SpotTestModel extends Spot{}
