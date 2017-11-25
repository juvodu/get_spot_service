package com.juvodu.service;

import com.juvodu.database.model.User;
import com.juvodu.service.testmodel.UserTestModel;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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

    @Test
    public void givenSpotWhenSaveThenSuccess(){

        //execute
        userService.save(createUser());
    }

    @Test
    public void givenSavedUserWhenDeleteThenSuccess(){

        //setup
        UserTestModel user = createUser();
        userService.save(user);

        //execute
        userService.delete(user);
    }

    @Test
    public void givenSavedUserWhenGetUserByIdThenReturnUser(){

        //setup
        UserTestModel user = createUser();
        userService.save(user);
        String id = user.getId();

                //execute
        User userResult = userService.getByHashKey(id);

        //verify
        assertNotNull(userResult);
        assertEquals(id, userResult.getId());
        assertEquals(user.getPlatformEndpointArn(), userResult.getPlatformEndpointArn());
    }

    @Test
    public void given2SavedUsersWhenGetAllUsersThenReturnBoth(){

        //setup
        UserTestModel user1 = createUser();
        UserTestModel user2 = createUser();
        userService.save(user1);
        userService.save(user2);

        //execute
        List<UserTestModel> allUsers = userService.findAll();

        //verify
        assertNotNull(allUsers);
        assertEquals(2, allUsers.size());
    }

    @Test
    public void givenExistingSpotWhenUpdateThenSuccess(){

        //setup
        UserTestModel user = createUser();
        userService.save(user);
        user.setPlatformEndpointArn("updated-platform-endpoint");

        //execute
        userService.save(user);

        //verify
        User userResult = userService.getByHashKey(user.getId());
        assertEquals("updated-platform-endpoint", userResult.getPlatformEndpointArn());
    }

    /**
     * Helper function to create a user
     *
     * @return the created instance of the user
     */
    private UserTestModel createUser(){

        UserTestModel user = new UserTestModel();
        user.setPlatformEndpointArn("platform-endpoint");
        return user;
    }
}
