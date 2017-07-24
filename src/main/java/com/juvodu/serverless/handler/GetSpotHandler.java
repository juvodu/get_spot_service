package com.juvodu.serverless.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.juvodu.database.model.Spot;
import com.juvodu.serverless.ParameterParser;
import com.juvodu.serverless.response.ApiGatewayResponse;
import com.juvodu.serverless.response.CrudSpotResponse;
import com.juvodu.service.SpotService;
import org.apache.log4j.Logger;

import java.util.Map;

/**
 * Handler which retrieves a specific Spot
 *
 * @author Juvodu
 */
public class GetSpotHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

    private static final Logger LOG = Logger.getLogger(GetSpotHandler.class);

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {

        String queryStringParameters = input.get("queryStringParameters").toString();
        LOG.info("Query String parameters: " + queryStringParameters);

        Object result;
        String id = null;
        int statusCode = 200;

        try {

            // get parameters
            Map<String, String> queryStringParametersMap = ParameterParser.getParameters(queryStringParameters);
            id = queryStringParametersMap.get("id");
            LOG.info("Get spot by id: " + id);

            //get spot
            SpotService spotService = new SpotService(Spot.class);
            result = spotService.getSpotById(id);

        } catch (Exception e) {

            statusCode = 500;
            result = new CrudSpotResponse(id, "Could not get spot with id " + id + ". Spot does not exist.");
            e.printStackTrace();
        }

        return ApiGatewayResponse.builder()
                .setStatusCode(statusCode)
                .setObjectBody(result)
                .build();
    }
}