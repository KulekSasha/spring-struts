package com.nix.struts.converter;

import com.opensymphony.xwork2.conversion.TypeConversionException;
import org.apache.struts2.util.StrutsTypeConverter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;

public class DateConverter extends StrutsTypeConverter {

    @Override
    public Object convertFromString(Map context, String[] values, Class toClass) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);
        try {
            return sdf.parse(values[0]);
        } catch (ParseException e) {
            throw new TypeConversionException("can not convert date from string: " + values[0]);
        }
    }

    @Override
    public String convertToString(Map context, Object o) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(o);
    }
}