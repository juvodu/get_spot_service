package com.juvodu.service;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.*;
import com.juvodu.database.model.BaseSpot;
import com.juvodu.database.model.Platform;
import com.juvodu.database.model.Spot;
import com.juvodu.util.Constants;
import com.juvodu.util.JsonHelper;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Service for push notifications via SNS to users
 *
 * @author Juvodu
 */
public class NotificationService {

    private AmazonSNS snsClient;

    public NotificationService(){

        this.snsClient = AmazonSNSClientBuilder.standard()
                .withRegion(Regions.EU_CENTRAL_1)
                .build();
    }

    /**
     * Register user for notifications following best practices outlined
     * http://docs.aws.amazon.com/sns/latest/dg/mobile-platform-endpoint.html
     *
     * @param deviceToken
     *              retrieved from the mobile operating system
     * @param endpointArn
     *              endpoint to be updated or null to create a new endpoint
     */
    public String registerDeviceForPushNotification(String deviceToken, String endpointArn) {

        boolean updateNeeded = false;
        boolean createNeeded = (null == endpointArn);

        if (createNeeded) {
            // No platform endpoint ARN is stored; need to call createPlatformEndpoint.
            endpointArn = createPlatformEndpoint(deviceToken);
            createNeeded = false;
        }

        // Look up the platform endpoint and make sure the data in it is current, even if
        // it was just created.
        try {
            GetEndpointAttributesRequest geaReq = new GetEndpointAttributesRequest().withEndpointArn(endpointArn);
            GetEndpointAttributesResult geaRes = snsClient.getEndpointAttributes(geaReq);

            updateNeeded = !geaRes.getAttributes().get("Token").equals(deviceToken)
                    || !geaRes.getAttributes().get("Enabled").equalsIgnoreCase("true");

        } catch (NotFoundException nfe) {
            // We had a stored ARN, but the platform endpoint associated with it
            // disappeared. Recreate it.
            createNeeded = true;
        }

        if (createNeeded) {
            endpointArn = createPlatformEndpoint(deviceToken);
        }


        if (updateNeeded) {
            // The platform endpoint is out of sync with the current data;
            // update the token and enable it.
            Map attribs = new HashMap();
            attribs.put("Token", deviceToken);
            attribs.put("Enabled", "true");
            SetEndpointAttributesRequest saeReq = new SetEndpointAttributesRequest().withEndpointArn(endpointArn).withAttributes(attribs);
            snsClient.setEndpointAttributes(saeReq);
        }
        return endpointArn;
    }

