package com.nix.struts.interceptor;

import com.nix.service.UserService;
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

public class UniqueLoginInterceptor extends AbstractInterceptor {

    private static final Logger log = LoggerFactory.getLogger(UniqueLoginInterceptor.class);

    @Autowired
    @Qualifier(value = "userService")
    private UserService userService;

    @Autowired
    @Qualifier(value = "messageSource")
    private MessageSource messageSource;

    private String loginAttrName;
    private String beanParamName;

    public void setLoginAttrName(String loginAttrName) {
        this.loginAttrName = loginAttrName;
    }

    public void setBeanParamName(String beanParamName) {
        this.beanParamName = beanParamName;
    }

    @Override
    public String intercept(ActionInvocation invocation) throws Exception {

        log.debug("interceptor param: loginAttrName - [{}]", loginAttrName);

        ValidationAware action = (ValidationAware) invocation.getAction();
        String loginFromForm = ServletActionContext.getRequest().getParameter(loginAttrName);

        if (userService.findByLogin(loginFromForm) != null) {
            action.addFieldError(beanParamName, messageSource.getMessage("non.unique.userLogin", null, Locale.getDefault()));
        }

        return invocation.invoke();
    }
}
