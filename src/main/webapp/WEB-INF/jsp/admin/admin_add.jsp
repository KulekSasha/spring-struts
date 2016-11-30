<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ taglib prefix="s" uri="/struts-tags" %>


<!DOCTYPE html>
<html>
<head>
    <c:set var="url">${pageContext.request.contextPath}</c:set>
    <title>Admin add</title>
    <link href="${url}/resources/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            padding-top: 70px;
        }

        li {
            text-align: left;
        }
    </style>
</head>
<body>

<!-- Page Content -->
<div class="container">
    <div class="row">
        <div class="col-lg-4 col-lg-offset-7 text-left">
            <h5><p class="text-center">Admin ${sessionScope.loginUser.firstName}
                (<a href="${url}/logout">logout</a>)</p></h5>
        </div>
    </div>

    <div class="row">
        <div class="col-lg-8 col-lg-offset-2 text-left">
            <h3>Add user</h3> </br>

            <s:form method="POST" class="form-horizontal" action="admin/users/addDo"
                    namespace="/" theme="simple">

                <div class="form-group <s:if test="fieldErrors.containsKey('login')">has-error</s:if>">
                    <label class="control-label col-lg-2" for="login">Login:</label>
                    <div class="col-lg-4">
                        <s:textfield path="login" name="newUser.login" id="login"
                                     class="form-control" placeholder="Login..."/>
                        <s:fielderror fieldName="login" class="control-label"/>
                    </div>
                </div>

                <div class="form-group <s:if test="fieldErrors.containsKey('password')">has-error</s:if>">
                    <div class="form-group ">
                        <label class="control-label col-lg-2" for="password">Password:</label>
                        <div class="col-lg-4">
                            <s:password path="password" name="newUser.password" class="form-control"
                                        showPassword="true" id="password"
                                        placeholder="Enter password"
                            />
                        </div>
                    </div>
                    <div class="form-group ">
                        <label class="control-label col-lg-2 " for="passConfirm">Password
                            again:</label>
                        <div class="col-lg-4">
                            <input type="password" class="form-control" id="passConfirm"
                                   name="passConfirm" placeholder="Enter password"
                                   value="<s:property value="%{newUser.password}"/>"/>
                            <s:fielderror fieldName="password" class="control-label"/>
                        </div>
                    </div>
                </div>


                <div class="form-group <s:if test="fieldErrors.containsKey('email')">has-error</s:if>">
                    <label class="control-label col-lg-2" for="email">Email:</label>
                    <div class="col-lg-4">
                        <s:textfield path="email" name="newUser.email" class="form-control"
                                     placeholder="E-mail..." id="email"/>
                        <s:fielderror fieldName="email" cssClass="control-label"/>
                    </div>
                </div>


                <div class="form-group <s:if test="fieldErrors.containsKey('firstName')">has-error</s:if>">
                    <label class="control-label col-lg-2" for="firstName">First name:</label>
                    <div class="col-lg-4">
                        <s:textfield path="firstName" name="newUser.firstName" class="form-control"
                                     placeholder="Name..." id="firstName"/>
                        <s:fielderror fieldName="firstName" cssClass="control-label"/>
                    </div>
                </div>

                <div class="form-group <s:if test="fieldErrors.containsKey('lastName')">has-error</s:if>">
                    <label class="control-label col-lg-2" for="lastName">Last name:</label>
                    <div class="col-lg-4">
                        <s:textfield path="lastName" class="form-control" name="newUser.lastName"
                                     placeholder="Surname..." id="lastName"/>
                        <s:fielderror fieldName="lastName" cssClass="control-label"/>
                    </div>
                </div>

                <div class="form-group <s:if test="fieldErrors.containsKey('birthday')">has-error</s:if>">
                    <label class="control-label col-lg-2" for="birthday">Birthday:</label>
                    <div class="col-lg-4">
                        <fmt:formatDate pattern="yyyy-MM-dd" value="${newUser.birthday}"
                                        var="dob"/>
                        <input type="date" value="${dob}" name="newUser.birthday" id="birthday"
                               class="form-control"/>
                        <s:fielderror fieldName="birthday" cssClass="control-label"/>
                    </div>
                </div>

                <div class="form-group">
                    <label class="control-label col-lg-2" for="role">Role:</label>
                    <div class="col-lg-4">
                        <s:select path="role" class="list-group-item" id="role" name="newUser.role"
                                  list="#{'admin':'Admin', 'user':'User'}"/>
                    </div>
                </div>

                <div class="form-group">
                    <div class="col-sm-offset-2 col-sm-10">
                        <button type="submit" class="btn btn-default">Ok</button>
                        <button type="reset" class="btn btn-default">Rest</button>
                        <a href="${url}/admin/users" class="btn btn-default"
                           role="button">Cancel</a>
                    </div>
                </div>

            </s:form>

        </div>
    </div>
</div>

<!-- jQuery Version 1.11.1 -->
<script src="${url}/resources/js/jquery.js"></script>

<!-- Bootstrap Core JavaScript -->
<script src="${url}/resources/js/bootstrap.min.js"></script>

</body>
</html>