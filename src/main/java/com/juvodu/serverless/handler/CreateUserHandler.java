package com.juvodu.serverless.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.juvodu.database.model.User;
import com.juvodu.serverless.response.ApiGatewayResponse;
import com.juvodu.serverless.response.CrudSpotResponse;
import com.juvodu.service.UserService;
import org.apache.log4j.Logger;

import java.util.Map;

/**
 * Handler for creating a new user, called upon registration
 *
 * @author Juvodu
 */
public class CreateUserHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

    private static final Logger LOG = Logger.getLogger(CreateUserHandler.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {

        LOG.info("Create User:" + input);

        //API gateway puts json POST data into a body object
        Object body = input.get("body");

        String message = "Created User successfully.";
        String id = null;
        int statusCode = 200;

        try {

            // parse post and create spot
            User user = objectMapper.readValue(body.toString(), User.class);
            UserService<User> userService = new UserService(User.class);
            userService.save(user);
            id = user.getId();

        }catch(Exception e){

            statusCode = 500;
            message = "Error: Could not create user due to: " + e.getMessage();
            e.printStackTrace();
        }

        return ApiGatewayResponse.builder()
                .setStatusCode(statusCode)
                .setObjectBody(new CrudSpotResponse(id, message))
                .build();
    }
}
