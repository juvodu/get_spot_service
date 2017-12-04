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

import java.util.Date;
import java.util.Map;

/**
 * Handler for registering a new device with push notifications
 *
 * @author Juvodu
 */
public class CreateDeviceHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

    private static final Logger LOG = Logger.getLogger(CreateDeviceHandler.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {

        LOG.info("Register device for push notification:" + input);

        //API gateway puts json POST data into a body object
        Object body = input.get("body");

        DeviceService<Device> deviceService = new DeviceService(Device.class);
        NotificationService notificationService = new NotificationService();

        String message = "Registered device successfully.";
        int statusCode = 200;

        try {
            // get parameter
            Date date = new Date();
            JsonNode jsonNode = objectMapper.readTree(body.toString());
            String username = jsonNode.get("username").textValue();
            String deviceToken = jsonNode.get("deviceToken").textValue();
            Device device = deviceService.getByCompositeKey(username, deviceToken);

            // lazy create
            if(device == null){
                device = new Device();
                device.setUsername(username);
                device.setDeviceToken(deviceToken);
                device.setCreatedDate(date);
            }

            // update platform endpoint
            String endpointArn = notificationService.registerDeviceForPushNotification(deviceToken, device.getPlatformEndpointArn());
            device.setPlatformEndpointArn(endpointArn);
            device.setUpdatedDate(date);
            deviceService.save(device);

        }catch(Exception e){

            statusCode = 500;
            message = "Error: Could not register device due to: " + e.getMessage();
            e.printStackTrace();
        }

        return ApiGatewayResponse.builder()
                .setStatusCode(statusCode)
                .setObjectBody(new CrudResponse(message))
                .build();
    }
}
