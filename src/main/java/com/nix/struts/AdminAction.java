package com.nix.struts;

import com.nix.model.User;
import com.nix.service.RoleService;
import com.nix.service.UserService;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.interceptor.PrincipalAware;
import org.apache.struts2.interceptor.PrincipalProxy;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.servlet.http.HttpServletRequest;

@Namespace("/admin")
public class AdminAction extends ActionSupport implements PrincipalAware, ServletRequestAware {

    private static final Logger log = LoggerFactory.getLogger(AdminAction.class);

    private UserService userService;
    private RoleService roleService;
    private PrincipalProxy principal;
    private HttpServletRequest request;
    private String userLogin;


    @Autowired
    public void setUserService(@Qualifier("userService") UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setRoleService(@Qualifier("roleService") RoleService roleService) {
        this.roleService = roleService;
    }

    @Override
    public void setPrincipalProxy(PrincipalProxy principalProxy) {
        principal = principalProxy;
    }

    @Override
    public void setServletRequest(HttpServletRequest request) {
        this.request = request;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    @Action(value = "users",
            results = {@Result(name = "success", location = "/WEB-INF/jsp/admin/admin.jsp")})
    public String adminPageGet() {
        log.debug("invoke adminPage action");

        if (principal.getUserPrincipal() != null) {
            User loginUser = userService.findByLogin(principal.getUserPrincipal().getName());
            ActionContext.getContext().getSession().put("loginUser", loginUser);
            return SUCCESS;
        } else {
            return LOGIN;
        }
    }

    @Action(value = "/admin/users/{*}/delete",
            results = {@Result(name = "success", location = "/WEB-INF/jsp/admin/admin.jsp")})
    public String adminDeleteUserPost() {
        log.debug("delete user with userLogin: {}", userLogin);
        log.debug("delete user with userLogin: {}", request.getRequestURL().toString());
        return SUCCESS;
    }


}
