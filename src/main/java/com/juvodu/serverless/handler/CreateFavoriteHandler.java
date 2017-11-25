package com.juvodu.serverless.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.juvodu.database.model.Subscription;
import com.juvodu.serverless.ParameterParser;
import com.juvodu.serverless.response.ApiGatewayResponse;
import com.juvodu.serverless.response.CrudSpotResponse;
import com.juvodu.service.SubscriptionService;
import org.apache.log4j.Logger;

import java.util.Map;

/**
 * TODO: description
 *
 * @author Juvodu
 */
public class CreateFavoriteHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

    private static final Logger LOG = Logger.getLogger(CreateFavoriteHandler.class);

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {

        String queryStringParameters = input.get("queryStringParameters").toString();
        LOG.info("Query String parameters: " + queryStringParameters);
        int statusCode = 200;

        // TODO: check if favorite already exist
        // TODO: add favorite
        // subscribe and save subscription

        SubscriptionService subscriptionService = new SubscriptionService(Subscription.class);
        //subscriptionService.


        Object body;
        try {

            Map<String, String> parameters = ParameterParser.getParameters(queryStringParameters);
            String id = parameters.get("id");

            //Spot spot = (Spot) spotService.getSpotById(id);
            //Forecast forecast = getForecast(spot);
            //body = spot;

        } catch (Exception e) {

            statusCode = 500;
            body = new CrudSpotResponse(queryStringParameters, "Error during get spot by id.");
            e.printStackTrace();
        }

        return ApiGatewayResponse.builder()
                .setStatusCode(statusCode)
                .setObjectBody(null)
                .build();
    }
}
