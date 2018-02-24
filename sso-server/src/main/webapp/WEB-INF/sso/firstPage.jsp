<%@ page language="java" contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <%@include file='/common.jsp' %>
</head>
<body>
<div class="top-container clearBorderLR">
	<h1 style="font-size: xx-large;">欢迎您: ${sessionScope.SESSION_USER_REALNAME}</h1>
	<div>SpringBoot  Spring-session Redis</div>
</div>
</body>
</html>

