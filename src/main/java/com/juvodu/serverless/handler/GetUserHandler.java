package com.juvodu.serverless.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.juvodu.database.model.User;
import com.juvodu.serverless.ParameterParser;
import com.juvodu.serverless.response.ApiGatewayResponse;
import com.juvodu.serverless.response.CrudResponse;
import com.juvodu.service.UserService;
import org.apache.log4j.Logger;

import java.util.Map;

/**
 * Handler which retrieves a user
 *
 * @author Juvodu
 */
public class GetUserHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

    private static final Logger LOG = Logger.getLogger(GetUserHandler.class);

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {

        String queryStringParameters = input.get("queryStringParameters").toString();
        LOG.info("Query String parameters: " + queryStringParameters);
        int statusCode = 200;

        UserService<User> userService = new UserService(User.class);
        Object body;

        try {

            Map<String, String> parameters = ParameterParser.getParameters(queryStringParameters);
            String username = parameters.get("username");

            body = userService.getByHashKey(username);

        } catch (Exception e) {

            statusCode = 500;
            body = new CrudResponse("Could not get user: " + e.getMessage());
            e.printStackTrace();
        }

        return ApiGatewayResponse.builder()
                .setStatusCode(statusCode)
                .setObjectBody(body)
                .build();
    }
}
