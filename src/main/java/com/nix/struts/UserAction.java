package com.nix.struts;

import com.nix.model.User;
import com.nix.service.UserService;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.PrincipalAware;
import org.apache.struts2.interceptor.PrincipalProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class UserAction extends ActionSupport implements PrincipalAware {

    private static final Logger log = LoggerFactory.getLogger(UserAction.class);

    private UserService userService;
    private PrincipalProxy principal;
    private User user;

    @Autowired
    public void setUserService(@Qualifier("userService") UserService userService) {
        this.userService = userService;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public void setPrincipalProxy(PrincipalProxy principalProxy) {
        this.principal = principalProxy;
    }

    public String userPageGet() {
        log.debug("invoke userPage action");

        if (principal.getUserPrincipal() != null) {
            user = userService.findByLogin(principal.getUserPrincipal().getName());
            return SUCCESS;
        } else {
            return LOGIN;
        }
    }


}
