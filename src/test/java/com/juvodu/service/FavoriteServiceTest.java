package com.juvodu.service;

import com.juvodu.service.testmodel.FavoriteTestModel;
import com.juvodu.service.testmodel.SpotTestModel;
import com.juvodu.service.testmodel.UserTestModel;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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
    public void givenUserWhenGetFavoritesByUserThenReturnFavorites(){

        // setup
        UserTestModel user = new UserTestModel();
        userService.save(user);
        FavoriteTestModel favorite = new FavoriteTestModel();
        favorite.setUserId(user.getId());
        favorite.setSpotId("spot");
        favoriteService.save(favorite);

        // execute
        List<FavoriteTestModel> favorites = favoriteService.getFavoritesByUser(user.getId(), 100);

        // verify
        assertNotNull(favorites);
        assertEquals(1, favorites.size());
        assertEquals(user.getId(), favorites.get(0).getUserId());
    }

    @Test
    public void givenUserAndSpotWhenIsSpotUserFavoriteThenReturnTrue(){

        // setup
        UserTestModel user = new UserTestModel();
        userService.save(user);
        FavoriteTestModel favorite = new FavoriteTestModel();
        favorite.setUserId(user.getId());
        favorite.setSpotId("spot");
        favoriteService.save(favorite);

        // execute
        boolean favoriteResult = favoriteService.isSpotUserFavorite(user.getId(), "spot");

        // verify
        assertTrue(favoriteResult);
    }

    @Test
    public void givenUserAndSpotWhenIsSpotUserFavoriteThenReturnFalse(){

        // setup
        UserTestModel user = new UserTestModel();
        userService.save(user);
        FavoriteTestModel favorite = new FavoriteTestModel();
        favorite.setUserId(user.getId());
        favorite.setSpotId("spot2");
        favoriteService.save(favorite);

        // execute
        boolean favoriteResult = favoriteService.isSpotUserFavorite(user.getId(), "spot");

        // verify
        assertFalse(favoriteResult);
    }
}
