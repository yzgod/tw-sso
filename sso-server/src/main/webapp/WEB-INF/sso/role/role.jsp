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
    <script type="text/javascript" src="<s:static/>/sso/role/roleTree.js"></script>
    <script type="text/javascript" src="<s:static/>/sso/role/role.js"></script>
</head>
<body style="width:100%;height:100%;" class="clearBorder">


<!-- 角色 -->
<div id="header1" style="text-align:right;">
    <span class="header-left">角色管理</span>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add"
       onclick="addRole()">添加</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit"
       onclick="editRole()">编辑</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove"
       onclick="deleteRole()">删除</a>
</div>

<!-- 菜单分配 -->
<div id="header2" style="text-align:right;">
    <span class="header-left" style="align-text:left">菜单分配</span>
    <a id="saveRoleMenuBtn" href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit"
       onclick="saveRoleMenu()">保存菜单分配</a>
</div>

<!-- 权限分配 -->
<div id="header3" style="text-align:right;">
    <span class="header-left" style="align-text:left">权限分配</span>
    <a id="saveRolePermBtn" href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit"
       onclick="saveRolePerm()">保存权限分配</a>
</div>

<!-- 角色tree -->
<div style="width:40%;height:100%;float:left;">
	<div class="easyui-panel" data-options="header:'#header1',maximized : true">
	<div class="easyui-panel" style="width:100%;height:100%;">
	    <ul id="role_tab"></ul>
	    </div>
	</div>
</div>
<%-- 菜单tree --%>
<div style="width:30%;height:100%;float:left;">
    <div class="easyui-panel" data-options="header:'#header2',maximized : true">
	    <div class="easyui-panel" style="width:100%;height:100%;">
		    <table id="menu_tab">
		    </table> 
		</div>
    </div>
</div>
<%-- 权限tree,带权限组 --%>
<div style="width:30%;height:100%;float:left;">
    <div class="easyui-panel" data-options="header:'#header3',maximized : true">
	    <div class="easyui-panel" style="width:100%;height:100%;">
		    <table id="perm_tab">
		    </table> 
		</div>
    </div>
</div>

<!-- 新增/编辑权限组表单 -->
<div id="role_data" class="easyui-dialog" closed="true" cache="false" modal="true" buttons="#dlg-buttons1">
    <form id="roleForm" method="post" style="width:420px;height:320px;" class="padding-10" >
          <div >
              <input type="hidden" name="id"/>
              <input id="roleAppCode" type="hidden" name="appCode"/>
              <ul class="li-horizontal">
              	<li>
               	<span>应用名称：</span>
                   <div>
                   	<input id="roleAppName" type="text" disabled="disabled" class="form-control-150 easyui-validatebox" style="width: 250px" >
                   </div>
              	</li>
              	<li>
               	<span>角色名称：</span>
                   <div>
                   	<input type="text" name="name" class="form-control-150 easyui-validatebox" data-options="required:true"  style="width: 250px" >
                   </div>
              	</li>
              	<li>
               	<span>角色编码：</span>
                   <div>
                   	<input type="text" name="code" id="roleCode" class="form-control-150 easyui-validatebox" data-options="required:true"  style="width: 250px" >
                   </div>
              	</li>
              	<li>
               	 <span>继承角色：</span>
                   <div class="form-control-inner">
                   	<input id="parentRole" name="parentId" class="easyui-combobox">
                   </div>
              	</li>
               <li>
                 	<span>备注：</span>
                   <div>
                   	<input type="text" class="form-control-150" style="width: 250px" name="remark">
                   </div>
               </li>
              </ul>
          </div>
    </form>
</div>

<!-- 模态框 -->
<div id="dlg-buttons1">
    <table cellpadding="0" cellspacing="0" style="width:100%">
        <tr>
            <td style="text-align:right">
                <a href="#" class="easyui-linkbutton" iconCls="icon-save" onclick="roleSubmit();">提交</a>
                <a href="#" class="easyui-linkbutton" iconCls="icon-cancel"
                            onclick="javascript:$('#role_data').dialog('close')">关闭</a>
            </td>
        </tr>
    </table>
</div>

</body>
</html>

