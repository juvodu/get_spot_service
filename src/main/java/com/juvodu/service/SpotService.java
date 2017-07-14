package com.juvodu.service;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.juvodu.model.Spot;
import com.juvodu.util.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Juvodu on 01.07.17.
 */
public class SpotService {

    private final AmazonDynamoDB client;
    private final Table table;

    public SpotService(){

        this.client = AmazonDynamoDBClientBuilder.standard()
                .withRegion(Regions.EU_CENTRAL_1)
                .build();
        DynamoDB dynamoDB = new DynamoDB(client);
        table = dynamoDB.getTable(Constants.DB_NAME);
    }

    public List<Spot> findAllSpots(){

        ScanRequest scanRequest = new ScanRequest().withTableName(Constants.DB_NAME);
        ScanResult scanResult = client.scan(scanRequest);

        List<Spot> spots = new ArrayList<>();
        for (Map<String, AttributeValue> item : scanResult.getItems()) {
            Spot rating = new Spot();
            rating.setId(item.get(Constants.DB_FIELD_ID).getS());
            rating.setName(item.get(Constants.DB_FIELD_NAME).getS());
            rating.setDescription(item.get(Constants.DB_FIELD_DESC).getS());
            spots.add(rating);
        }
        return spots;
    }

    public String save(Spot spot){

        String id = UUID.randomUUID().toString();

        Item item = new Item();
        item.withString(Constants.DB_FIELD_ID, id);
        item.withString(Constants.DB_FIELD_NAME, spot.getName());
        item.withString(Constants.DB_FIELD_DESC, spot.getDescription());
        table.putItem(item);

        return id;
    }
}
