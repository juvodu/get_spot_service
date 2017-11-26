package com.juvodu.serverless.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.juvodu.database.model.BaseSpot;
import com.juvodu.database.model.Favorite;
import com.juvodu.serverless.ParameterParser;
import com.juvodu.serverless.response.ApiGatewayResponse;
import com.juvodu.serverless.response.CrudResponse;
import com.juvodu.service.FavoriteService;
import com.juvodu.service.SpotService;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

        FavoriteService<Favorite> favoriteService = new FavoriteService<>(Favorite.class);
        SpotService<BaseSpot> spotService = new SpotService(BaseSpot.class);
        Object body;

        try {

            Map<String, String> parameters = ParameterParser.getParameters(queryStringParameters);
            String userId = parameters.get("userId");
            String limitStr = parameters.get("limit");

            // default list size is 10
            int limit = 10;
            if(StringUtils.isNotBlank(limitStr)){
                limit = Integer.parseInt(limitStr);
            }

            // get favorite spots
            List<Favorite> favorites = favoriteService.getFavoritesByUser(userId, limit);
            List<BaseSpot> spots = new ArrayList();
            if(favorites.size() > 0 ) {
                List<String> spotIds = favorites.stream().map(Favorite::getSpotId).collect(Collectors.toList());
                spots = spotService.getByIds(spotIds);
            }

            body = spots;
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
