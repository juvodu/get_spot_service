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

    private final DateFormat dateFormat = new SimpleDateFormat(Constants.yyyyMMdd);

    @Override
    public String convert(Date date) {

        return dateFormat.format(date);
    }

    @Override
    public Date unconvert(String dateString) {

        Date date = null;
        try {
            if (StringUtils.isNotBlank(dateString)) {
                date = dateFormat.parse(dateString);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }

        return date;
    }
}