    /**
     *
     * @return the created endpoint but never null
     */
    private String createPlatformEndpoint(String deviceToken) {

        String endpointArn = null;

        try {

            CreatePlatformEndpointRequest cpeReq = new CreatePlatformEndpointRequest()
                            .withPlatformApplicationArn(Constants.SNS_APPLICATION_ARN)
                            .withToken(deviceToken);
            CreatePlatformEndpointResult cpeRes = snsClient.createPlatformEndpoint(cpeReq);
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
                // createPlatformEndpoint doesn't want to overwrite. Just use the
                // existing platform endpoint.
                endpointArn = m.group(1);
            } else {
                // Rethrow the exception, the input is actually bad.
                throw ipe;
            }
        }
        return endpointArn;
    }

    /**
     * Delete a platform endpoint from AWS
     *
     * @param endpointArn
     *          the endpoint to delete
     */
    public void deletePlatformEndpoint(String endpointArn){

        DeleteEndpointRequest deReq = new DeleteEndpointRequest()
                .withEndpointArn(endpointArn);
        DeleteEndpointResult deRes = snsClient.deleteEndpoint(deReq);
    }

    /**
     * Create a topic for publish/subscribe mechanism
     *
     * @param topicName
     *           of the topic to be created - needs to be unique
     * @return arn of the created topic
     */
    public String createTopic(String topicName){

        CreateTopicRequest createTopicRequest = new CreateTopicRequest(topicName);
        CreateTopicResult createTopicResult = snsClient.createTopic(createTopicRequest);
        return createTopicResult.getTopicArn();
    }

    /**
     * Subscribes an endpoint to a topic
     *
     * @param topicArn
     *            of the topic to be subscribed to
     * @param endpointArn
     *             to receive notifications
     *
     * @return the created subscription arn
     */
    public String subscribeToTopic(String topicArn, String endpointArn){

        SubscribeRequest subscribeRequest = new SubscribeRequest(topicArn, "application", endpointArn);
        SubscribeResult subscribeResult = snsClient.subscribe(subscribeRequest);
        return subscribeResult.getSubscriptionArn();
    }

    /**
     * Removes subscription from a topic
     *
     * @param subscriptionArn
     *              of the subscription to be removed
     */
    public void unsubscribe(String subscriptionArn){

        UnsubscribeRequest unsubscribeRequest = new UnsubscribeRequest(subscriptionArn);
        snsClient.unsubscribe(unsubscribeRequest);
    }

    /**
     * Notify subscribers about surf conditions at the subscribed spot
     *
     * @param spot
     *          to which users have subscribed and the swell alert relates to
     */
    public void swellAlert(Spot spot){

        String topicArn = spot.getTopicArn();

        // push message to topic
        if(StringUtils.isNotBlank(topicArn)) {
            swellNotification(Platform.GCM, topicArn, spot);
        }
    }

    /**
     *
     * Publish a message to a endpoint arn (a single mobile device)
     *
     * @param platform
     *              of the mobile device, currently only android supported
     * @param endpointArn
     *              of the mobile device or topic
     * @param spot
     *             the spot the alert relates to
     *
     * @return id of the created message or null if failure
     */
    public String swellNotification(Platform platform, String endpointArn, BaseSpot spot){

        return swellNotification(platform, endpointArn, null, spot);
    }

    /**
     * Publish a message to a endpoint arn (a single mobile device)
     *
     * @param platform
     *              of the mobile device, currently only android supported
     * @param endpointArn
     *              of the mobile device or topic
     * @param collapseKey
     *            the collapseKey used for the notification, notifications with the same collapse key are replaced by newer notifications
     * @param spot
     *              the spot the alert relates to
     *
     * @return id of the created message or null if failure
     */
    public String swellNotification(Platform platform, String endpointArn, String collapseKey, BaseSpot spot){

        PublishRequest publishRequest = new PublishRequest();
        publishRequest.setMessageStructure("json");

        Map<String, String> messageMap = new HashMap<>();

        String message = null;
        switch (platform){
            case GCM:
                message = getAndroidNotificationBodyForSwellAlert(collapseKey, spot);
                break;
            default:
                throw new IllegalArgumentException("Platform not supported : "
                        + platform.name());
        }

        messageMap.put(platform.name(), message);
        // @see: https://docs.aws.amazon.com/sns/latest/dg/mobile-push-send-custommessage.html
        // This is the default message which must be present when publishing a message to a topic.
        // The default message will only be used if a message is not present for
        // one of the notification platforms.
        messageMap.put("default", "Swell Alert for " + spot.getName());
        message = JsonHelper.jsonify(messageMap);

        publishRequest.setTargetArn(endpointArn);
        publishRequest.setMessage(message);

        PublishResult publishResult = snsClient.publish(publishRequest);
        return publishResult.getMessageId();
    }

    /**
     * Creates a message JSON structure for android devices and sets the attributes of the push notification
     * @see  <a href="https://developers.google.com/cloud-messaging/http-server-ref">GCM</a>
     *
     * @param collapseKey
     *            the collapseKey used for the notification, notifications with the same collapse key are replaced by newer notifications
     * @param spot
     *            the spot the alert relates to
     *
     * @return the populated JSON message
     */
    private String getAndroidNotificationBodyForSwellAlert(String collapseKey, BaseSpot spot) {

        Map<String, Object> androidMessageMap = new HashMap<>();
        if(StringUtils.isNotBlank(collapseKey)) {
            androidMessageMap.put("collapse_key", collapseKey);
        }
        androidMessageMap.put("data", getDataForSwellAlert(spot));
        return JsonHelper.jsonify(androidMessageMap);
    }

    /**
     * Creates a map with custom values inside the data object
     *
     * @param spot
     *          to which the swell alert relates to
     *
     * @return map of custom values for a swell alert
     */
    private Map<String, String> getDataForSwellAlert(BaseSpot spot){

        Map<String, String> payload = new HashMap<>();
        payload.put("message", "Swell Alert for " + spot.getName());
        payload.put("spotId", spot.getId());
        payload.put("notification_type", "swell_alert");

        return payload;
    }
}
