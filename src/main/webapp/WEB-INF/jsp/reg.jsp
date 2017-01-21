<%--
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
    <form action="/jsp/reg" method="post">
        username:<input name="username"><br/>
        pwd:<input name="pwd"><br/>
        <input type="submit" value="commit">
    </form>
    <br/>
</div>
<div>
    <form action="/jsp/login" method="post">
        username:<input name="username"><br/>
        pwd:<input name="pwd"><br/>
        rememberMe:<input name="rememberMe"><br/>
        <input type="submit" value="commit">
    </form>
</div>
</body>
</html>
