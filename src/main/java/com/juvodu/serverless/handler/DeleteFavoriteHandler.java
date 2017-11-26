package com.juvodu.serverless.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.juvodu.database.model.Favorite;
import com.juvodu.database.model.Spot;
import com.juvodu.database.model.Subscription;
import com.juvodu.serverless.response.ApiGatewayResponse;
import com.juvodu.serverless.response.CrudResponse;
import com.juvodu.service.FavoriteService;
import com.juvodu.service.NotificationService;
import com.juvodu.service.SpotService;
import com.juvodu.service.SubscriptionService;
import org.apache.log4j.Logger;

import java.util.Map;

/**
 * Handler to delete a favorite and unsubscribe user from updates
 *
 * @author Juvodu
 */
public class DeleteFavoriteHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

    private static final Logger LOG = Logger.getLogger(DeleteFavoriteHandler.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {

        LOG.info("Delete Favorite:" + input);

        //API gateway puts json POST data into a body object
        Object body = input.get("body");

        SpotService<Spot> spotService = new SpotService(Spot.class);
        FavoriteService<Favorite> favoriteService = new FavoriteService(Favorite.class);
        SubscriptionService<Subscription> subscriptionService = new SubscriptionService(Subscription.class);
        NotificationService notificationService = new NotificationService();

        int statusCode = 200;
        String message = "Deleted Favorite successfully.";

        try {

            Favorite favorite = objectMapper.readValue(body.toString(), Favorite.class);
            Spot spot = spotService.getByHashKey(favorite.getSpotId());
            Subscription subscription = subscriptionService.getByUserAndTopic(favorite.getUserId(), spot.getTopicArn());

            // unsubscribe
            notificationService.unsubscribe(subscription.getSubscriptionArn());

            // delete subscription and favorite
            subscriptionService.delete(subscription);
            favoriteService.delete(favorite);

        } catch (Exception e) {

            statusCode = 500;
            message = "Could not delete Favorite: " + e.getMessage();
            e.printStackTrace();
        }

        return ApiGatewayResponse.builder()
                .setStatusCode(statusCode)
                .setObjectBody(new CrudResponse(message))
                .build();
    }
}
