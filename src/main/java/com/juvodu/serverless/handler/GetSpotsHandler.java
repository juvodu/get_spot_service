package com.juvodu.serverless.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.juvodu.database.model.*;
import com.juvodu.forecast.exception.WWOMClientException;
import com.juvodu.serverless.ParameterParser;
import com.juvodu.serverless.response.ApiGatewayResponse;
import com.juvodu.serverless.response.CrudResponse;
import com.juvodu.service.FavoriteService;
import com.juvodu.service.SpotService;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Handler which retrieves a list of Spots.
 *
 * @author Juvodu
 */
public class GetSpotsHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

	private static final Logger LOG = Logger.getLogger(GetSpotsHandler.class);

	@Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {

		String queryStringParameters = input.get("queryStringParameters").toString();
		LOG.info("Query String parameters: " + queryStringParameters);
		int statusCode = 200;

		Object body;

		try {

			body = findSpotsByParameter(ParameterParser.getParameters(queryStringParameters));

		} catch (Exception e) {

			statusCode = 500;
			body = new CrudResponse("Could not retrieve spots by parameter:" + e.getMessage());
			e.printStackTrace();
		}

        return ApiGatewayResponse.builder()
				.setStatusCode(statusCode)
				.setObjectBody(body)
				.build();
	}

	/**
	 * Method routes depending on parameters to correct service method of SpotService
	 *
	 * @param queryStringParametersMap
	 *          parameters specified by the client
	 *
	 * @return list of retrieved spots
	 */
	private List<BaseSpot> findSpotsByParameter(Map<String, String> queryStringParametersMap) throws WWOMClientException {

		// base spot class as list views only need partial data of a spot
		SpotService<BaseSpot> baseSpotService = new SpotService(BaseSpot.class);
		FavoriteService<Favorite> favoriteService = new FavoriteService<>(Favorite.class);
		List<BaseSpot> spots = new ArrayList<>();

		String username = queryStringParametersMap.get("username");
		String continent = queryStringParametersMap.get("continent");
		String lat = queryStringParametersMap.get("lat");
		String lon = queryStringParametersMap.get("lon");
		String dist = queryStringParametersMap.get("distance");
		String country = queryStringParametersMap.get("country");
		String limitStr = queryStringParametersMap.get("limit");

		// default list size is 10
		int limit = 10;
		if(StringUtils.isNotBlank(limitStr)){
			limit = Integer.parseInt(limitStr);
		}

		if(!StringUtils.isBlank(username)) {

			LOG.info("Find spots by user: " + username);
            List<Favorite> favorites = favoriteService.getFavoritesByUser(username, limit);
            if(favorites.size() > 0 ) {
                List<String> spotIds = favorites.stream().map(Favorite::getSpotId).collect(Collectors.toList());
                spots.addAll(baseSpotService.getByIds(spotIds));
            }

		}else if(!StringUtils.isAnyBlank(continent, country)){

			LOG.info("Find spots by country: " + country);
			Locale locale = new Locale("", country);
			spots.addAll(baseSpotService.findByCountry(Continent.valueOf(continent), new Country(locale.getCountry(), locale.getDisplayName()), limit));

		}else if (!StringUtils.isAnyBlank(lat, lon, dist)){

			LOG.info("Find spots by distance: " + dist + " center: " + lat + "," + lon);
			int distance = Integer.valueOf(dist);
			double latitude = Double.valueOf(lat);
			double longitude = Double.valueOf(lon);

			// check all continents
            for(Continent c : Continent.values()){
                spots.addAll(baseSpotService.findByDistance(c, new Position(latitude, longitude), distance, limit));
            }

            // sort again by distance now for all continents
            spots = spots.stream()
                    .sorted(Comparator.comparing(BaseSpot::getDistance))
                    .collect(Collectors.toList());

        }else if(StringUtils.isNotBlank(continent)){

			LOG.info("Find spots by continent: " + continent);
			spots.addAll(baseSpotService.findByContinent(Continent.valueOf(continent), limit));

		}

		return spots;
	}
}
