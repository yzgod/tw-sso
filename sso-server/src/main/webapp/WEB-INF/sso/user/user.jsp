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
    <script type="text/javascript" src="<s:static/>/sso/user/user.js"></script>
</head>
<body style="width:100%;height:100%;" class="clearBorder">

<!-- 顶部功能条 -->
<div id="header" style="text-align:right;">
    <span class="header-left" style="align-text:left">用户管理</span>
    <a id="addUser_btn" href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add"
       onclick="addUser()">添加</a>
    <a id="editUser_btn" href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit"
       onclick="editUser()">编辑</a>
    <a id="forbiddenUser_btn" href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove"
       onclick="forbiddenUser()">启用/禁止</a>
       
    <input id="search_user" name="keyword" class="form-control form-control-inline" style="width: 150px" />
</div>


<!-- 用户信息列表 -->
<div style="width:60%;height:100%;float:left;">
	<div class="easyui-panel" data-options="header:'#header',maximized : true">
	<div class="easyui-panel" style="width:100%;height:100%;">
	    <ul id="userinfo_tab"></ul>
	    </div>
	</div>
</div>

<!-- 有效角色 -->
<div id="header1" style="text-align:right;">
    <span class="header-left">有效角色列表</span>
</div>
<%-- 有效角色列表 --%>
<div style="width:40%;height:100%;float:right;">
    <div class="easyui-panel" data-options="header:'#header1',maximized : true">
	    <div class="easyui-panel" style="width:100%;height:100%;">
		    <table id="role_tab">
		    </table> 
		</div>
    </div>
</div>


<!-- 新增/编辑用户表单 -->
<div id="user_data" class="easyui-dialog" closed="true" cache="false" modal="true" buttons="#dlg-buttons">
    <form id="userForm" method="post" style="width:420px;height:420px;" class="padding-10" >
          <div >
              <input type="hidden" name="id"/>
              <ul class="li-horizontal">
              	<li>
               	<span>用户账号：</span>
                   <div>
                   	<input type="text" name="loginName" class="form-control-150 easyui-validatebox" data-options="required:true,validType:['loginNameRepeat']"  style="width: 250px" >
                   </div>
              	</li>
              	<li>
               	<span>用户姓名：</span>
                   <div>
                   	<input type="text" name="realName" class="form-control-150 easyui-validatebox" data-options="required:true" style="width: 250px" >
                   </div>
              	</li>
              	<li>
               	<span>手机号码：</span>
                   <div>
                   	<input type="text" name="cellPhone" class="form-control-150 easyui-validatebox" data-options="required:true,validType:['cellPhoneRepeat']" style="width: 250px" >
                   </div>
              	</li>
              	<li>
               	<span>邮箱地址：</span>
                   <div>
                   	<input type="text" name="email" class="form-control-150 easyui-validatebox" style="width: 250px" >
                   </div>
              	</li>
               <li>
              		<span>主组织：</span>
                   <div class="form-control-inner">
                   	<input id="mainOrg"  name="mainOrg" class="easyui-combobox">
                   </div>
               </li>
               <li>
                	<span>兼职组织：</span>
                   <div class="form-control-inner">
                   	<input id="partTimeOrgs" name="partTimeOrgs" class="easyui-combobox">
                   </div>
               </li>
               <li>
              		<span>主岗位：</span>
                   <div class="form-control-inner">
                   	<input id="mainPosition" class="easyui-combobox" name="mainPosition"/>
                   </div>
               </li>
               <li>
                	<span>兼职岗位：</span>
                   <div class="form-control-inner">
                   	<input id="partTimePositions" name="partTimePositions" class="easyui-combobox">
                   </div>
               </li>
               <li>
              		<span>用户群组：</span>
                   <div class="form-control-inner" >
                   	<input id="userGroups" class="easyui-combobox" name="userGroups"/>
                   </div>
               </li>
               <li>
                 	<span>备注：</span>
                   <div>
                   	<input type="text" id="remark" class="form-control-150" style="width: 250px" name="remark">
                   </div>
               </li>
              </ul>
          </div>
    </form>
</div>

<!-- 模态框 -->
<div id="dlg-buttons">
    <table cellpadding="0" cellspacing="0" style="width:100%">
        <tr>
            <td style="text-align:right">
                <a href="#" class="easyui-linkbutton" iconCls="icon-save" onclick="userSubmit();">提交</a>
                <a href="#" class="easyui-linkbutton" iconCls="icon-cancel"
                            onclick="javascript:$('#user_data').dialog('close')">关闭</a>
            </td>
        </tr>
    </table>
</div>

</body>
</html>

