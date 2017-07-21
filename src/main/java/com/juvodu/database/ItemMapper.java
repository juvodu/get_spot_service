package com.juvodu.database;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.juvodu.database.model.Spot;

/**
 * Helper to Map DynamoDB Item to Models
 *
 * @author Juvodu
 */
public class ItemMapper {

    public static Spot createSpot(Item item){

        Spot spot = new Spot();
        spot.setId(item.get("id").toString());
        spot.setName(item.get("name").toString());
        spot.setDescription(item.get("description").toString());

        return spot;
    }
}
