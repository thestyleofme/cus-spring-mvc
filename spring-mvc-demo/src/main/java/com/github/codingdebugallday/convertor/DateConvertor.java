package com.github.codingdebugallday.convertor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.core.convert.converter.Converter;

/**
 * <p>
 * description
 * </p>
 *
 * @author isaac 2020/9/12 16:49
 * @since 1.0.0
 */
public class DateConvertor implements Converter<String, Date> {

    @Override
    public Date convert(String str) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return simpleDateFormat.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
