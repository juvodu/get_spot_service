package com.juvodu.service;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.*;
import com.juvodu.database.model.User;
import com.juvodu.util.Constants;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Service for push notifications via SNS to users
 *
 * @author Juvodu
 */
public class NotificationService<T extends User> {

    private UserService userService;
    private AmazonSNS client;

    public NotificationService(UserService userService){

        this.userService = userService;
        this.client = AmazonSNSClientBuilder.standard()
                .withRegion(Regions.EU_CENTRAL_1)
                .build();
    }

    /**
     * Register user for notifications following best practices outlined
     * http://docs.aws.amazon.com/sns/latest/dg/mobile-platform-endpoint.html
     *
     * @param userId
     *              user to register for notifications
     * @param deviceToken
     *              retrieved from the mobile operating system
     */
    public void registerDeviceForPushNotification(String userId, String deviceToken) {

        String endpointArn = userService.retrieveEndpointArnByUserId(userId);

        boolean updateNeeded = false;
        boolean createNeeded = (null == endpointArn);

        if (createNeeded) {
            // No platform endpoint ARN is stored; need to call createEndpoint.
            endpointArn = createEndpoint(userId, deviceToken);
            createNeeded = false;
        }

        // Look up the platform endpoint and make sure the data in it is current, even if
        // it was just created.
        try {
            GetEndpointAttributesRequest geaReq = new GetEndpointAttributesRequest().withEndpointArn(endpointArn);
            GetEndpointAttributesResult geaRes = client.getEndpointAttributes(geaReq);

            updateNeeded = !geaRes.getAttributes().get("Token").equals(deviceToken)
                    || !geaRes.getAttributes().get("Enabled").equalsIgnoreCase("true");

        } catch (NotFoundException nfe) {
            // We had a stored ARN, but the platform endpoint associated with it
            // disappeared. Recreate it.
            createNeeded = true;
        }

        if (createNeeded) {
            createEndpoint(userId, deviceToken);
        }


        if (updateNeeded) {
            // The platform endpoint is out of sync with the current data;
            // update the token and enable it.
            Map attribs = new HashMap();
            attribs.put("Token", deviceToken);
            attribs.put("Enabled", "true");
            SetEndpointAttributesRequest saeReq = new SetEndpointAttributesRequest().withEndpointArn(endpointArn).withAttributes(attribs);
            client.setEndpointAttributes(saeReq);
        }
    }

    /**
     *
     * @return the created endpoint but never null
     */
    private String createEndpoint(String userId, String deviceToken) {

        String endpointArn = null;

        try {

            CreatePlatformEndpointRequest cpeReq = new CreatePlatformEndpointRequest()
                            .withPlatformApplicationArn(Constants.SNS_APPLICATION_ARN)
                            .withToken(deviceToken);
            CreatePlatformEndpointResult cpeRes = client.createPlatformEndpoint(cpeReq);
            endpointArn = cpeRes.getEndpointArn();

        } catch (InvalidParameterException ipe) {

            String message = ipe.getErrorMessage();
            Pattern p = Pattern
                    .compile(".*Endpoint (arn:aws:sns[^ ]+) already exists " +
                            "with the same token.*");
            Matcher m = p.matcher(message);
            if (m.matches()) {
                // The platform endpoint already exists for this token, but with
                // additional custom data that
                // createEndpoint doesn't want to overwrite. Just use the
                // existing platform endpoint.
                endpointArn = m.group(1);
            } else {
                // Rethrow the exception, the input is actually bad.
                throw ipe;
            }
        }
        userService.storeEndpointArn(userId, endpointArn);
        return endpointArn;
    }
}
