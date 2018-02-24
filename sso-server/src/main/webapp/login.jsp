<%@page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>权限认证中心</title>
    <%@include file='/common.jsp' %>
    <link rel="shortcut icon" type="image/x-icon" href="<s:static/>/favicon.ico"/>
    <link href="<s:static/>/login/login.css" rel="stylesheet">
</head>
<body>
    <div class="login-container">
        <div class="left-login"></div>
        <div class="right-login">
            <div class="title-login">单点登录认证中心<p>统一登录入口</p></div>
            <div class="form-container">
                <div class="alert alert-error alert-add" >
                    <span id="msg"></span>
                </div>
                <form id="loginForm">
                    <label for="login-y"><img src="<s:static/>/public/images/login/user-login.png" alt=""><input class="form-control" name="loginName" type="text" id="login-y" placeholder="用户名" value=""></label>
                    <label for="psd-y"><img src="<s:static/>/public/images/login/psd-login.png" alt=""><input class="form-control" name="password" type="password" id="psd-y" placeholder="密码" value=""><img id="isPsdHide" src="<s:static/>/public/images/login/display_eyeon.png" alt=""></label>
                    <input type="hidden" name="successUrl" value="<%=request.getParameter("successUrl")%>" />
                    <input type="hidden" name="rememberMeType" value="<%=request.getParameter("rememberMeType")%>" />
                    <div class="tips clearfix">
                         <label><input name="rememberMe" style="margin-bottom:8px;zoom:120%; " type="checkbox" />&nbsp7天免登陆</label>
                     </div>
                    <input type="button" onclick="login()" class="form-control" value="登录">
                </form>
            </div>
        </div>
    </div>
 
    <script>
        function login(){
        	var form = $("#loginForm").serialize()
        	$.ajax({
    			url : base_url+ '/login',
    			type : 'POST',
    			data : form,
    			async: false,
    			dataType : 'json',
    			success : function(res) {
    				if(res.code==500){
    					$("#msg").text(res.msg)
    				}
    				if(res.code==200){
    					var data = res.data;
    					var hf = data.setCookieUrl+"?toUrl="+data.successUrl+"&SESSION="+data.SESSION 
						if(data.AUTHUSER){
							hf += "&AUTHUSER="+data.AUTHUSER
						}
    					window.location.href = hf;
    				}
    			},
    			error : function() {
    				altert("连接异常!")
    			}
    		});
        }
    </script>
</body>
</html>