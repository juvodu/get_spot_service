package com.juvodu.util;

/**
 * Generic constants are stored here
 *
 * @author Juvodu
 */
public class Constants {

    /** dynamo db indexes used for querying */
    public static final String SNS_APPLICATION_ARN = "arn:aws:sns:eu-central-1:980738030415:app/GCM/LetMeGoAndroid"; // SNS application arn for push notifications
    public static final String CONTINENT_COUNTRY_INDEX = "continent-country-index";
    public static final String CONTINENT_GEOHASH_INDEX = "continent-geohash-index";
    public static final String CONTINENT_CRONDATE_INDEX ="continent-crondate-index";
    public static final String USER_TOPIC_INDEX ="user-topic-index";

    /** number of max subscriptions per user - devices per user to receive push notifications */
    public static final int MAX_SUBSCRIPTIONS_USER = 3;
}
