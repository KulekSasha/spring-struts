package com.nix.config;

import org.apache.struts2.dispatcher.filter.StrutsPrepareAndExecuteFilter;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import java.util.EnumSet;

public class SecurityWebAppInitializer extends AbstractSecurityWebApplicationInitializer {

    @Override
    protected void afterSpringSecurityFilterChain(ServletContext servletContext) {
        FilterRegistration.Dynamic filter = servletContext.addFilter(
                "StrutsDispatcher", new StrutsPrepareAndExecuteFilter());
        filter.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, "/*");

    }
}
