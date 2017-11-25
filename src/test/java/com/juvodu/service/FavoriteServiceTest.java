package com.juvodu.service;

import com.juvodu.service.testmodel.FavoriteTestModel;
import com.juvodu.service.testmodel.SpotTestModel;
import com.juvodu.service.testmodel.UserTestModel;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

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

    @Test
    public void givenFavoriteWhenGetByCompositeKeyThenReturnFavorite(){

        // setup
        UserTestModel user = new UserTestModel();
        userService.save(user);
        SpotTestModel spot = new SpotTestModel();
        spotService.save(spot);
        FavoriteTestModel favorite = new FavoriteTestModel();
        favorite.setUserId(user.getId());
        favorite.setSpotId(spot.getId());
        favoriteService.save(favorite);

        // execute
        FavoriteTestModel fav = favoriteService.getByCompositeKey(favorite.getUserId(), favorite.getSpotId());

        // verify
        assertEquals(user.getId(), fav.getUserId());
        assertEquals(spot.getId(), fav.getSpotId());
    }
}
