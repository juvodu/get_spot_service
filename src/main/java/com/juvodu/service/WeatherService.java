package com.juvodu.service;

import com.juvodu.database.model.Position;
import com.juvodu.forecast.controller.WWOMClient;
import com.juvodu.forecast.exception.WWOMClientException;
import com.juvodu.forecast.model.Forecast;

/**
 * Service for retrieval of the maritim weather conditions.
 *
 * @author Juvodu
 */
public class WeatherService {


    private WWOMClient client;

    public WeatherService(){
        this.client = new WWOMClient();
    }

    /**
     * Get the maritim weather forecast for a position
     *
     * @param position
     * @return
     * @throws WWOMClientException
     */
    public Forecast getForecastForPosition(Position position) throws WWOMClientException {

        return client.getForecast(String.format("%s, %s", position.getLatitude(), position.getLongitude()), true, 6, true, null);
    }
}
