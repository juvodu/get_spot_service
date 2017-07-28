package com.juvodu.serverless.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.juvodu.database.model.Continent;
import com.juvodu.database.model.Country;
import com.juvodu.database.model.Position;
import com.juvodu.database.model.Spot;
import com.juvodu.serverless.ParameterParser;
import com.juvodu.serverless.response.ApiGatewayResponse;
import com.juvodu.serverless.response.CrudSpotResponse;
import com.juvodu.service.SpotService;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Handler which retrieves a specific Spot
 *
 * @author Juvodu
 */
public class GetSpotByParameterHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

    private static final Logger LOG = Logger.getLogger(GetSpotByParameterHandler.class);

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {

        String queryStringParameters = input.get("queryStringParameters").toString();
        LOG.info("Query String parameters: " + queryStringParameters);

        int statusCode = 200;
        List<Object> results = new ArrayList<>();

        try {

            // find spots by parameter
            results.addAll(findSpotsByParameter(ParameterParser.getParameters(queryStringParameters)));

        } catch (Exception e) {

            statusCode = 500;
            results.add(new CrudSpotResponse(queryStringParameters, "Error retrieving spots by parameter."));
            e.printStackTrace();
        }

        return ApiGatewayResponse.builder()
                .setStatusCode(statusCode)
                .setObjectBody(results)
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
    private List<Spot> findSpotsByParameter(Map<String, String> queryStringParametersMap){

        SpotService spotService = new SpotService(Spot.class);
        List<Spot> spots = new ArrayList<>();

        //get spot by id
        String id = queryStringParametersMap.get("id");
        String continent = queryStringParametersMap.get("continent");
        String lat = queryStringParametersMap.get("lat");
        String lon = queryStringParametersMap.get("lon");
        String dist = queryStringParametersMap.get("distance");
        String country = queryStringParametersMap.get("country");

        LOG.info("Continent " + continent);
        LOG.info("Country " + country);

        if(StringUtils.isNotBlank(id)){

            LOG.info("Get spot by id: " + id);
            spots.add(spotService.getSpotById(id));

        }else if(!StringUtils.isAnyBlank(continent, country)){

            LOG.info("Find spots by country: " + country);
            Locale locale = new Locale("", country);
            spots.addAll(spotService.findByCountry(Continent.valueOf(continent), new Country(locale.getCountry(), locale.getDisplayName())));

        }else if (!StringUtils.isAnyBlank(continent, lat, lon, dist)){

            LOG.info("Find spots by distance: " + dist + " center: " + lat + "," + lon);
            int distance = Integer.valueOf(dist);
            double latitude = Double.valueOf(lat);
            double longitude = Double.valueOf(lon);
            spots.addAll(spotService.findByDistance(Continent.valueOf(continent), new Position(latitude, longitude), distance));

        }else if(StringUtils.isNotBlank(continent)){

            LOG.info("Find spots by continent: " + continent);
            spots.addAll(spotService.findByContinent(Continent.valueOf(continent)));

        }

        return spots;
    }
}