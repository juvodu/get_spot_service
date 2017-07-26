package com.juvodu.serverless.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.juvodu.database.model.Spot;
import com.juvodu.serverless.response.ApiGatewayResponse;
import com.juvodu.serverless.response.CrudSpotResponse;
import com.juvodu.service.SpotService;
import org.apache.log4j.Logger;

import java.util.Map;

/**
 * Handler for updating an existing Spot.
 *
 * @author Juvodu
 */
public class UpdateSpotHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {


    private static final Logger LOG = Logger.getLogger(UpdateSpotHandler.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {

        LOG.info("Update Spot:" + input);

        //API gateway puts json POST data into a body object
        Object body = input.get("body");

        String id = null;
        String message = "Updated Spot successfully.";
        int statusCode = 200;

        try {

            // parse post and update spot
            Spot spot = objectMapper.readValue(body.toString(), Spot.class);
            SpotService spotService = new SpotService(Spot.class);
            id = spotService.save(spot);

        }catch(Exception e){

            statusCode = 500;
            message = "Error: Could not update spot due to: " + e.getMessage();
            e.printStackTrace();
        }

        return ApiGatewayResponse.builder()
                .setStatusCode(statusCode)
                .setObjectBody(new CrudSpotResponse(id, message))
                .build();
    }
}
