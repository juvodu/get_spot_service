package com.juvodu.serverless.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.juvodu.database.model.Spot;
import com.juvodu.serverless.response.ApiGatewayResponse;
import com.juvodu.service.SpotService;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Map;

/**
 * Handler which retrieves all Spots.
 *
 * @author Juvodu
 */
public class GetAllSpotsHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

	private static final Logger LOG = Logger.getLogger(GetAllSpotsHandler.class);

	@Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {

		LOG.info("Get all spots");

        SpotService spotService = new SpotService(Spot.class);
        List<Spot> spots = spotService.findAll();

        return ApiGatewayResponse.builder()
				.setStatusCode(200)
				.setObjectBody(spots)
				.build();
	}
}
