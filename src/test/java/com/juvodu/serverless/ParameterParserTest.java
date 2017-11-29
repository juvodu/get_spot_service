package com.juvodu.serverless;

import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Test Suite for ParameterParser
 *
 * @author Juvodu
 */
public class ParameterParserTest {

    @Test
    public void givenOneParameterWhenGetParametersThenSuccess() throws Exception {

        String testParameter = "{id=123}";
        Map<String, String> parameters = ParameterParser.getParameters(testParameter);
        String id = parameters.get("id");
        assertEquals("123", id);
    }

    @Test
    public void givenMultipleParametersGetParametersThenSuccess() throws Exception {

        String testParameter = "{continent=EU, country=FR}";
        Map<String, String> parameters = ParameterParser.getParameters(testParameter);
        String continent = parameters.get("continent");
        String country = parameters.get("country");
        assertEquals("EU", continent);
        assertEquals("FR", country);
    }
}