package com.juvodu.serverless.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.juvodu.database.model.Spot;
import com.juvodu.serverless.response.ApiGatewayResponse;
import com.juvodu.serverless.response.CrudResponse;
import com.juvodu.service.NotificationService;
import com.juvodu.service.SpotService;
import org.apache.log4j.Logger;

import javax.management.Notification;
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
        String message = "Deleted Spot successfully.";
        NotificationService notificationService = new NotificationService();

        try {

            // get parameter
            JsonNode jsonNode = objectMapper.readTree(body.toString());
            String id = jsonNode.get("id").textValue();
            LOG.info("Delete Spot with Id:" + id);

            // delete spot
            SpotService<Spot> spotService = new SpotService(Spot.class);
            Spot spot = spotService.getByHashKey(id);
            String topicArn = spot.getTopicArn();
            notificationService.deleteTopic(topicArn);
            spotService.delete(spot);

        } catch (Exception e) {

            statusCode = 500;
            message = "Could not delete Spot: " + e.getMessage();
            e.printStackTrace();
        }

        return ApiGatewayResponse.builder()
                .setStatusCode(statusCode)
                .setObjectBody(new CrudResponse(message))
                .build();
    }
}
