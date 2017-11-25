package com.juvodu.service;

import com.juvodu.database.model.Platform;
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
    private static String USERNAME = "juvodu";


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
    public void givenEndpointArnWhenPushNotificationThenReturnMessageId(){

        // setup
        String deviceToken = UUID.randomUUID().toString();
        String endpointArn = notificationService.registerDeviceForPushNotification(deviceToken, null);

        // execute
        String messageId = notificationService.pushNotification(Platform.GCM, endpointArn, "unit-subject", "unit-message");

        // verify
        assertNotNull(messageId);
    }

    @Test
    public void test(){
        String messageId = notificationService.pushNotification(Platform.GCM,"arn:aws:sns:eu-central-1:980738030415:endpoint/GCM/LetMeGoAndroid/217f658e-e141-3d63-8209-35d76abb5fba", "unit-subject", "unit-message");
    }

    /**
     * Helper function to create a user
     *
     * @return test  user
     */
    private UserTestModel createUser(){

        UserTestModel user = new UserTestModel();
        user.setUserName(USERNAME);
        return user;
    }
}
