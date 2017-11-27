package com.juvodu.database.converter;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * Custom date converter, which convert dates to sum of total milliseconds since January 1, 1970
 *
 * @author Juvodu
 */
public class DateConverter implements DynamoDBTypeConverter<String, Date> {

    @Override
    public String convert(Date date) {

        return Long.toString(date.getTime());
    }

    @Override
    public Date unconvert(String dateString) {

        Date date = null;
        try {
            if (StringUtils.isNotBlank(dateString)) {
                date = new Date(Long.valueOf(dateString));
            }
        }catch (Exception e) {
            e.printStackTrace();
        }

        return date;
    }
}
