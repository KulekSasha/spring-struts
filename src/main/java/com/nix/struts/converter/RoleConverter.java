package com.nix.struts.converter;

import com.nix.model.Role;
import com.nix.service.RoleService;
import com.opensymphony.xwork2.conversion.TypeConversionException;
import org.apache.struts2.util.StrutsTypeConverter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

public class RoleConverter extends StrutsTypeConverter {

    @Autowired
    private RoleService roleService;

    @Override
    public Object convertFromString(Map context, String[] values, Class toClass) {

        if (values == null && values.length == 0) {
            throw new TypeConversionException("can not convert role from string: " +
                    "string[] is null or length is 0");
        }

        if (Role.class.isAssignableFrom(toClass)) {
            return roleService.findByName(values[0]);
        } else {
            throw new TypeConversionException("can not convert role from string.");
        }
    }

    @Override
    public String convertToString(Map context, Object o) {
        if (o instanceof Role) {
            return ((Role) o).getName();
        } else {
            throw new TypeConversionException("can not convert role to string");
        }
    }
}
