package com.juvodu.util;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Generic helper for JSON handling
 *
 * @author Juvodu
 */
public class JsonHelper {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String jsonify(Object message) {
        try {
            return objectMapper.writeValueAsString(message);
        } catch (Exception e) {
            e.printStackTrace();
            throw (RuntimeException) e;
        }
    }
}
