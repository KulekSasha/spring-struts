package com.nix.struts;

import com.opensymphony.xwork2.ActionSupport;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.ResultPath;


@Action("login")
@ResultPath("/WEB-INF/jsp")
@Result(name = "success", location = "login.jsp")
public class ViewLoginAction extends ActionSupport {

}
