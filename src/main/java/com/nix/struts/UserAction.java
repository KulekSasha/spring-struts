package com.nix.struts;

import com.nix.model.User;
import com.nix.service.UserService;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.interceptor.PrincipalAware;
import org.apache.struts2.interceptor.PrincipalProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@Namespace("/")
public class UserAction extends ActionSupport implements PrincipalAware {

    private static final Logger log = LoggerFactory.getLogger(UserAction.class);

    private UserService userService;
    private PrincipalProxy principal;
    private User user;

    @Autowired
    public void setUserService(@Qualifier("userService") UserService userService) {
        this.userService = userService;
    }

    @Action(value = "user/user",
            results = {@Result(name = "success", location = "/WEB-INF/jsp/user/user.jsp"),
                    @Result(name = "login", location = "/WEB-INF/jsp/login.jsp")}
    )
    public String userPageGet() {
        log.debug("invoke userPage action");

        if (principal.getUserPrincipal() != null) {
            user = userService.findByLogin(principal.getUserPrincipal().getName());
            return SUCCESS;
        } else {
            return LOGIN;
        }
    }


    @Override
    public void setPrincipalProxy(PrincipalProxy principalProxy) {
        this.principal = principalProxy;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
