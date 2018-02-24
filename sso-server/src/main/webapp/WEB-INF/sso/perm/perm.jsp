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
    <script type="text/javascript" src="<s:static/>/sso/perm/pgTree.js"></script>
    <script type="text/javascript" src="<s:static/>/sso/perm/perm.js"></script>
</head>
<body style="width:100%;height:100%;" class="clearBorder">

<!-- 权限组 -->
<div id="header1" style="text-align:right;">
    <span class="header-left">权限组管理</span>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add"
       onclick="addPg()">添加</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit"
       onclick="editPg()">编辑</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove"
       onclick="deletePg()">删除</a>
</div>

<!-- 权限按钮 -->
<div id="header" style="text-align:right;">
    <span class="header-left" style="align-text:left">权限管理</span>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add"
       onclick="addPerm()">添加</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit"
       onclick="editPerm()">编辑</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove"
       onclick="deletePerm()">删除</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-reload"
       onclick="giveRoles()">授予角色</a>
</div>

<!-- 角色 -->
<div id="header2" style="text-align:right;">
    <span class="header-left">权限所属直接角色列表</span>
     <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove"
       onclick="cancelRole()">取消角色</a>
</div>

<!-- 权限组列表 -->
<div style="width:30%;height:100%;float:left;">
	<div class="easyui-panel" data-options="header:'#header1',maximized : true">
	<div class="easyui-panel" style="width:100%;height:100%;">
	    <ul id="pg_tab"></ul>
	    </div>
	</div>
</div>
<%-- 权限tree,带权限组 --%>
<div style="width:45%;height:100%;float:left;">
    <div class="easyui-panel" data-options="header:'#header',maximized : true">
	    <div class="easyui-panel" style="width:100%;height:100%;">
		    <table id="perm_tab">
		    </table> 
		</div>
    </div>
</div>
<!-- 权限所属直接角色列表 -->
<div style="width:25%;height:100%;float:right;">
	<div class="easyui-panel" data-options="header:'#header2',maximized : true">
	<div class="easyui-panel" style="width:100%;height:100%;">
	    <ul id="role_tab"></ul>
	    </div>
	</div>
</div>


<!-- 新增/编辑权限组表单 -->
<div id="pg_data" class="easyui-dialog" closed="true" cache="false" modal="true" buttons="#dlg-buttons1">
    <form id="pgForm" method="post" style="width:420px;height:320px;" class="padding-10" >
          <div id="pg_info" >
              <input type="hidden" name="id"/>
              <input id="pgAppCode" type="hidden" name="appCode"/>
              <ul class="li-horizontal">
              	<li>
               	<span>应用名称：</span>
                   <div>
                   	<input id="pgAppName" type="text" disabled="disabled" class="form-control-150 easyui-validatebox" style="width: 250px" >
                   </div>
              	</li>
              	<li>
               	<span>组名称：</span>
                   <div>
                   	<input type="text" name="name" class="form-control-150 easyui-validatebox" data-options="required:true"  style="width: 250px" >
                   </div>
              	</li>
              	<li>
               	<span>组编码：</span>
                   <div>
                   	<input type="text" name="code" id="pgCode" class="form-control-150 easyui-validatebox" data-options="required:true"  style="width: 250px" >
                   </div>
              	</li>
              	<li>
               	 <span>父权限组：</span>
                   <div class="form-control-inner">
                   	<input id="parentPg" name="parentId" class="easyui-combobox">
                   </div>
              	</li>
              	<li>
                 	<span>排序：</span>
                   <div>
                   	<input type="text" class="form-control-150 easyui-numberbox" style="width: 275px" name="ord">
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
                <a href="#" class="easyui-linkbutton" iconCls="icon-save" onclick="pgSubmit();">提交</a>
                <a href="#" class="easyui-linkbutton" iconCls="icon-cancel"
                            onclick="javascript:$('#pg_data').dialog('close')">关闭</a>
            </td>
        </tr>
    </table>
</div>
<!-- 新增/编辑权限表单 -->
<div id="perm_data" class="easyui-dialog" closed="true" cache="false" modal="true" buttons="#dlg-buttons">
    <form id="permForm" method="post" style="width:420px;height:320px;" class="padding-10" >
          <div id="perm_info" >
              <input type="hidden" name="id"/>
              <input id="permAppCode" type="hidden" name="appCode"/>
              <ul class="li-horizontal">
              	<li>
               	<span>应用名称：</span>
                   <div>
                   	<input id="permAppName" type="text" disabled="disabled" class="form-control-150 easyui-validatebox" style="width: 250px" >
                   </div>
              	</li>
              	<li>
               	<span>权限名称：</span>
                   <div>
                   	<input type="text" name="name" class="form-control-150 easyui-validatebox" data-options="required:true"  style="width: 250px" >
                   </div>
              	</li>
              	<li>
               	<span>权限编码：</span>
                   <div>
                   	<input type="text" id="permCode" name="code" class="form-control-150 easyui-validatebox" data-options="required:true"  style="width: 250px" >
                   </div>
              	</li>
              	<li>
               	 <span>权限组：</span>
                   <div class="form-control-inner">
                   	<input id="permGroup" name="groupId" class="easyui-combobox" data-options="required:true" >
                   </div>
              	</li>
              	<li>
                 	<span>排序：</span>
                   <div>
                   	<input type="text" id="ord" class="form-control-150 easyui-numberbox" style="width: 275px" name="ord">
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
                <a href="#" class="easyui-linkbutton" iconCls="icon-save" onclick="permSubmit();">提交</a>
                <a href="#" class="easyui-linkbutton" iconCls="icon-cancel"
                            onclick="javascript:$('#perm_data').dialog('close')">关闭</a>
            </td>
        </tr>
    </table>
</div>

<!-- 权限授予角色表单 -->
<div id="role_data" class="easyui-dialog" closed="true" cache="false" modal="true" buttons="#dlg-buttons2">
    <form id="roleForm" method="post" style="width:420px;height:220px;" class="padding-10" >
          <div>
              <ul class="li-horizontal">
              	<li>
               	<span>权限组：</span>
                   <div>
                   	<input type="text" name="pgName" class="form-control-150" disabled="true"  style="width: 250px" >
                   </div>
              	</li>
              	<li>
               	 <span>权限选择：</span>
                   <div class="form-control-inner">
                   	<input id="permIds" class="easyui-combobox easyui-validatebox" data-options="required:true"  style="width: 250px;"/>
                   	<input type="hidden" name="permIds" />
                   </div>
              	</li>
              	<li>
               	 <span>应用角色：</span>
                   <div class="form-control-inner">
                   	<input id="roleIds" class="easyui-combobox easyui-validatebox" data-options="required:true"  style="width: 250px;"/>
                   	<input type="hidden" name="roleIds" />
                   </div>
              	</li>
              </ul>
          </div>
    </form>
</div>

<!-- 模态框 -->
<div id="dlg-buttons2">
    <table cellpadding="0" cellspacing="0" style="width:100%">
        <tr>
            <td style="text-align:right">
                <a href="#" class="easyui-linkbutton" iconCls="icon-save" onclick="giveRolesSubmit();">提交</a>
                <a href="#" class="easyui-linkbutton" iconCls="icon-cancel"
                            onclick="javascript:$('#role_data').dialog('close')">关闭</a>
            </td>
        </tr>
    </table>
</div>

</body>
</html>

