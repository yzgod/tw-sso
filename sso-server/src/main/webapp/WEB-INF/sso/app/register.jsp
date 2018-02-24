<%@ page language="java" contentType="text/html; charset=utf-8" %>
<%@taglib uri="http://www.tongwei.com/auth" prefix="sso" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <%@include file='/common.jsp' %>
    <script type="text/javascript" src="<s:static/>/sso/app/register.js"></script>
</head>
<body style="width:100%;height:100%;" class="clearBorder">

<!-- 顶部功能条 -->
<div id="header" style="text-align:right;">
    <span class="header-left" style="align-text:left">应用注册</span>
    <sso:hasRoles value="system_sso">
	    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add"
	       onclick="addApp()">添加</a>
    </sso:hasRoles>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit"
       onclick="editApp()">编辑</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove"
       onclick="deleteApp()">删除</a>
</div>


<!-- 应用列表 -->
<div style="width:100%;height:100%;float:left;">
	<div class="easyui-panel" data-options="header:'#header',maximized : true">
	<div class="easyui-panel" style="width:100%;height:100%;">
	    <ul id="app_tab"></ul>
	    </div>
	</div>
</div>


<!-- 新增/编辑应用表单 -->
<div id="app_data" class="easyui-dialog" closed="true" cache="false" modal="true" buttons="#dlg-buttons">
    <form id="appForm" method="post" style="width:420px;height:280px;" class="padding-10" >
          <div>
              <input type="hidden" name="id"/>
              <ul class="li-horizontal">
              	<li>
               	<span>应用名称：</span>
                   <div>
                   	<input type="text" name="name" class="form-control-150 easyui-validatebox" data-options="required:true"  style="width: 250px" >
                   </div>
              	</li>
              	<li>
               	<span>应用编码：</span>
                   <div>
                   	<input id="appCode" type="text" name="appCode" class="form-control-150 easyui-validatebox" data-options="required:true" style="width: 250px" >
                   </div>
              	</li>
              	<li>
               	<span>负责人：</span>
                   <div>
                   	<input type="text" name="author" class="form-control-150 easyui-validatebox" data-options="required:true" style="width: 250px" >
                   </div>
              	</li>
              	<!-- <li>
               	<span>报警邮箱：</span>
                   <div>
                   	<input type="text" name="alertEmail" class="form-control-150 easyui-validatebox" style="width: 250px" >
                   </div>
              	</li> -->
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
                <a href="#" class="easyui-linkbutton" iconCls="icon-save" onclick="appSubmit();">提交</a>
                <a href="#" class="easyui-linkbutton" iconCls="icon-cancel"
                            onclick="javascript:$('#app_data').dialog('close')">关闭</a>
            </td>
        </tr>
    </table>
</div>

</body>
</html>

