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
    <script type="text/javascript" src="<s:static/>/sso/role/ug_role.js"></script>
</head>
<body style="width:100%;height:100%;" class="clearBorder">

<!-- 顶部功能条 -->
<div id="header" style="text-align:right;">
    <span class="header-left" style="align-text:left">用户组架构树</span>
</div>

<!-- 用户组树 -->
<div style="width:60%;height:100%;float:left;">
	<div class="easyui-panel" data-options="header:'#header',maximized : true">
	<div class="easyui-panel" style="width:100%;height:100%;">
	    <ul id="ug_tab"></ul>
	    </div>
	</div>
</div>

<!-- 角色 -->
<div id="header1" style="text-align:right;">
    <span class="header-left">角色树</span>
    <a id="saveUgRoleBtn" href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit"
       onclick="saveUgRole()">保存角色分配</a>
</div>
<%-- 角色树 --%>
<div style="width:40%;height:100%;float:right;">
    <div class="easyui-panel" data-options="header:'#header1',maximized : true">
	    <div class="easyui-panel" style="width:100%;height:100%;">
		    <table id="role_tab">
		    </table> 
		</div>
    </div>
</div>
</body>
</html>

