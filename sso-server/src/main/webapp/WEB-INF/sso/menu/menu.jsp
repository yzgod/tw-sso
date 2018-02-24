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
    <script type="text/javascript" src="<s:static/>/sso/menu/menuTree.js"></script>
    <script type="text/javascript" src="<s:static/>/sso/menu/menu.js"></script>
</head>
<body style="width:100%;height:100%;" class="clearBorder">

<!-- 菜单按钮 -->
<div id="header" style="text-align:right;">
    <span class="header-left" style="align-text:left">菜单管理</span>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add"
       onclick="addMenu()">添加</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit"
       onclick="editMenu()">编辑</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove"
       onclick="deleteMenu()">删除</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-reload"
       onclick="giveRoles()">授予角色</a>
</div>

<!-- 应用 -->
<div id="header1" style="text-align:right;">
    <span class="header-left">应用列表</span>
</div>

<!-- 角色 -->
<div id="header2" style="text-align:right;">
    <span class="header-left">菜单所属直接角色列表</span>
     <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove"
       onclick="cancelRole()">取消角色</a>
</div>

<!-- 应用列表 -->
<div style="width:25%;height:100%;float:left;">
	<div class="easyui-panel" data-options="header:'#header1',maximized : true">
	<div class="easyui-panel" style="width:100%;height:100%;">
	    <ul id="app_tab"></ul>
	    </div>
	</div>
</div>
<%-- 菜单tree --%>
<div style="width:50%;height:100%;float:left;">
    <div class="easyui-panel" data-options="header:'#header',maximized : true">
	    <div class="easyui-panel" style="width:100%;height:100%;">
		    <table id="menu_tab">
		    </table> 
		</div>
    </div>
</div>
<!-- 菜单所属直接角色列表 -->
<div style="width:25%;height:100%;float:right;">
	<div class="easyui-panel" data-options="header:'#header2',maximized : true">
	<div class="easyui-panel" style="width:100%;height:100%;">
	    <ul id="role_tab"></ul>
	    </div>
	</div>
</div>


<!-- 新增/编辑菜单表单 -->
<div id="menu_data" class="easyui-dialog" closed="true" cache="false" modal="true" buttons="#dlg-buttons">
    <form id="menuForm" method="post" style="width:420px;height:320px;" class="padding-10" >
          <div id="menu_info" >
              <input type="hidden" name="id"/>
              <input type="hidden" name="appCode"/>
              <ul class="li-horizontal">
              	<li>
               	<span>应用名称：</span>
                   <div>
                   	<input id="appName" type="text" disabled="disabled" class="form-control-150 easyui-validatebox" style="width: 250px" >
                   </div>
              	</li>
              	<li>
               	<span>菜单名称：</span>
                   <div>
                   	<input type="text" name="name" class="form-control-150 easyui-validatebox" data-options="required:true"  style="width: 250px" >
                   </div>
              	</li>
              	<li>
               	<span>url地址：</span>
                   <div>
                   	<input type="text" name="url" class="form-control-150" style="width: 250px" >
                   </div>
              	</li>
              	<li>
               	<span>匹配规则：</span>
                   <div>
                   	<input type="text" name="pattern" class="form-control-150" style="width: 250px" >
                   </div>
              	</li>
              	<li>
               	 <span>父菜单：</span>
                   <div class="form-control-inner">
                   	<input id="parentMenu" name="parentId" class="easyui-combobox">
                   </div>
              	</li>
              	<li>
               		<span>图标样式：</span>
                   <div>
                   	<input type="text" name="icon" class="form-control-150" style="width: 250px" >
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
                <a href="#" class="easyui-linkbutton" iconCls="icon-save" onclick="menuSubmit();">提交</a>
                <a href="#" class="easyui-linkbutton" iconCls="icon-cancel"
                            onclick="javascript:$('#menu_data').dialog('close')">关闭</a>
            </td>
        </tr>
    </table>
</div>

<!-- 授予角色表单 -->
<div id="role_data" class="easyui-dialog" closed="true" cache="false" modal="true" buttons="#dlg-buttons2">
    <form id="roleForm" method="post" style="width:420px;height:220px;" class="padding-10" >
          <div>
              <input type="hidden" name="menuId"/>
              <ul class="li-horizontal">
              	<li>
               	<span>菜单名称：</span>
                   <div>
                   	<input type="text" name="menuName" class="form-control-150" disabled="true"  style="width: 250px" >
                   </div>
              	</li>
              	<li>
               	 <span>子级包含：</span>
                   <div class="form-control-inner">
                   	<select name="isInclude" class="easyui-combobox">
                   		<option selected="selected" value="0">不包含子级菜单</option>
   						<option value="1">包含子级菜单</option>
                   	</select>
                   </div>
              	</li>
              	<li>
               	 <span>应用角色：</span>
                   <div class="form-control-inner">
                   	<input id="roleIds" class="easyui-combobox"  style="width: 250px;"/>
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

