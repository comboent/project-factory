<%@ page import="org.apache.shiro.SecurityUtils" %><%--
  Created by IntelliJ IDEA.
  User: combo
  Date: 2017/1/16
  Time: 15:42
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>reg</title>
</head>
<body>
<div>
    <h1>isAuthc : <%=SecurityUtils.getSubject().isAuthenticated()%></h1>
    <h1>principals: <%=SecurityUtils.getSubject().getPrincipals()%></h1>
    <h1>remembered: <%=SecurityUtils.getSubject().isRemembered()%></h1>
</div>
</body>
</html>
