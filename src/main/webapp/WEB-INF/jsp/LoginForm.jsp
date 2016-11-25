<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<%@ taglib prefix="s" uri="/struts-tags" %>


<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Spring and Struts Integration Demo</title>
</head>
<body>
<div align="center">
    <h1>Spring and Struts Integration Demo</h1>
    <h2>Users Login</h2>
    <s:form action="doLogin" method="post">
        <s:textfield label="Username" name="user.username" />
        <s:password label="Password" name="user.password" />
        <s:submit value="Login" />
    </s:form>
</div>
</body>
</html>
