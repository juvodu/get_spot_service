package com.juvodu.serverless.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.juvodu.model.Spot;
import com.juvodu.serverless.ApiGatewayResponse;
import com.juvodu.service.SpotService;
import org.apache.log4j.Logger;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class GetAllSpotsHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

	private static final Logger LOG = Logger.getLogger(GetAllSpotsHandler.class);

	@Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {

		LOG.info("Get all spots");

        SpotService spotService = new SpotService();
        List<Spot> spots = spotService.findAllSpots();

        return ApiGatewayResponse.builder()
				.setStatusCode(200)
				.setObjectBody(spots)
				.build();
	}
}
