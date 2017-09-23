package com.juvodu.service;

import com.juvodu.database.model.Position;
import com.juvodu.forecast.model.Forecast;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;


/**
 * Test suite for the WeatherService
 *
 * @author Juvodu
 */
public class WeatherServiceTest {

    private static WeatherService weatherService;
    private final Position hossegor = new Position(43.671223, -1.441445);

    @BeforeClass
    public static void beforeClass() {

        weatherService = new WeatherService();
    }

    @Test
    public void givenPositionWhenGetForecastForPositionThenReturnForecast() throws Exception {

        Forecast forecast = weatherService.getForecastForPosition(hossegor);
        assertNotNull(forecast);
    }

}