package com.juvodu.serverless.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.juvodu.database.model.BaseSpot;
import com.juvodu.database.model.Continent;
import com.juvodu.database.model.Spot;
import com.juvodu.forecast.exception.WWOMClientException;
import com.juvodu.forecast.model.Forecast;
import com.juvodu.forecast.model.Hourly;
import com.juvodu.serverless.response.ApiGatewayResponse;
import com.juvodu.service.SpotService;
import com.juvodu.service.WeatherService;
import org.apache.log4j.Logger;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Handler for recurring jobs - like a cron job.
 *
 * @author Juvodu
 */
public class CronSpotHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

    private static final Logger LOG = Logger.getLogger(CronSpotHandler.class);

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {

        LOG.info("Cron Spot Handler:" + input);
        int statusCode = 200;

        SpotService spotService = new SpotService(Spot.class);
        WeatherService weatherService = new WeatherService();

        // batch size of 100 spots
        List<Spot> spots = spotService.findByToBeUpdated(Continent.EU);

        for(Spot spot : spots){

            LOG.info("Updating spot " + spot.getId());

            try {

                Forecast forecast = weatherService.getForecastForPosition(spot.getPosition());
                Hourly hourly = forecast.getData().getWeather().get(0).getHourly().get(0);
                spot.setSwellHeight(hourly.getSwellHeightM());
                spot.setSwellPeriod(hourly.getSwellPeriodSecs());
                spot.setWindDescription(hourly.getWindspeedKmph() + "Kmph from " + hourly.getWinddir16Point());
                spot.setCronDate(new Date());
                spotService.save(spot);

            } catch (WWOMClientException e) {
                statusCode = 500;
                e.printStackTrace();
            }
        }

        return ApiGatewayResponse.builder()
                .setStatusCode(statusCode)
                .build();
    }
}
