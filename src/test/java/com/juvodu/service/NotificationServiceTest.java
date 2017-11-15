package com.juvodu.service;

import com.juvodu.database.model.User;
import com.juvodu.service.model.UserTestModel;
import com.juvodu.util.Constants;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.*;

/**
 * Test suite for the NotificationService
 *
 * @author Juvodu
 */
public class NotificationServiceTest {

    private static NotificationService notificationService;
    private static UserService userService;
    private static String USERNAME = "juvodu";


    @BeforeClass
    public static void beforeClass(){
        userService = new UserService(UserTestModel.class);
        notificationService = new NotificationService(userService);
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
        notificationService.registerDeviceForPushNotification(USERNAME, deviceToken);

        // verify
        User user = userService.getUserById(USERNAME);
        assertNotNull(user.getPlatformEndpointArn());
    }

    @Test
    public void givenArnWithoutEndpointWhenRegisterDeviceForPushNotificationThenCreateNewEndpointWithNewArn(){

        // setup
        String arn = Constants.SNS_APPLICATION_ARN.replace("app", "endpoint") + "/" + UUID.randomUUID();
        String deviceToken = UUID.randomUUID().toString();
        User user = createUser();
        user.setPlatformEndpointArn(arn);
        userService.save(user);

        // execute
        notificationService.registerDeviceForPushNotification(USERNAME, deviceToken);

        // verify
        user = userService.getUserById(USERNAME);
        assertFalse(arn.equals(user.getPlatformEndpointArn()));
    }

    @Test
    public void givenEndpointArnWhenPushNotificationThenReturnMessageId(){

        // setup
        String deviceToken = UUID.randomUUID().toString();
        User user = createUser();
        String userId = userService.save(user);
        notificationService.registerDeviceForPushNotification(USERNAME, deviceToken);
        user = userService.getUserById(userId);

        // execute
        String messageId = notificationService.pushNotification(user.getPlatformEndpointArn(), "unit-subject", "unit-message");

        // verify
        assertNotNull(messageId);
    }

    /**
     * Helper function to create a user
     *
     * @return test  user
     */
    private User createUser(){

        User user = new UserTestModel();
        user.setId(USERNAME);
        return user;
    }
}
