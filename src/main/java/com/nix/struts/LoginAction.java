package com.nix.struts;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.ResultPath;


@ResultPath("/WEB-INF/jsp")
public class LoginAction extends ActionSupport {

    @Override
    @Action(value = "login",
            results = {@Result(name = "success", location = "login.jsp")}
    )
    public String execute() throws Exception {
        return SUCCESS;
    }
}
