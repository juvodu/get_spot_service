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
    public void getOneParametersTest() throws Exception {

        String testParameter = "{id=123}";
        Map<String, String> parameters = ParameterParser.getParameters(testParameter);
        String id = parameters.get("id");
        assertEquals("123", id);
    }

    @Test
    public void getMultipleParameters() throws Exception {

        String testParameter = "{name=AWS&desc=cloud}";
        Map<String, String> parameters = ParameterParser.getParameters(testParameter);
        String name = parameters.get("name");
        String desc = parameters.get("desc");
        assertEquals("AWS", name);
        assertEquals("cloud", desc);
    }

}