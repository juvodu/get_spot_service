package com.juvodu.serverless.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.juvodu.database.model.*;
import com.juvodu.serverless.response.ApiGatewayResponse;
import com.juvodu.serverless.response.CrudResponse;
import com.juvodu.service.*;
import com.juvodu.util.Constants;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Map;

/**
 * Handler to delete an existing user.
 *
 * @author Juvodu
 */
public class DeleteUserHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

    private static final Logger LOG = Logger.getLogger(DeleteUserHandler.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {

        LOG.info("Delete User:" + input);

        //API gateway puts json POST data into a body object
        Object body = input.get("body");

        SpotService<Spot> spotService = new SpotService(Spot.class);
        DeviceService<Device> deviceService = new DeviceService(Device.class);
        UserService<User> userService = new UserService(User.class);
        FavoriteService<Favorite> favoriteService = new FavoriteService(Favorite.class);
        SubscriptionService<Subscription> subscriptionService = new SubscriptionService(Subscription.class);

        int statusCode = 200;
        String userId = null;
        String message = "Deleted User successfully.";

        try {

            // get parameter
            JsonNode jsonNode = objectMapper.readTree(body.toString());
            userId = jsonNode.get("id").textValue();
            LOG.info("Delete User with Id:" + userId);

            // verify that user exists
            User user = userService.getByHashKey(userId);
            if(user != null) {

                // delete all user devices
                List<Device> devices = deviceService.getDevicesByUser(userId, Constants.MAX_USER_DEVICES);
                devices.stream().forEach(device -> deviceService.delete(device));

                // delete all subscriptions
                List<Favorite> favorites = favoriteService.getFavoritesByUser(userId, 100);
                for(Favorite favorite : favorites){

                    //TODO: unsubscribe from SNS

                    Spot spot = spotService.getByHashKey(favorite.getSpotId());
                    List<Subscription> subscriptions = subscriptionService.getByUserAndTopic(userId, spot.getTopicArn(), 100);
                    subscriptions.stream().forEach(subscription -> subscriptionService.delete(subscription));
                }

                // delete all favorites
                favorites.stream().forEach(favorite -> favoriteService.delete(favorite));

                // delete user
                userService.delete(user);
            }

        } catch (Exception e) {

            statusCode = 500;
            message = "Error: Could not delete User with id " + userId + ". User does not exist.";
            e.printStackTrace();
        }

        return ApiGatewayResponse.builder()
                .setStatusCode(statusCode)
                .setObjectBody(new CrudResponse(message))
                .build();
    }
}
