package com.juvodu.serverless.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.juvodu.database.model.Favorite;
import com.juvodu.database.model.Spot;
import com.juvodu.database.model.Subscription;
import com.juvodu.database.model.User;
import com.juvodu.serverless.ParameterParser;
import com.juvodu.serverless.response.ApiGatewayResponse;
import com.juvodu.serverless.response.CrudSpotResponse;
import com.juvodu.service.*;
import org.apache.log4j.Logger;

import java.util.Map;

/**
 * Handler to add a favorite and subscribe user for updates
 *
 * @author Juvodu
 */
public class CreateFavoriteHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

    private static final Logger LOG = Logger.getLogger(CreateFavoriteHandler.class);

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {

        FavoriteService<Favorite> favoriteService = new FavoriteService<>(Favorite.class);
        SpotService<Spot> spotService = new SpotService(Spot.class);
        UserService<User> userService = new UserService(User.class);
        NotificationService notificationService = new NotificationService();
        SubscriptionService<Subscription> subscriptionService = new SubscriptionService(Subscription.class);
        String queryStringParameters = input.get("queryStringParameters").toString();
        LOG.info("Query String parameters: " + queryStringParameters);
        int statusCode = 200;

        Object body;
        try {

            Map<String, String> parameters = ParameterParser.getParameters(queryStringParameters);
            String userId = parameters.get("userId");
            String spotId = parameters.get("spotId");

            // verify that favorite does not exist yet
            if(favoriteService.getByCompositeKey(userId, spotId) == null){

                // save favorite
                Favorite favorite = new Favorite();
                favorite.setUserId(userId);
                favorite.setSpotId(spotId);
                favoriteService.save(favorite);

                // get topic from spot and endpoint from user
                Spot spot = spotService.getByHashKey(spotId);
                User user = userService.getByHashKey(userId);

                // subscribe
                String subscriptionArn = notificationService.subscribeToTopic(spot.getTopicArn(), user.getPlatformEndpointArn());
                Subscription subscription = new Subscription();
                subscription.setSubscriptionArn(subscriptionArn);
                subscription.setTopicArn(spot.getTopicArn());
                subscription.setEndpointArn(user.getPlatformEndpointArn());
                subscription.setUserId(userId);
                subscriptionService.save(subscription);
            }

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
