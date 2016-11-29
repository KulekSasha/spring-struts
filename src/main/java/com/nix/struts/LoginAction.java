package com.nix.struts;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.convention.annotation.*;


@ResultPath("/WEB-INF/jsp")
@Namespace("/")
public class LoginAction extends ActionSupport {

    @Override
    @Actions({
            @Action(value = "login", results = {@Result(name = "success", location = "login.jsp")}),
            @Action(value = "", results = {@Result(name = "success", location = "login.jsp")}),
    })
    public String execute() throws Exception {
        return SUCCESS;
    }
}
