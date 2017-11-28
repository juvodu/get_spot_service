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
        subscription1.setUserId(userId);
        subscription1.setTopicArn(topicArn);
        subscriptionService.save(subscription1);

        SubscriptionTestModel subscription2 = new SubscriptionTestModel();
        subscription2.setUserId(userId);
        subscription2.setTopicArn(topicArn);
        subscriptionService.save(subscription2);
        
        // execute
        List<SubscriptionTestModel> subscriptionResults = subscriptionService.getByUserAndTopic(userId, topicArn, Constants.MAX_USER_DEVICES);

        // verify
        assertNotNull(subscriptionResults);
        assertEquals(2, subscriptionResults.size());
    }
}
