package com.juvodu.service;

import com.juvodu.database.model.Continent;
import com.juvodu.database.model.Country;

import java.util.*;

/**
 * Service for country retrieval
 *
 * @author Juvodu
 */
public class CountryService {

    private final Map<String, List<String>> continentCountryMap = new HashMap<>(250);

    public CountryService(){

        // africa
        continentCountryMap.put(Continent.AF.getCode(), Arrays.asList("AO", "BF", "BI", "BJ", "BW", "CD", "CF", "CG",
                "CI", "CM", "CV", "DJ", "DZ", "EG", "EH", "ER", "ET", "GA", "GH", "GM", "GN", "GQ", "GW", "KE", "KM",
                "LR", "LS", "LY", "MA", "MG", "ML", "MR", "MU", "MW", "MZ", "NA", "NE", "NG", "RE", "RW", "SC", "SD",
                "SH", "SL", "SN", "SO", "ST", "SZ", "TD", "TG", "TN", "TZ", "UG", "YT", "ZA", "ZM", "ZW"));

        // north america
        continentCountryMap.put(Continent.NA.getCode(), Arrays.asList("AG", "AI", "AN", "AW", "BB", "BL", "BM", "BS",
                "BZ", "CA", "CR", "CU", "DM", "DO", "GD", "GL", "GP", "GT", "HN", "HT", "JM", "KN", "KY", "LC", "MF",
                "MQ", "MS", "MX", "NI", "PA", "PM", "PR", "SV", "TC", "TT", "US", "VC", "VG", "VI"));

        // oceania
        continentCountryMap.put(Continent.OC.getCode(), Arrays.asList("AS", "AU", "CK", "FJ", "FM", "GU", "KI", "MH",
                "MP", "NC", "NF", "NR", "NU", "NZ", "PF", "PG", "PN", "PW", "SB", "TK", "TO", "TV", "UM", "VU", "WF", "WS"));

        // antarctica
        continentCountryMap.put(Continent.AN.getCode(), Arrays.asList("AQ", "BV", "GS", "HM", "TF"));

        // asia
        continentCountryMap.put(Continent.AS.getCode(), Arrays.asList("AE", "AF", "AM", "AZ", "BD", "BH", "BN",
                "BT", "CC", "CN", "CX", "CY", "GE", "HK", "ID", "IL", "IN", "IO", "IQ", "IR", "JO", "JP", "KG", "KH",
                "KP", "KR", "KW", "KZ", "LA", "LB", "LK", "MM", "MN", "MO", "MV", "MY", "NP", "OM", "PH", "PK", "PS",
                "QA", "SA", "SG", "SY", "TH", "TJ", "TL", "TM", "TW", "UZ", "VN", "YE"));

        // europe
        continentCountryMap.put(Continent.EU.getCode(), Arrays.asList("AD", "AL", "AT", "AX", "BA", "BE", "BG", "BY",
                "CH", "CZ", "DE", "DK", "EE", "ES", "FI", "FO", "FR", "GB", "GG", "GI", "GR", "HR", "HU",
                "IE", "IM", "IS", "IT", "JE", "LI", "LT", "LU", "LV", "MC", "MD", "ME", "MK", "MT", "NL", "NO", "PL",
                "PT", "RO", "RS", "RU", "SE", "SI", "SJ", "SK", "SM", "TR", "UA", "VA"));

        // south america
        continentCountryMap.put(Continent.SA.getCode(), Arrays.asList("AR", "BO", "BR", "CL", "CO", "EC", "FK", "GF",
                "GY", "PE", "PY", "SR", "UY", "VE"));
    }

    /**
     * Get list of countries for a given continent
     * @param continentCode
     *              the continent to filter by
     *
     * @return list of countries
     */
    public List<Country> getCountryByContinent(String continentCode){

        List<String> countryCodes = continentCountryMap.get(continentCode);
        List<Country> countries = new ArrayList<>();
        for (String countryCode : countryCodes) {

            Locale locale = new Locale("", countryCode);
            countries.add(new Country(locale.getCountry(), locale.getDisplayName()));

        }

        // sort countries alphabetical by country name
        countries.sort(Comparator.comparing(Country::getName));

        return countries;
    }
}
