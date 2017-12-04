package com.juvodu.serverless.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.juvodu.database.model.Favorite;
import com.juvodu.database.model.Spot;
import com.juvodu.forecast.model.Forecast;
import com.juvodu.serverless.ParameterParser;
import com.juvodu.serverless.response.ApiGatewayResponse;
import com.juvodu.serverless.response.CrudResponse;
import com.juvodu.service.FavoriteService;
import com.juvodu.service.SpotService;
import com.juvodu.service.WeatherService;
import org.apache.log4j.Logger;

import java.util.Map;

/**
 * Handler which retrieves a Spot with detailed information
 * including weather forecast
 *
 * @author Juvodu
 */
public class GetSpotHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

    private static final Logger LOG = Logger.getLogger(GetSpotHandler.class);

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {

        String queryStringParameters = input.get("queryStringParameters").toString();
        LOG.info("Query String parameters: " + queryStringParameters);
        int statusCode = 200;

        // use detailed spot class
        SpotService<Spot> spotService = new SpotService(Spot.class);
        FavoriteService<Favorite> favoriteService = new FavoriteService(Favorite.class);
        Object body;

        try {

            Map<String, String> parameters = ParameterParser.getParameters(queryStringParameters);
            String spotId = parameters.get("spotId");
            String username = parameters.get("username");

            Spot spot = spotService.getByHashKey(spotId);
            Forecast forecast = getForecast(spot);
            spot.setForecast(forecast);

            // populate if spot is favorite of user
            boolean favorite = favoriteService.isSpotUserFavorite(username, spotId);
            spot.setFavorite(favorite);

            body = spot;

        } catch (Exception e) {

            statusCode = 500;
            body = new CrudResponse("Could not get spot: " + e.getMessage());
            e.printStackTrace();
        }

        return ApiGatewayResponse.builder()
                .setStatusCode(statusCode)
                .setObjectBody(body)
                .build();
    }

    /**
     * Get forecast for the spot
     *
     * @param spot
     *          for which the forecast will be retrieved
     * @return the forecast or null if error occured
     */
    private Forecast getForecast(Spot spot){

        WeatherService weatherService = new WeatherService();
        Forecast forecast = null;

        try {

            forecast = weatherService.getForecastForPosition(spot.getPosition());

        } catch (Exception e) {

            LOG.warn("Error retrieving forecast for spot " + spot.getId());
            e.printStackTrace();
        }

        return forecast;
    }
}