package com.juvodu.util;

import com.juvodu.database.model.Position;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Util class for geo calculations
 *
 * @author Juvodu
 */
public class GeoHelperTest {

    @Test
    public void givenTwoPositionsWhenGetDistanceThenReturnValidValue(){

        Position pos1 = new Position(51.061745, 13.735144);
        Position pos2 = new Position(52.536595, 13.391742);
        int distance = GeoHelper.getDistance(pos1, pos2);
        assertEquals(165686, distance);
    }
}
