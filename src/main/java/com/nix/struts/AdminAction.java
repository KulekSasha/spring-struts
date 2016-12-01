package com.nix.struts;

import com.nix.model.User;
import com.nix.service.UserService;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.conversion.annotations.Conversion;
import com.opensymphony.xwork2.conversion.annotations.TypeConversion;
import org.apache.struts2.interceptor.PrincipalAware;
import org.apache.struts2.interceptor.PrincipalProxy;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.servlet.http.HttpServletRequest;


@Conversion(
        conversions = {
                @TypeConversion(key = "newUser.role",
                        converter = "com.nix.struts.converter.RoleConverter"),
                @TypeConversion(key = "newUser.birthday",
                        converter = "com.nix.struts.converter.DateConverter"),
                @TypeConversion(key = "editableUser.role",
                        converter = "com.nix.struts.converter.RoleConverter"),
                @TypeConversion(key = "editableUser.birthday",
                        converter = "com.nix.struts.converter.DateConverter")
        })
public class AdminAction extends ActionSupport implements PrincipalAware, ServletRequestAware {

    private static final Logger log = LoggerFactory.getLogger(AdminAction.class);

    private UserService userService;
    private PrincipalProxy principal;
    private HttpServletRequest request;
    private User newUser;
    private User editableUser;

    @Autowired
    public void setUserService(@Qualifier("userService") UserService userService) {
        this.userService = userService;
    }

    @Override
    public void setPrincipalProxy(PrincipalProxy principalProxy) {
        principal = principalProxy;
    }

    @Override
    public void setServletRequest(HttpServletRequest request) {
        this.request = request;
    }

    public User getNewUser() {
        return newUser;
    }

    public void setNewUser(User newUser) {
        this.newUser = newUser;
    }

    public User getEditableUser() {
        return editableUser;
    }

    public void setEditableUser(User editableUser) {
        this.editableUser = editableUser;
    }

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

    public String adminAddUserGet() {
        log.debug("show form for new user creation");
        newUser = new User();
        return "success";
    }

    public String adminAddUserPost() {
        log.debug("add new user from form: {}", newUser);

        if (getFieldErrors().size() > 0) {
            return INPUT;
        }

        log.debug("save new user: {}", newUser);
        userService.create(newUser);
        return SUCCESS;
    }

    public String adminEditUserGet() {
        log.debug("show form for user edit; login - {}", editableUser);

        editableUser = userService.findByLogin(editableUser.getLogin());

        return (editableUser != null && editableUser.getId() > 0)
                ? SUCCESS
                : INPUT;
    }

    public String adminEditUserPost() {
        log.debug("editable user from user from: {}", editableUser);

        if (getFieldErrors().size() > 0) {
            return INPUT;
        }

        log.debug("update user: {}", editableUser);
        userService.update(editableUser);
        return SUCCESS;
    }

    public String adminDeleteUserPost() {
        String userLogin = request.getParameter("userLogin");
        log.debug("delete user with userLogin: {}; method: {}", userLogin, request.getMethod());

        User user = new User();
        user.setLogin(userLogin);
        userService.remove(user);

        return SUCCESS;
    }

}
