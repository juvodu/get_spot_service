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
        String arn = Constants.SNS_APPLICATION_ARN.replace("app", "endpoint") + "/217f658e-e141-3d63-8209-35d76abb5fba";
        User user = new UserTestModel();
        user.setId(USERNAME);
        user.setPlatformEndpointArn(arn);
        userService.save(user);

        // execute
        notificationService.registerDeviceForPushNotification(USERNAME, "456");

        // verify
        user = userService.getUserById(USERNAME);
        assertFalse(arn.equalsIgnoreCase(user.getPlatformEndpointArn()));
    }
}
