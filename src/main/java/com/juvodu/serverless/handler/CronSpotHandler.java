package com.juvodu.serverless.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.juvodu.serverless.response.ApiGatewayResponse;
import org.apache.log4j.Logger;

import java.util.Map;

/**
 * Handler for recurring jobs - like a cron job.
 *
 * @author Juvodu
 */
public class CronSpotHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

    private static final Logger LOG = Logger.getLogger(CronSpotHandler.class);

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {

        LOG.info("Cron Spot Handler:" + input);


        return null;
    }
}
