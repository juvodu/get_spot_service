package com.juvodu.service;

import com.juvodu.service.testmodel.SubscriptionTestModel;
import com.juvodu.util.Constants;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test suite for the SubscriptionService
 *
 * @author Juvodu
 */
public class SubscriptionServiceTest {

    // instantiate with test model to ensure persisting all data to the test table "subscription_test"
    private static SubscriptionService<SubscriptionTestModel> subscriptionService;

    @BeforeClass
    public static void beforeClass(){

        subscriptionService = new SubscriptionService(SubscriptionTestModel.class);
    }

    @Before
    public void before(){

        subscriptionService.deleteAll();
    }

    @Test
    public void givenSubscriptionWhenGetByUserAndTopicThenReturnSubscription(){

        // setup
        String userId = "123";
        String topicArn = "topic";
        SubscriptionTestModel subscription1 = new SubscriptionTestModel();
        subscription1.setUsername(userId);
        subscription1.setTopicArn(topicArn);
        subscriptionService.save(subscription1);

        SubscriptionTestModel subscription2 = new SubscriptionTestModel();
        subscription2.setUsername(userId);
        subscription2.setTopicArn(topicArn);
        subscriptionService.save(subscription2);
        
        // execute
        List<SubscriptionTestModel> subscriptionResults = subscriptionService.getByUserAndTopic(userId, topicArn, 100);

        // verify
        assertNotNull(subscriptionResults);
        assertEquals(2, subscriptionResults.size());
    }

    @Test
    public void givenSubscriptionWhenGetByUserAndPlatformEndpointArnThenReturnSubscription(){

        // setup
        String userId = "123";
        String endpointArn = "456";
        SubscriptionTestModel subscription = new SubscriptionTestModel();
        subscription.setUsername(userId);
        subscription.setEndpointArn(endpointArn);
        subscriptionService.save(subscription);

        // execute
        List<SubscriptionTestModel> subscriptionResults = subscriptionService.getByUserAndPlatformEndpointArn(userId, endpointArn, 100);

        // verify
        assertNotNull(subscriptionResults);
        assertEquals(1, subscriptionResults.size());
        SubscriptionTestModel result = subscriptionResults.get(0);
        assertEquals(userId, result.getUsername());
        assertEquals(endpointArn, result.getEndpointArn());
    }
}
