package com.juvodu.service;

import com.juvodu.service.testmodel.UserTestModel;
import org.junit.Before;
import org.junit.BeforeClass;

/**
 * Test suite for the UserService
 *
 * @author Juvodu
 */
public class UserServiceTest {

    // instantiate with test model to ensure persisting all data to the test table "user_test"
    private static UserService<UserTestModel> userService;

    @BeforeClass
    public static void beforeClass(){

        userService = new UserService<>(UserTestModel.class);
    }

    @Before
    public void before(){

        userService.deleteAll();
    }
}
