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
    userId:${user.id}
</div>
<div>
    username:${user.username}
</div>
<div>
    salt:${user.salt}
</div>
<div>
    hashedPwd:${user.hashedPwd}
</div>
</body>
</html>
