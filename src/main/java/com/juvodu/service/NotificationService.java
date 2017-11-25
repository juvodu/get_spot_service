package com.juvodu.service;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.*;
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
            // No platform endpoint ARN is stored; need to call createEndpoint.
            endpointArn = createEndpoint(deviceToken);
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
            endpointArn = createEndpoint(deviceToken);
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
    private String createEndpoint(String deviceToken) {

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
                // createEndpoint doesn't want to overwrite. Just use the
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
     * Notify subscribers of spot conditions
     *
     * @param spot
     *          to which users have subscribed
     */
    public void swellAlert(Spot spot){

        String topicArn = spot.getTopicArn();

        // push message to topic
        if(StringUtils.isNotBlank(topicArn)) {
            pushNotification(Platform.GCM, topicArn, "Nice swell alert!", "Dude, its on @ " + spot.getName());
        }
    }

    /**
     * Publish a message to a endpoint arn (a single mobile device)
     *
     * @param platform
     *              of the mobile device, currently only android supported
     * @param endpointArn
     *              of the mobile device
     * @param subject
     *              subject of the notification
     * @param messageText
     *              the message text
     *
     * @return id of the created message or null if failure
     */
    public String pushNotification(Platform platform, String endpointArn, String subject, String messageText){

        PublishRequest publishRequest = new PublishRequest();
        publishRequest.setMessageStructure("json");

        Map<String, String> messageMap = new HashMap<>();

        String message = null;
        switch (platform){
            case GCM:
                message = getAndroidMessageBody(subject, messageText);
                break;
            default:
                throw new IllegalArgumentException("Platform not supported : "
                        + platform.name());
        }

        messageMap.put(platform.name(), message);
        message = JsonHelper.jsonify(messageMap);

        publishRequest.setTargetArn(endpointArn);
        publishRequest.setMessage(message);

        PublishResult publishResult = snsClient.publish(publishRequest);
        return publishResult.getMessageId();
    }

    /**
     * Creates a message JSON structure for android devices and sets the attributes of the push notification
     *
     * @param subject
     *            the subject used for the notification
     * @param messageText
     *            the message text used for the notification
     *
     * @return the populated JSON message
     */
    private String getAndroidMessageBody(String subject, String messageText) {

        Map<String, Object> androidMessageMap = new HashMap<>();
        androidMessageMap.put("collapse_key", subject);
        androidMessageMap.put("data", getData(messageText));
        androidMessageMap.put("delay_while_idle", true);
        androidMessageMap.put("time_to_live", 500);
        androidMessageMap.put("dry_run", false);
        return JsonHelper.jsonify(androidMessageMap);
    }

    /**
     * Set platform independent the message text parameter
     * @param messageText
     *              to set
     * @return the message as a JSON parameter
     */
    private Map<String, String> getData(String messageText) {
        Map<String, String> payload = new HashMap<>();
        payload.put("message", messageText);
        return payload;
    }
}
