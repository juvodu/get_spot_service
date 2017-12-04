package com.juvodu.serverless.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
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
 * Handler to add a favorite and subscribe user for updates
 *
 * @author Juvodu
 */
public class CreateFavoriteHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

    private static final Logger LOG = Logger.getLogger(CreateFavoriteHandler.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {

        LOG.info("Create Favorite:" + input);

        //API gateway puts json POST data into a body object
        Object body = input.get("body");

        FavoriteService<Favorite> favoriteService = new FavoriteService<>(Favorite.class);
        SpotService<Spot> spotService = new SpotService(Spot.class);
        UserService<User> userService = new UserService(User.class);
        NotificationService notificationService = new NotificationService();
        SubscriptionService<Subscription> subscriptionService = new SubscriptionService(Subscription.class);
        DeviceService<Device> deviceService = new DeviceService(Device.class);

        int statusCode = 200;
        String message = "Created Favorite successfully.";

        try {

            // parse post and create spot
            Favorite favorite = objectMapper.readValue(body.toString(), Favorite.class);
            String username = favorite.getUsername();
            String spotId = favorite.getSpotId();

            // verify that favorite does not exist yet
            if(favoriteService.getByCompositeKey(username, spotId) == null){

                // get topic from spot and endpoint from user
                Spot spot = spotService.getByHashKey(spotId);
                User user = userService.getByHashKey(username);

                // verify that spot id and user id exist
                if(spot == null && user == null) {
                    throw new IllegalArgumentException("Spot/User does not exist!");
                }

                // subscribe all user devices (max 3 devices supported)
                List<Device> devices = deviceService.getDevicesByUser(username, Constants.MAX_USER_DEVICES);
                for(Device device : devices) {
                    String subscriptionArn = notificationService.subscribeToTopic(spot.getTopicArn(), device.getPlatformEndpointArn());
                    Subscription subscription = new Subscription();
                    subscription.setSubscriptionArn(subscriptionArn);
                    subscription.setTopicArn(spot.getTopicArn());
                    subscription.setEndpointArn(device.getPlatformEndpointArn());
                    subscription.setUsername(username);
                    subscriptionService.save(subscription);
                }

                // save favorite
                favoriteService.save(favorite);
            }
        } catch (Exception e) {

            statusCode = 500;
            message = "Error during favorite creation: " + e.getMessage();
            e.printStackTrace();
        }

        return ApiGatewayResponse.builder()
                .setStatusCode(statusCode)
                .setObjectBody(new CrudResponse(message))
                .build();
    }
}
