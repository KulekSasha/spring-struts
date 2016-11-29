package com.nix.struts;

import com.nix.model.User;
import com.nix.service.RoleService;
import com.nix.service.UserService;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.conversion.annotations.Conversion;
import com.opensymphony.xwork2.conversion.annotations.TypeConversion;
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
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validator;
import java.util.Set;

@Namespace("/")
@Conversion(
        conversions = {
                @TypeConversion(key = "newUser.role", converter = "com.nix.struts.converter.RoleConverter"),
                @TypeConversion(key = "role", converter = "com.nix.struts.converter.RoleConverter")
        }
)
public class AdminAction extends ActionSupport implements PrincipalAware, ServletRequestAware, MessageSourceAware {

    private static final Logger log = LoggerFactory.getLogger(AdminAction.class);
    private static final String GET = "GET";
    private static final String POST = "POST";


    private UserService userService;
    private RoleService roleService;
    private PrincipalProxy principal;
    private HttpServletRequest request;
    private String userLogin;
    @Valid
    private User newUser;

    private String passConfirm;

    private MessageSource messageSource;

    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }


    @Autowired
    private Validator validator;

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

    public User getNewUser() {
        return newUser;
    }

    public void setNewUser(User newUser) {
        this.newUser = newUser;
    }

    public String getPassConfirm() {
        return passConfirm;
    }

    public void setPassConfirm(String passConfirm) {
        this.passConfirm = passConfirm;
    }

    @Action(value = "admin/users",
            results = {@Result(name = "success", location = "/WEB-INF/jsp/admin/admin.jsp"),
                    @Result(name = "login", location = "/WEB-INF/jsp/login.jsp")})
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

    @Action(value = "admin/users/{userLogin}/delete",
            results = {@Result(name = "success", location = "/WEB-INF/jsp/admin/admin.jsp")})
    public String adminDeleteUserPost() {
        log.debug("delete user with userLogin: {}; method: {}", userLogin, request.getMethod());

        if (POST.equals(request.getMethod())) {
            User user = new User();
            user.setLogin(userLogin);
            userService.remove(user);
        }

        return SUCCESS;
    }

    public String adminAddUser() {
        if (GET.equals(request.getMethod())) {
            log.debug("get new user form: {}; method: {}", userLogin, request.getMethod());
            newUser = new User();
            return "success";
        }
        return "notAllowed";
    }

    public String adminAddUserDo() {

        if (!passConfirm.equals(newUser.getPassword())) {
            addFieldError("password", "passwords not equal");
        }

//        if (userService.findByLogin(newUser.getLogin()) != null) {
//            addFieldError("login", messageSource.getMessage("non.unique.userLogin", null, Locale.getDefault()));
//        }

        Set<ConstraintViolation<User>> violations = validator.validate(newUser);

        if (violations.size() > 0) {
            violations.forEach(violation -> addFieldError(
                    violation.getPropertyPath().toString(),
                    violation.getMessage()));
        }

        if (getFieldErrors().size() > 0) {
            log.debug("errors: {}", getFieldErrors());
            return "input";
        }

        log.debug("save new user: {}", newUser);
        userService.create(newUser);
        return "success";
    }

}
