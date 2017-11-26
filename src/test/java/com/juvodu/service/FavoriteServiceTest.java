package com.juvodu.service;

import com.juvodu.service.testmodel.FavoriteTestModel;
import com.juvodu.service.testmodel.SpotTestModel;
import com.juvodu.service.testmodel.UserTestModel;
import org.junit.Before;
import org.junit.BeforeClass;

/**
 * Test suite for the FavoriteService
 *
 * @author Juvodu
 */
public class FavoriteServiceTest {

    // instantiate with test model to ensure persisting all data to the test table "favorite_test"
    private static FavoriteService<FavoriteTestModel> favoriteService;
    private static UserService<UserTestModel> userService;
    private static SpotService<SpotTestModel> spotService;

    @BeforeClass
    public static void beforeClass(){

        favoriteService = new FavoriteService(FavoriteTestModel.class);
        userService = new UserService(UserTestModel.class);
        spotService = new SpotService(SpotTestModel.class);
    }

    @Before
    public void before(){

        favoriteService.deleteAll();
    }
}
