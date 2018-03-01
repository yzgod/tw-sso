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
    <script type="text/javascript" src="<s:static/>/sso/position/base_position.js"></script>
</head>
<body style="width:100%;height:100%;" class="clearBorder">

<!-- 顶部功能条 -->
<div id="header" style="text-align:right;">
    <span class="header-left" style="align-text:left">基础岗位列表</span>
    <a href="javascript:void(0)" class="easyui-linkbutton white" iconCls="icon-add" onclick="addPosition()">添加</a>
    <a href="javascript:void(0)" class="easyui-linkbutton white" iconCls="icon-edit" onclick="editPosition()">编辑</a>
    
    <input id="search_position" name="keyword" class="form-control form-control-inline" style="width: 150px" />
</div>


<!-- 基础岗位列表 -->
<div style="width:100%;height:100%;float:left;">
	<div class="easyui-panel" data-options="header:'#header',maximized : true">
	<div class="easyui-panel" style="width:100%;height:100%;">
	    <ul id="position_tab"></ul>
	    </div>
	</div>
</div>

<!-- 新增/编辑基础岗位表单 -->
<div id="position_data" class="easyui-dialog" closed="true" cache="false" modal="true" buttons="#dlg-buttons">
    <form id="positionForm" method="post" style="width:420px;height:250px;" class="padding-10" >
        <div>
            <input type="hidden" name="id"/>
            <ul class="li-horizontal">
              	<li>
               	<span>岗位名称：</span>
                   <div>
                   	<input type="text" name="name" class="form-control-150 easyui-validatebox" data-options="required:true"  style="width: 250px" >
                   </div>
              	</li>
              	<li>
               	<span>岗位编码：</span>
                   <div>
                   	<input type="text" name="code" class="form-control-150 easyui-validatebox" data-options="required:true"  style="width: 250px" >
                   </div>
              	</li>
              	<li>
               	<span>备注：</span>
                   <div>
                   	<input type="text" name="remark" class="form-control-150 easyui-validatebox" data-options="required:false"  style="width: 250px" >
                   </div>
              	</li>
            </ul>
        </div>
    </form>
</div>
<div id="dlg-buttons">
    <table cellpadding="0" cellspacing="0" style="width:100%">
        <tr>
            <td style="text-align:right">
                <a href="#" class="easyui-linkbutton" iconCls="icon-save" onclick="$('#positionForm').form('submit')">提交</a>
                <a href="#" class="easyui-linkbutton" iconCls="icon-cancel"
                            onclick="javascript:$('#position_data').dialog('close')">关闭</a>
            </td>
        </tr>
    </table>
</div>

</body>
</html>

