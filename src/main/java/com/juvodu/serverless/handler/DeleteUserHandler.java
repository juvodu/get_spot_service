package com.juvodu.serverless.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.juvodu.database.model.User;
import com.juvodu.serverless.response.ApiGatewayResponse;
import com.juvodu.serverless.response.CrudResponse;
import com.juvodu.service.UserService;
import org.apache.log4j.Logger;

import java.util.Map;

/**
 * Handler to delete an existing user.
 *
 * @author Juvodu
 */
public class DeleteUserHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

    private static final Logger LOG = Logger.getLogger(DeleteUserHandler.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {

        LOG.info("Delete User:" + input);

        //API gateway puts json POST data into a body object
        Object body = input.get("body");

        int statusCode = 200;
        String id = null;
        String message = "Deleted User successfully.";

        try {

            // get parameter
            JsonNode jsonNode = objectMapper.readTree(body.toString());
            id = jsonNode.get("id").textValue();
            LOG.info("Delete User with Id:" + id);

            //TODO: delete all subscriptions and devices of the user

            // delete user
            UserService<User> userService = new UserService(User.class);
            User user = userService.getByHashKey(id);
            userService.delete(user);

        } catch (Exception e) {

            statusCode = 500;
            message = "Error: Could not delete User with id " + id + ". User does not exist.";
            e.printStackTrace();
        }

        return ApiGatewayResponse.builder()
                .setStatusCode(statusCode)
                .setObjectBody(new CrudResponse(message))
                .build();
    }
}
