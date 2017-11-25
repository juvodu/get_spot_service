package com.juvodu.service;

import com.juvodu.database.model.Platform;
import com.juvodu.database.model.User;
import com.juvodu.service.testmodel.UserTestModel;
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
    private static UserService<UserTestModel> userService;
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
        User user = userService.getById(USERNAME);
        assertNotNull(user.getPlatformEndpointArn());
    }

    @Test
    public void givenArnWithoutEndpointWhenRegisterDeviceForPushNotificationThenCreateNewEndpointWithNewArn(){

        // setup
        String arn = Constants.SNS_APPLICATION_ARN.replace("app", "endpoint") + "/" + UUID.randomUUID();
        String deviceToken = UUID.randomUUID().toString();
        UserTestModel user = createUser();
        user.setPlatformEndpointArn(arn);
        userService.save(user);

        // execute
        notificationService.registerDeviceForPushNotification(USERNAME, deviceToken);

        // verify
        user = userService.getById(USERNAME);
        assertFalse(arn.equals(user.getPlatformEndpointArn()));
    }

    @Test
    public void givenEndpointArnWhenPushNotificationThenReturnMessageId(){

        // setup
        String deviceToken = UUID.randomUUID().toString();
        UserTestModel user = createUser();
        userService.save(user);
        String userId = user.getId();
        notificationService.registerDeviceForPushNotification(USERNAME, deviceToken);
        user = userService.getById(userId);

        // execute
        String messageId = notificationService.pushNotification(Platform.GCM, user.getPlatformEndpointArn(), "unit-subject", "unit-message");

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
        user.setId(USERNAME);
        return user;
    }
}
