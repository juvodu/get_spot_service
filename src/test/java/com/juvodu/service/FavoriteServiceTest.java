package com.juvodu.service;

import com.juvodu.service.testmodel.FavoriteTestModel;
import org.junit.Before;
import org.junit.BeforeClass;

/**
 * TODO: description
 *
 * @author Juvodu
 */
public class FavoriteServiceTest {

    // instantiate with test model to ensure persisting all data to the test table "favorite_test"
    private static FavoriteService favoriteService;

    @BeforeClass
    public static void beforeClass(){

        favoriteService = new FavoriteService(FavoriteTestModel.class);
    }

    @Before
    public void before(){

        //favoriteService.deleteAll();
    }
}
