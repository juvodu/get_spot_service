package com.juvodu.serverless.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.juvodu.database.model.*;
import com.juvodu.forecast.exception.WWOMClientException;
import com.juvodu.serverless.ParameterParser;
import com.juvodu.serverless.response.ApiGatewayResponse;
import com.juvodu.serverless.response.CrudSpotResponse;
import com.juvodu.service.SpotService;
import com.juvodu.service.WeatherService;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
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
        SpotService spotService = new SpotService(Spot.class);
        WeatherService weatherService = new WeatherService();
        Object body;

        try {

            Map<String, String> parameters = ParameterParser.getParameters(queryStringParameters);
            String id = parameters.get("id");

            Spot spot = (Spot) spotService.getSpotById(id);
            spot.setForecast(weatherService.getForecastForPosition(spot.getPosition()));
            body = spot;

        } catch (Exception e) {

            statusCode = 500;
            body = new CrudSpotResponse(queryStringParameters, "Error during get spot by id.");
            e.printStackTrace();
        }

        return ApiGatewayResponse.builder()
                .setStatusCode(statusCode)
                .setObjectBody(body)
                .build();
    }
}