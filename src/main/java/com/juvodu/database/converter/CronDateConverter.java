package com.juvodu.database.converter;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.juvodu.database.model.Country;
import com.juvodu.util.Constants;
import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Custom date converter, which convert dates to the format yyyy-MM-dd
 *
 * @author Juvodu
 */
public class CronDateConverter implements DynamoDBTypeConverter<String, Date> {

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
