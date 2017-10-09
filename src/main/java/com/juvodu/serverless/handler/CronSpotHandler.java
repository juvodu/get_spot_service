package com.juvodu.serverless.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.juvodu.database.model.BaseSpot;
import com.juvodu.database.model.Continent;
import com.juvodu.database.model.Spot;
import com.juvodu.forecast.exception.WWOMClientException;
import com.juvodu.forecast.model.Forecast;
import com.juvodu.forecast.model.Hourly;
import com.juvodu.forecast.model.Weather;
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

        long startTimeMilli = System.currentTimeMillis();
        int statusCode = 200;

        SpotService spotService = new SpotService(Spot.class);
        WeatherService weatherService = new WeatherService();

        // batch size of 200 spots
        List<Spot> spots = spotService.findByToBeUpdated(Continent.EU);
        LOG.info("Found " + spots.size() + " spots to update.");

        int updatedSpots = 0;

        for (Spot spot : spots) {

            LOG.info("Updating spot " + spot.getId());

            try {

                Forecast forecast = weatherService.getForecastForPosition(spot.getPosition());
                Hourly hourly = getLatestHourly(forecast);
                spot.setSwellHeight(hourly.getSwellHeightM());
                spot.setSwellPeriod(hourly.getSwellPeriodSecs());
                spot.setWindDescription(hourly.getWindspeedKmph() + "Kmph from " + hourly.getWinddir16Point());
                spot.setCronDate(new Date());
                spotService.save(spot);
                updatedSpots++;

            } catch (WWOMClientException e) {

                LOG.info("Error updating spot " + spot.getId());
                e.printStackTrace();
            }
        }

        long endTimeMilli = System.currentTimeMillis();
        long durationSec = (endTimeMilli - startTimeMilli)/1000;

        LOG.info("Updated " + updatedSpots + " successfully. Overall duration of cron job in seconds: " + durationSec);

        return ApiGatewayResponse.builder()
                .setStatusCode(statusCode)
                .build();
    }

    /**
     * Get the latest surf condition from the forecast wrapper
     *
     * @param forecast
     *           wrapper object
     * @return hourly containing latest surf condition
     * @throws WWOMClientException
     *              if latest surf conditions could not be retrieved
     */
    private Hourly getLatestHourly(Forecast forecast) throws WWOMClientException {


        if (forecast != null) {
            List<Weather> weatherList = forecast.getData().getWeather();

            if(!weatherList.isEmpty()){

                // get weather for today
                Weather weather = weatherList.get(0);

                List<Hourly> hourlyList = weather.getHourly();

                if(!hourlyList.isEmpty()) {

                    // get latest surf forecast
                    return hourlyList.get(0);
                }
            }
        }

        throw new WWOMClientException("Could not parse forecast: "  + forecast.toString());
    }
}
