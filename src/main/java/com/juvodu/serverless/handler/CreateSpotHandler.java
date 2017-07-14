package com.juvodu.serverless.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.juvodu.model.Spot;
import com.juvodu.serverless.ApiGatewayResponse;
import com.juvodu.serverless.CreateSpotResponse;
import com.juvodu.service.SpotService;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Map;

/**
 * Created by Juvodu on 01.07.17.
 */
public class CreateSpotHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

private static final Logger LOG = Logger.getLogger(CreateSpotHandler.class);
private static final ObjectMapper objectMapper = new ObjectMapper();


    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {

        LOG.info("Create Spot:" + input);

        //API gateway puts json post data to a body object
        Object body = input.get("body");

        String id = null;
        String message = "Created Spot successfully.";
        int statusCode = 200;

        try {
            Spot spot = objectMapper.readValue(body.toString(), Spot.class);
            SpotService spotService = new SpotService();
            id = spotService.save(spot);
        }catch(IOException e){
            e.printStackTrace();
        }

        if(StringUtils.isBlank(id)){
            statusCode = 500;
            message = "Error: Incorrect spot data provided.";
        }

        CreateSpotResponse createSpotResponse = new CreateSpotResponse();
        createSpotResponse.setId(id);
        createSpotResponse.setMessage(message);

        return ApiGatewayResponse.builder()
        .setStatusCode(statusCode)
        .setObjectBody(createSpotResponse)
        .build();
    }
}