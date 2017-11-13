package com.juvodu.serverless.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.juvodu.database.model.User;
import com.juvodu.serverless.response.ApiGatewayResponse;
import com.juvodu.serverless.response.CrudSpotResponse;
import com.juvodu.service.NotificationService;
import com.juvodu.service.UserService;
import org.apache.log4j.Logger;

import java.util.Map;

/**
 * Handler for registering a new device with push notifications
 *
 * @author Juvodu
 */
public class RegisterPushHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

    private static final Logger LOG = Logger.getLogger(RegisterPushHandler.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {

        LOG.info("Register device for push notification:" + input);

        //API gateway puts json POST data into a body object
        Object body = input.get("body");

        String message = "Registered device successfully.";
        String userId = null;
        int statusCode = 200;

        try {
            // get parameter
            JsonNode jsonNode = objectMapper.readTree(body.toString());
            String deviceToken = jsonNode.get("deviceToken").textValue();
            userId = jsonNode.get("userId").textValue();
            UserService userService = new UserService(User.class);
            NotificationService notificationService = new NotificationService(userService);
            notificationService.registerDeviceForPushNotification(userId, deviceToken);

        }catch(Exception e){

            statusCode = 500;
            message = "Error: Could not register device due to: " + e.getMessage();
            e.printStackTrace();
        }

        return ApiGatewayResponse.builder()
                .setStatusCode(statusCode)
                .setObjectBody(new CrudSpotResponse(userId, message))
                .build();
    }
}
