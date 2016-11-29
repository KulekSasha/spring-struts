package com.nix.struts.interceptor;

import com.nix.service.UserService;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.opensymphony.xwork2.interceptor.ValidationAware;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import java.util.Locale;

public class UniqueLoginInterceptor extends AbstractInterceptor {

    @Autowired
    private UserService userService;

    @Autowired
    private MessageSource messageSource;

    private String loginField;

//    private final Map<String, String> uniqueLoginConfig = new HashMap<String, String>();
//
//
//    public Map<String, String> getUniqueLoginConfig() {
//        return uniqueLoginConfig;
//    }
//
//
//    public void addUniqueLoginConfig(final String configName, final String configValue) {
//        uniqueLoginConfig.put(configName, configValue);
//    }

//    public String getLoginField() {
//        return loginField;
//    }

    public void setLoginField(String loginField) {
        this.loginField = loginField;
    }

    @Override
    public String intercept(ActionInvocation invocation) throws Exception {

        ValidationAware action = (ValidationAware) invocation.getAction();
        String loginFromForm = ServletActionContext.getRequest().getParameter(loginField);

        if (userService.findByLogin(loginFromForm) != null) {
            action.addFieldError(loginField, messageSource.getMessage("non.unique.userLogin", null, Locale.getDefault()));
        }

        return invocation.invoke();
    }
}
