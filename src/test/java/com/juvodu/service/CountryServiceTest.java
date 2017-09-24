package com.juvodu.service;

import com.juvodu.database.model.Country;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Test suite for CountryService
 *
 * @author Juvodu
 */
public class CountryServiceTest {

    private static CountryService countryService;

    @BeforeClass
    public static void beforeClass(){

        countryService = new CountryService();
    }

    @Test
    public void whenGetAllCountriesThenSuccess() throws Exception {

        List<Country> countries = countryService.getAllCountries();
        assertFalse(countries.isEmpty());
    }

}