<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html style="width:100%;height:100%">
<head>
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <%@include file='/common.jsp' %>
    <script type="text/javascript" src="<s:static/>/sso/ug/ugTree.js"></script>
    <script type="text/javascript" src="<s:static/>/sso/ug/ug.js"></script>
</head>
<body style="width:100%;height:100%;" class="clearBorder">

<!-- 用户组按钮 -->
<div id="header" class="text-right">
    <span class="header-left">用户组导航</span>
    <a href="javascript:void(0)" class="easyui-linkbutton white" iconCls="icon-add" onclick="addUg()">新增组</a>
    <a href="javascript:void(0)" class="easyui-linkbutton white" iconCls="icon-edit" onclick="editUg()">编辑组</a>
</div>


<!-- 人员 -->
<div id="header1" style="text-align: right;">
    <span class="header-left">组员列表</span>
</div>
<!-- 角色 -->
<div id="header2" style="text-align: right;">
    <span class="header-left">用户组有效角色列表</span>
</div>

<!-- 用户组树 -->
<div style="width:40%;height:100%;float:left;">
    <div class="easyui-panel" data-options="header:'#header',maximized : true">
        <ul id="ug_tab"></ul>
    </div>
</div>
<%-- 人员列表 --%>
<div style="width:30%;height:100%;float:left;">
    <div class="easyui-panel" data-options="header:'#header1',maximized : true">
	    <div class="easyui-panel" style="height:100%;">
		    <table id="user_tab">
		    </table> 
		</div>
    </div>
</div>
<%-- 角色列表 --%>
<div style="width:30%;height:100%;float:right;">
    <div class="easyui-panel" data-options="header:'#header2',maximized : true">
	    <div class="easyui-panel" style="width:100%;height:100%;">
		    <table id="role_tab" style="width:100%;height:100%;">
		    </table> 
		</div>
    </div>
</div>

<!-- 新增/编辑组表单 -->
<div id="ug_data" class="easyui-dialog" closed="true" cache="false" modal="true" buttons="#dlg-buttons">
    <form id="ugForm" method="post" style="width:400px;height:240px;" class="padding-10" >
          <div id="ug_info">
              <input type="hidden" name="id"/>
              <ul class="li-horizontal">
              	<li>
               	<span>组名称：</span>
                   <div>
                   	<input type="text" name="name" class="form-control-150 easyui-validatebox" data-options="required:true"  style="width: 250px" >
                   </div>
              	</li>
              	<li>
               	<span>编码：</span>
                   <div>
                   	<input type="text" name="code" class="form-control-150 easyui-validatebox" data-options="required:true,validType:['ugCodeRepeat']" style="width: 250px" >
                   </div>
              	</li>
               <li>
               		<span>父用户组：</span>
                   <div class="form-control-inner">
                   	<input id="parentUg" name="parentId" class="easyui-combobox">
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
                <a href="#" class="easyui-linkbutton" iconCls="icon-save" onclick="ugSubmit();">确定</a>
                <a href="#" class="easyui-linkbutton" iconCls="icon-cancel"
                            onclick="javascript:$('#ug_data').dialog('close')">关闭</a>
            </td>
        </tr>
    </table>
</div>

</body>
</html>