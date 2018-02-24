<%@ page language="java" contentType="text/html; charset=utf-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>单点登录权限认证系统</title>
	<%@include file='/common.jsp' %>
	<link rel="shortcut icon" type="image/x-icon" href="<s:static/>/favicon.ico"/>
    <script type="text/javascript" src="<s:static/>/sso/main/main.js"></script>
    <style>
       #passwordForm {margin-top:35px;}
       #passwordForm div{margin-top:5px;margin-left:35px;}
    </style>
</head>

<body>

<div class="container">
    <div id="pf-hd">
        <div class="pf-logo">
            <img src="<s:static/>/public/images/main/main_logo.png" alt="logo">
        </div>

        <div class="pf-nav-wrap">
            <div class="pf-nav-ww">
                <ul class="pf-nav" id="top-level-container">
                </ul>
            </div>
            <a href="javascript:;" class="pf-nav-prev disabled iconfont">&#xe606;</a>
            <a href="javascript:;" class="pf-nav-next iconfont">&#xe607;</a>
        </div>

        <div class="pf-user">
            <h4 class="pf-user-name ellipsis">${sessionScope.SESSION_USER_REALNAME}</h4>
            <i class="iconfont xiala">&#xe607;</i>
            <div class="pf-user-panel">
                <ul class="pf-user-opt">
                    <li class="pf-modify-pwd">
                        <a href="javascript:;">
                            <i class="iconfont">&#xe634;</i>
                            <span class="pf-opt-name">修改密码</span>
                        </a>
                    </li>
                    <li class="pf-logout">
                        <a>
                            <i class="iconfont">&#xe60e;</i>
                            <span class="pf-opt-name">注销登录</span>
                        </a>
                    </li>
                </ul>
            </div>
        </div>
        <div style="position: relative;float: right;height: 70px;line-height: 70px;color:white">
            欢迎您！
        </div>
    </div>

    <div id="pf-bd">
        <div id="pf-sider"></div>
        <div id="pf-page">
            <div id="tabs1" class="easyui-tabs1" style="width:100%;height:100%;"></div>
        </div>
    </div>

    <div id="pf-ft">
        <div class="system-name">
            <i class="iconfont">&#xe6fe;</i>
            &nbsp;&nbsp;
            <span>在线人数:&nbsp;${requestScope.onlineUsers} 人，</span>
            <span>在线会话:&nbsp;${requestScope.onlineSessions}</span>
        </div>
        <div class="copyright-name">
            <span>单点登录权限认证系统&nbsp;v0.1</span>
            <i class="iconfont">&#xe6ff;</i>
        </div>
    </div>
</div>

<div id="cg_password" class="easyui-dialog" style="width:360px;height: 300px;" closed="true" cache="false" modal="true" buttons="#dlg-button">  
	<form id="passwordForm" method="post">
		<ul class="li-horizontal">
		<li>
        	<span>用户名：</span>
            <div>
            	<input type="text" value="${sessionScope.SESSION_USER_LOGINNAME}" class="form-control-150" disabled="disabled"  style="width: 250px" >
            </div>
       	</li>
		<li>
        	<span>原密码：</span>
            <div>
            	<input type="password" name="oldPwd" class="form-control-150 easyui-validatebox" style="width: 250px" data-options="required:true" >
            </div>
       	</li>
		<li>
        	<span>新密码：</span>
            <div>
            	<input type="password" id="newPwd" name="newPwd" class="form-control-150 easyui-validatebox" style="width: 250px" data-options="required:true" >
            </div>
       	</li>
		<li>
        	<span>确认密码：</span>
            <div>
            	<input type="password" id='rePwd' class="form-control-150 easyui-validatebox" style="width: 250px" data-options="required:true" validType="eqPwd['#newPwd']" >
            </div>
       	</li>
	    
	    </ul>  
	</form>  
</div>
<!--模态框按钮 -->
<div id="dlg-button">
	<table cellpadding="0" cellspacing="0" style="width:100%">
		<tr>
		   <td style="text-align:right">
			   <a href="#" class="easyui-linkbutton" iconCls="icon-save" onclick="changePassword();">确定</a>
			   <a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#cg_password').dialog('close')">关闭</a>
			</td>
		</tr>
	</table>
</div>

<script type="text/javascript">
    //切换sidebar引导箭头方向
	$(function () {
		$('#pf-sider .toggle-icon').click(function () {
			$(this).toggleClass('icon-right')
		})
	    $('#tabs1').tabs({
	        tabHeight: 44,
	    });

        //初始化首页面板
        $('#tabs1').tabs('add', {
            title: '首页',
            closable: false,
            content: '<div class="iframe-container"><iframe id="iframe" class="page-iframe" src="' + base_url + '/firstPage" frameborder="no" border="no" height="100%" width="100%" scrolling="auto"></iframe></div>',
        });
	})
</script>
</body>
</html>

