package com.juvodu.serverless.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.juvodu.database.model.*;
import com.juvodu.forecast.exception.WWOMClientException;
import com.juvodu.serverless.ParameterParser;
import com.juvodu.serverless.response.ApiGatewayResponse;
import com.juvodu.serverless.response.CrudSpotResponse;
import com.juvodu.service.SpotService;
import com.juvodu.service.WeatherService;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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
			body = new CrudSpotResponse(queryStringParameters, "Error retrieving spots by parameter.");
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
	private List<Spot> findSpotsByParameter(Map<String, String> queryStringParametersMap) throws WWOMClientException {

		// base spot class as list views only need partial data of a spot
		SpotService baseSpotService = new SpotService(BaseSpot.class);
		List<Spot> spots = new ArrayList<>();

		String ids = queryStringParametersMap.get("ids");
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

		if(queryStringParametersMap.isEmpty()){

			LOG.info("Return all spots: " + country);
			spots.addAll(baseSpotService.findAll());

		}else if(!StringUtils.isBlank(ids)) {

			LOG.info("Find spots by ids: " + ids);
			spots.addAll(baseSpotService.getSpotsByIds(ParameterParser.getSpotIds(ids)));

		}else if(!StringUtils.isAnyBlank(continent, country)){

			LOG.info("Find spots by country: " + country);
			Locale locale = new Locale("", country);
			spots.addAll(baseSpotService.findByCountry(Continent.valueOf(continent), new Country(locale.getCountry(), locale.getDisplayName()), limit));

		}else if (!StringUtils.isAnyBlank(continent, lat, lon, dist)){

			LOG.info("Find spots by distance: " + dist + " center: " + lat + "," + lon);
			int distance = Integer.valueOf(dist);
			double latitude = Double.valueOf(lat);
			double longitude = Double.valueOf(lon);
			spots.addAll(baseSpotService.findByDistance(Continent.valueOf(continent), new Position(latitude, longitude), distance, limit));

		}else if(StringUtils.isNotBlank(continent)){

			LOG.info("Find spots by continent: " + continent);
			spots.addAll(baseSpotService.findByContinent(Continent.valueOf(continent), limit));

		}

		return spots;
	}
}
