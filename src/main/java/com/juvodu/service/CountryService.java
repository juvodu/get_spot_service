package com.juvodu.service;

import com.juvodu.database.model.Country;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

/**
 * Service for country retrieval
 *
 * @author Juvodu
 */
public class CountryService {

    /**
     * Get list of countries sorted by display name
     *
     * @return list of countries
     */
    public List<Country> getAllCountries(){

        String[] locales = Locale.getISOCountries();
        List<Country> countries = new ArrayList<>();
        for (String countryCode : locales) {

            Locale locale = new Locale("", countryCode);
            countries.add(new Country(locale.getCountry(), locale.getDisplayName()));

        }

        // sort countries alphabetical by country name
        countries.sort(Comparator.comparing(Country::getName));
        return countries;
    }
}
