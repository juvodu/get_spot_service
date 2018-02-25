package com.juvodu.serverless.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.juvodu.database.model.Device;
import com.juvodu.serverless.response.ApiGatewayResponse;
import com.juvodu.serverless.response.CrudResponse;
import com.juvodu.service.DeviceService;
import com.juvodu.service.NotificationService;
import org.apache.log4j.Logger;

import java.util.Map;

/**
 * Handler to delete an existing device.
 *
 * @author Juvodu
 */
public class DeleteDeviceHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

    private static final Logger LOG = Logger.getLogger(DeleteDeviceHandler.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {

        LOG.info("Delete Device:" + input);

        //API gateway puts json POST data into a body object
        Object body = input.get("body");

        DeviceService<Device> deviceService = new DeviceService(Device.class);
        NotificationService notificationService = new NotificationService();

        int statusCode = 200;
        String message = "Deleted Device successfully. No more push notifications will be send.";

        try {

            // get parameter
            JsonNode jsonNode = objectMapper.readTree(body.toString());
            String username = jsonNode.get("username").textValue();
            String deviceToken = jsonNode.get("deviceToken").textValue();
            Device device = deviceService.getByCompositeKey(username, deviceToken);

            //TODO: delete subscriptions from DB and delete from AWS

            // delete platform endpoint
            notificationService.deletePlatformEndpoint(device.getPlatformEndpointArn());

            // delete device
            deviceService.delete(device);

        } catch (Exception e) {

            statusCode = 500;
            message = "Error: Could not delete device: " + e.getMessage();
            e.printStackTrace();
        }

        return ApiGatewayResponse.builder()
                .setStatusCode(statusCode)
                .setObjectBody(new CrudResponse(message))
                .build();
    }
}
