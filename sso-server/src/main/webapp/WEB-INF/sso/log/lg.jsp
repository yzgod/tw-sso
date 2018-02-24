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
    <script type="text/javascript" src="<s:static/>/sso/log/lg.js"></script>
</head>
<body style="width:100%;height:100%;" class="clearBorder">

<!-- 顶部功能条 -->
<div id="header" style="text-align:right;">
    <span class="header-left" style="align-text:left">登录日志列表</span>
    	用户账号: <input id="loginName" class="easyui-textbox" >
    	起始时间: <input id="startDate" class="easyui-datebox" > - &nbsp;
    	 <input id="endDate" class="easyui-datebox" >
</div>


<!-- 登录日志列表 -->
<div style="width:100%;height:100%;float:left;">
	<div class="easyui-panel" data-options="header:'#header',maximized : true">
	<div class="easyui-panel" style="width:100%;height:100%;">
	    <ul id="lg_tab"></ul>
	    </div>
	</div>
</div>


</body>
</html>

