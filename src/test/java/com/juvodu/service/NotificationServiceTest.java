package com.juvodu.service;

import com.juvodu.database.model.Platform;
import com.juvodu.database.model.Spot;
import com.juvodu.service.testmodel.UserTestModel;
import com.juvodu.util.Constants;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

/**
 * Test suite for the NotificationService
 *
 * @author Juvodu
 */
public class NotificationServiceTest {

    private static NotificationService notificationService;
    private static UserService<UserTestModel> userService;

    @BeforeClass
    public static void beforeClass(){
        userService = new UserService(UserTestModel.class);
        notificationService = new NotificationService();
    }

    @Before
    public void before(){

        userService.deleteAll();
    }

    @Test
    public void givenNoArnAndNoEndpointWhenRegisterDeviceForPushNotificationThenCreateEndpoint(){

        // setup
        String deviceToken = UUID.randomUUID().toString();

        // execute
        String endpointArn = notificationService.registerDeviceForPushNotification(deviceToken, null);

        // verify
        assertNotNull(endpointArn);
    }

    @Test
    public void givenArnWithoutEndpointWhenRegisterDeviceForPushNotificationThenCreateNewEndpointWithNewArn(){

        // setup
        String platformArn = Constants.SNS_APPLICATION_ARN.replace("app", "endpoint") + "/" + UUID.randomUUID();
        String deviceToken = UUID.randomUUID().toString();

        // execute
        String endpointArn = notificationService.registerDeviceForPushNotification(deviceToken, platformArn);

        // verify
        assertFalse(platformArn.equals(endpointArn));
    }

    @Test
    public void givenDeviceEndpointArnWhenPushNotificationThenReturnMessageId(){

        // setup
        Spot spot = new Spot();
        spot.setName("Nazare");
        spot.setId("123");
        String deviceToken = UUID.randomUUID().toString();
        String endpointArn = notificationService.registerDeviceForPushNotification(deviceToken, null);

        // execute
        String messageId = notificationService.swellNotification(Platform.GCM, endpointArn, spot);

        // verify
        assertNotNull(messageId);
    }

    @Test
    public void givenDeviceEndpointWhenPushNotificationThenMessageIdNotNull(){

        //setup
        String deviceToken = "12345";
        String deviceEndpointArn = notificationService.registerDeviceForPushNotification(deviceToken, null);
        Spot spot = createSpot();

        //execute
        String messageId = notificationService.swellNotification(Platform.GCM, deviceEndpointArn, spot);

        // verify
        assertNotNull(messageId);

        // cleanup
        notificationService.deletePlatformEndpoint(deviceEndpointArn);
    }

    @Test
    public void givenTopicEndpointWithSubscriberWhenPushNotificationThenMessageIdNotNull(){

        // setup
        String deviceToken = "12345";
        String deviceEndpointArn = notificationService.registerDeviceForPushNotification(deviceToken, null);
        String topicArn = notificationService.createTopic("test_topic");
        notificationService.subscribeToTopic(topicArn, deviceEndpointArn);
        Spot spot = createSpot();
        spot.setTopicArn(topicArn);

        // execute
        String messageId = notificationService.swellNotification(Platform.GCM, topicArn, spot);

        // verify
        assertNotNull(messageId);

        // cleanup
        notificationService.deletePlatformEndpoint(deviceEndpointArn);
        notificationService.deleteTopic(topicArn);

    }

    /**
     * Helper function to create a spot
     *
     * @return test spot
     */
    private Spot createSpot(){

        Spot spot = new Spot();
        spot.setName("Eisbach");
        spot.setId("ace26bea-4e10-4a16-beb7-efc5f28f167a");
        return spot;
    }

    /**
     * Helper function to create a user
     *
     * @return test  user
     */
    private UserTestModel createUser(){

        UserTestModel user = new UserTestModel();
        user.setUsername(UUID.randomUUID().toString());
        return user;
    }
}
