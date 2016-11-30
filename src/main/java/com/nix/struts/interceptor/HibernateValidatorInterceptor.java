package com.nix.struts.interceptor;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.opensymphony.xwork2.interceptor.ValidationAware;
import com.opensymphony.xwork2.util.TextParseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Set;

public class HibernateValidatorInterceptor extends AbstractInterceptor {

    private static final Logger log = LoggerFactory.getLogger(HibernateValidatorInterceptor.class);

    @Autowired
    @Qualifier(value = "validator")
    private Validator validator;

    @Autowired
    @Qualifier(value = "messageSource")
    private MessageSource messageSource;

    private Set<String> fieldsToValidate;
    private String actionFieldsToValidate;

    public void setActionFieldsToValidate(String actionFieldsToValidate) {
        this.actionFieldsToValidate = actionFieldsToValidate;
    }

    @Override
    public String intercept(ActionInvocation invocation) throws Exception {

        if (actionFieldsToValidate == null
                || actionFieldsToValidate.length() == 0) {
            return invocation.invoke();
        }

        fieldsToValidate = TextParseUtil.commaDelimitedStringToSet(actionFieldsToValidate);

        if (fieldsToValidate.size() == 0) {
            return invocation.invoke();
        }

        log.debug("start validate; field for validation: {}", fieldsToValidate);

        ValidationAware action = (ValidationAware) invocation.getAction();

        validate(action);

        return invocation.invoke();
    }

    private void validate(ValidationAware action) throws Exception {
        Iterator<String> iterator = fieldsToValidate.iterator();

        while (iterator.hasNext()) {
            String fieldName = iterator.next();
            Field declaredField = action.getClass().getDeclaredField(fieldName);
            declaredField.setAccessible(true);
            Object o = declaredField.get(action);

            Set<ConstraintViolation<Object>> violations = validator.validate(o);

            if (violations.size() > 0) {
                violations.forEach(violation -> action.addFieldError(
                        violation.getPropertyPath().toString(),
                        violation.getMessage()));
            }
        }

    }

}
