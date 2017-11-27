package com.juvodu.service.testmodel;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.juvodu.database.model.Device;

/**
 * Model representing the Device table for testing
 *
 * @author Juvodu
 */
@DynamoDBTable(tableName = "device_test")
public class DeviceTestModel extends Device {}
