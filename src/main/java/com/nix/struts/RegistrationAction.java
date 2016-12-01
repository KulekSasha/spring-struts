package com.nix.struts;

import com.nix.model.User;
import com.nix.service.UserService;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.conversion.annotations.Conversion;
import com.opensymphony.xwork2.conversion.annotations.TypeConversion;
import net.tanesha.recaptcha.ReCaptchaImpl;
import net.tanesha.recaptcha.ReCaptchaResponse;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;

import javax.servlet.http.HttpServletRequest;


@Conversion(
        conversions = {
                @TypeConversion(key = "newUser.role",
                        converter = "com.nix.struts.converter.RoleConverter"),
                @TypeConversion(key = "newUser.birthday",
                        converter = "com.nix.struts.converter.DateConverter")
        })
public class RegistrationAction extends ActionSupport implements ServletRequestAware {

    public static final Logger log = LoggerFactory.getLogger(RegistrationAction.class);

    private User newUser;
    private ReCaptchaImpl reCaptcha;
    private UserService userService;
    private HttpServletRequest request;
    private String recaptcha_challenge_field;
    private String recaptcha_response_field;
    Environment env;

    public User getNewUser() {
        return newUser;
    }

    public void setNewUser(User newUser) {
        this.newUser = newUser;
    }

    @Autowired
    public void setReCaptcha(@Qualifier("reCaptcha") ReCaptchaImpl reCaptcha) {
        this.reCaptcha = reCaptcha;
    }

    @Autowired
    public void setUserService(@Qualifier("userService") UserService userService) {
        this.userService = userService;
    }

    @Override
    public void setServletRequest(HttpServletRequest request) {
        this.request = request;
    }

    public String getRecaptcha_challenge_field() {
        return recaptcha_challenge_field;
    }

    public void setRecaptcha_challenge_field(String recaptcha_challenge_field) {
        this.recaptcha_challenge_field = recaptcha_challenge_field;
    }

    public String getRecaptcha_response_field() {
        return recaptcha_response_field;
    }

    public void setRecaptcha_response_field(String recaptcha_response_field) {
        this.recaptcha_response_field = recaptcha_response_field;
    }

    @Autowired
    public void setEnv(Environment env) {
        this.env = env;
    }

    public Environment getEnv() {
        return env;
    }

    public String registrationPageGet() {
        newUser = new User();
        return SUCCESS;
    }

    public String registrationPagePost() {

        ReCaptchaResponse reCaptchaResponse =
                reCaptcha.checkAnswer(request.getRemoteAddr(),
                        recaptcha_challenge_field, recaptcha_response_field);

        if (!reCaptchaResponse.isValid()) {
            ActionContext.getContext().getValueStack().set("invalidRecaptcha", true);
        }

        if (getFieldErrors().size() > 0 || !reCaptchaResponse.isValid()) {
            log.debug("some errors during registration");
            return INPUT;
        }

        userService.create(newUser);
        return SUCCESS;
    }


}
