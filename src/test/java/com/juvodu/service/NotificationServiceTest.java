package com.juvodu.service;

import com.juvodu.database.model.User;
import com.juvodu.service.model.UserTestModel;
import com.juvodu.util.Constants;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

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
    public void givenNonExistingUserIdAndDeviceTokenWhenRegisterDeviceForPushNotificationThenCreate(){

        // execute
        notificationService.registerDeviceForPushNotification(USERNAME, "123");

        // verify
        User user = userService.getUserById(USERNAME);
        assertNotNull(user.getPlatformEndpointArn());
    }

    @Test
    public void givenNonExistingUserIdAndDeviceTokenWhenRegisterDeviceForPushNotificationThenUpdate(){

        // setup
        User user = createUser();
        userService.save(user);
        String arn = user.getPlatformEndpointArn();

        // execute
        notificationService.registerDeviceForPushNotification(USERNAME, "456");

        // verify
        user = userService.getUserById(USERNAME);
        assertFalse(arn.equalsIgnoreCase(user.getPlatformEndpointArn()));
    }

    @Test
    public void givenEndpointArnWhenPushNotificationThenReturnMessageId(){

        // setup
        User user = createUser();
        userService.save(user);
        notificationService.registerDeviceForPushNotification(USERNAME, "456");

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

        String arn = Constants.SNS_APPLICATION_ARN.replace("app", "endpoint") + "/ec5706bf-5b62-373b-bae0-88f260af3012";
        User user = new UserTestModel();
        user.setId(USERNAME);
        user.setPlatformEndpointArn(arn);
        return user;
    }
}
