package com.juvodu.service;

import com.juvodu.service.testmodel.SubscriptionTestModel;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

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
        SubscriptionTestModel subscription = new SubscriptionTestModel();
        subscription.setUserId("user");
        subscription.setTopicArn("topic");
        subscriptionService.save(subscription);
        
        // execute
        SubscriptionTestModel subscriptionResult = subscriptionService.getByUserAndTopic(subscription.getUserId(), subscription.getTopicArn());

        // verify
        assertNotNull(subscriptionResult);
        assertEquals("user", subscriptionResult.getUserId());
        assertEquals("topic", subscriptionResult.getTopicArn());
    }
}
