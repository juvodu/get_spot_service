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
    private static UserService userService;

    @BeforeClass
    public static void beforeClass(){

        userService = new UserService(UserTestModel.class);
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
        User user = createUser();
        userService.save(user);

        //execute
        userService.delete(user);
    }

    @Test
    public void givenSavedUserWhenGetUserByIdThenReturnUser(){

        //setup
        User user = createUser();
        String id = userService.save(user);

        //execute
        User userResult = userService.getUserById(id);

        //verify
        assertNotNull(userResult);
        assertEquals(id, userResult.getId());
        assertEquals(user.getPlatformEndpointArn(), userResult.getPlatformEndpointArn());
    }

    @Test
    public void given2SavedUsersWhenGetAllUsersThenReturnBoth(){

        //setup
        User user1 = createUser();
        User user2 = createUser();
        userService.save(user1);
        userService.save(user2);

        //execute
        List<User> allUsers = userService.findAll();

        //verify
        assertNotNull(allUsers);
        assertEquals(2, allUsers.size());
    }

    @Test
    public void givenExistingSpotWhenUpdateThenSuccess(){

        //setup
        User user = createUser();
        userService.save(user);
        user.setPlatformEndpointArn("updated-platform-endpoint");

        //execute
        userService.save(user);

        //verify
        User userResult = userService.getUserById(user.getId());
        assertEquals("updated-platform-endpoint", userResult.getPlatformEndpointArn());
    }

    /**
     * Helper function to create a user
     *
     * @return the created instance of the user
     */
    private User createUser(){

        User user = new UserTestModel();
        user.setPlatformEndpointArn("platform-endpoint");
        return user;
    }
}
