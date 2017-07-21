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
import com.juvodu.database.ItemMapper;
import com.juvodu.database.model.Spot;
import com.juvodu.util.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Service for Spot retrieval and processing.
 *
 * @author Juvodu
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

    public Spot getSpotById(String id){

        Item item = table.getItem("id", id);
        return ItemMapper.createSpot(item);
    }

    public List<Spot> findAllSpots(){

        ScanRequest scanRequest = new ScanRequest().withTableName(Constants.DB_NAME);
        ScanResult scanResult = client.scan(scanRequest);

        List<Spot> spots = new ArrayList<>();
        for (Map<String, AttributeValue> item : scanResult.getItems()) {
            Spot spot = new Spot();
            spot.setId(item.get(Constants.DB_FIELD_ID).getS());
            spot.setName(item.get(Constants.DB_FIELD_NAME).getS());
            spot.setDescription(item.get(Constants.DB_FIELD_DESC).getS());
            spots.add(spot);
        }
        return spots;
    }

    public String save(Spot spot){

        String id = UUID.randomUUID().toString();
        spot.setId(id);
        return update(spot);
    }

    public String update(Spot spot){

        Item item = new Item();
        item.withString(Constants.DB_FIELD_ID, spot.getId());
        item.withString(Constants.DB_FIELD_NAME, spot.getName());
        item.withString(Constants.DB_FIELD_DESC, spot.getDescription());
        table.putItem(item);

        return spot.getId();
    }

    public void delete(String id){

        table.deleteItem("id", id);
    }
}
