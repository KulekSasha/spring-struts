package com.nix.struts.interceptor;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.opensymphony.xwork2.interceptor.ValidationAware;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;

import java.util.Locale;

public class PasswordConfirmInterceptor extends AbstractInterceptor {

    private static final Logger log = LoggerFactory.getLogger(PasswordConfirmInterceptor.class);

    @Autowired
    @Qualifier(value = "messageSource")
    private MessageSource messageSource;

    private String passwordAttrName;
    private String passwordConfirmAttrName;
    private String beanFieldName;

    public void setPasswordAttrName(String passwordAttrName) {
        this.passwordAttrName = passwordAttrName;
    }

    public void setPasswordConfirmAttrName(String passwordConfirmAttrName) {
        this.passwordConfirmAttrName = passwordConfirmAttrName;
    }

    public void setBeanFieldName(String beanFieldName) {
        this.beanFieldName = beanFieldName;
    }

    @Override
    public String intercept(ActionInvocation invocation) throws Exception {

        log.debug("interceptor prop: passwordAttrName: [{}]; passwordConfirmAttrName: [{}]",
                passwordAttrName, passwordConfirmAttrName);

        ValidationAware action = (ValidationAware) invocation.getAction();
        String passwordFromForm = ServletActionContext.getRequest()
                .getParameter(passwordAttrName);
        String passwordConfirmFromForm = ServletActionContext.getRequest()
                .getParameter(passwordConfirmAttrName);

        log.debug("passwords from form: password: [{}]; passwordConfirm: [{}]",
                passwordFromForm, passwordConfirmFromForm);

        if (!passwordFromForm.equals(passwordConfirmFromForm)) {
            log.debug("passwords not equal, set field error");
            action.addFieldError(beanFieldName, messageSource.getMessage("notEqual.password", null, Locale.getDefault()));
        }

        return invocation.invoke();
    }
}
