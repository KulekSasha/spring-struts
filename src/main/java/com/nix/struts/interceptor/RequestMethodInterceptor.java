package com.nix.struts.interceptor;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.opensymphony.xwork2.util.TextParseUtil;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

public class RequestMethodInterceptor extends AbstractInterceptor {

    private static final Logger log = LoggerFactory.getLogger(RequestMethodInterceptor.class);

    private String allowedMethods;
    private String resultName = "notAllowed";

    public void setAllowedMethods(String allowedMethods) {
        this.allowedMethods = allowedMethods;
    }

    public void setResultName(String resultName) {
        this.resultName = resultName;
    }

    @Override
    public String intercept(ActionInvocation invocation) throws Exception {

        log.debug("interceptor prop: allowedMethods: [{}]; resultName: [{}]",
                allowedMethods, resultName);

        Set<String> allowed = TextParseUtil.commaDelimitedStringToSet(allowedMethods);
        String currentMethod = ServletActionContext.getRequest().getMethod();

        log.debug("request method: {}", currentMethod);

        if (allowed.stream().anyMatch(currentMethod::equalsIgnoreCase)) {
            return invocation.invoke();
        } else {
            return resultName;
        }
    }
}
