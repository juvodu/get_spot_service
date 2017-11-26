package com.juvodu.serverless.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.juvodu.serverless.ParameterParser;
import com.juvodu.serverless.response.ApiGatewayResponse;
import com.juvodu.serverless.response.CrudResponse;
import org.apache.log4j.Logger;

import java.util.Map;

/**
 * Handler which retrieves a list of favorites for a given user.
 *
 * @author Juvodu
 */
public class GetFavoritesHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

    private static final Logger LOG = Logger.getLogger(GetFavoritesHandler.class);

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {

        String queryStringParameters = input.get("queryStringParameters").toString();
        LOG.info("Query String parameters: " + queryStringParameters);
        int statusCode = 200;

        Object body = null;

        try {

            Map<String, String> parameters = ParameterParser.getParameters(queryStringParameters);
            String userId = parameters.get("userId");

            //TODO: retrieve favorites


        } catch (Exception e) {

            statusCode = 500;
            body = new CrudResponse("Error retrieving favorites:" + e.getMessage());
            e.printStackTrace();
        }

        return ApiGatewayResponse.builder()
                .setStatusCode(statusCode)
                .setObjectBody(body)
                .build();
    }
}
