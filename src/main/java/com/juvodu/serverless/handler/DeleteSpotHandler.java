package com.juvodu.serverless.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.juvodu.database.model.Spot;
import com.juvodu.serverless.response.ApiGatewayResponse;
import com.juvodu.serverless.response.CrudSpotResponse;
import com.juvodu.service.SpotService;
import org.apache.log4j.Logger;

import java.util.Map;

/**
 * Handler for deleting an existing Spot.
 *
 * @author Juvodu
 */
public class DeleteSpotHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

    private static final Logger LOG = Logger.getLogger(DeleteSpotHandler.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {

        LOG.info("Delete Spot:" + input);

        //API gateway puts json POST data into a body object
        Object body = input.get("body");

        int statusCode = 200;
        String id = null;
        String message = "Deleted Spot successfully.";

        try {

            // get parameter
            JsonNode jsonNode = objectMapper.readTree(body.toString());
            id = jsonNode.get("id").textValue();
            LOG.info("Delete Spot with Id:" + id);

            // delete spot
            SpotService spotService = new SpotService(Spot.class);
            Spot spot = spotService.getSpotById(id);
            spotService.delete(spot);

        } catch (Exception e) {

            statusCode = 500;
            message = "Error: Could not delete Spot with id " + id + ". Spot does not exist.";
            e.printStackTrace();
        }

        return ApiGatewayResponse.builder()
                .setStatusCode(statusCode)
                .setObjectBody(new CrudSpotResponse(id, message))
                .build();
    }
}
