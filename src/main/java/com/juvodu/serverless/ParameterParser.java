package com.juvodu.serverless;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;

/**
 * ParameterParser used to parse URI parameters
 *
 * @author Juvodu
 */
public class ParameterParser {

    /**
     * Parse queries of request
     *
     * @param parametersStr
     *          the parameter string to be parsed
     * @return map of parameters
     * @throws UnsupportedEncodingException
     */
    public static Map<String, String> getParameters(String parametersStr) throws UnsupportedEncodingException {

        //remove curly braces if parameters are wrapped as an object
        parametersStr = parametersStr.replace(" ", "");
        parametersStr = parametersStr.replace("{", "");
        parametersStr = parametersStr.replace("}", "");

        Map<String, String> query_pairs = new LinkedHashMap<>();

        // api gateway converts get parameters {?continent=EU&country=FR} to JSON {continent=EU,country=FR}
        String[] pairs = parametersStr.split(",");
        for(String pair :pairs){
            int idx = pair.indexOf("=");
            query_pairs.put(
                    URLDecoder.decode(pair.substring(0, idx), "UTF-8"),
                    URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
        }
        return query_pairs;
    }
}
