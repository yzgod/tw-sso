/**
 * 拓展treegrid
 * 树形表格
 */
$.extend($.fn.treegrid.methods, {
    /**
     * 添加取消全部checknode
     */
    uncheckAll: function (jq) {
        var nodes = jq.treegrid("getCheckedNodes");
        $.each(nodes, function (i, v) {
            jq.treegrid("uncheckNode", v.id);
        });
    }
});

/**
 * 添加选项卡
 * @param title
 * @param url
 */
function addTab(title, url) {
    var $tabs = $('#tabs1'),
        existTab = $tabs.tabs('getTab', title);
    if (existTab) {
        $tabs.tabs('select', title);
    } else {
        $tabs.tabs('add', {
            title: title,
            closable: true,
            content: '<div class="iframe-container"><iframe id="iframe" class="page-iframe" src="' + base_url + url + '" frameborder="no"   border="no" height="100%" width="100%" scrolling="auto"></iframe></div>'
        });
    }
}

/**
 * 递归修改或者添加自身和children属性中的属性，
 * @param data 数据
 * @param oldPro 原属性的名称
 * @param newPro 新属性的名称
 * @param callback 把原属性的的传入该方法去返回值作为新属性
 */
function recursiveModify(data, oldPro, newPro, callback) {
    $.each(data, function (i, v) {
        var oldProperty = v[oldPro],
            newProperty = oldProperty;
        if (callback)
            newProperty = callback(oldProperty);// 有回则交由回调函数处理
        v[newPro] = newProperty;
        if (v.children)
            recursiveModify(v.children,oldPro, newPro, callback);// 如果有子集则递归修改子集
    });
    return data;
}

Date.prototype.Format = function (fmt) {
    var o = {
        "M+": this.getMonth() + 1, //月份
        "d+": this.getDate(), //日
        "h+": this.getHours(), //小时
        "m+": this.getMinutes(), //分
        "s+": this.getSeconds(), //秒
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
        "S": this.getMilliseconds() //毫秒
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}

/**
 * 时间格式化，前端input的class为easyui-date
 * @param arguments 时间数据
 * @returns
 */
function getTime(arguments) {
	var tss = arguments.time || 0;
	var ts=tss.toString().substring(0,10);
    var t, y, m, d, h, i, s;
    t = ts ? new Date(ts * 1000) : new Date();
    y = t.getFullYear();
    m = t.getMonth() + 1;
    d = t.getDate();
    h = t.getHours();
    i = t.getMinutes();
    s = t.getSeconds();
    // 可根据需要在这里定义时间格式  
    return y + '-' + (m < 10 ? '0' + m : m) + '-' + (d < 10 ? '0' + d : d) + ' ' + (h < 10 ? '0' + h : h) + ':' + (i < 10 ? '0' + i : i) + ':' + (s < 10 ? '0' + s : s);
}
/**
 * 前台对比日期大小
 * @param checkStartDate 开始时间
 * @param checkEndDate 结束时间
 * @returns 开始时间>结束时间为false,否则为true
 */
function compareDate(checkStartDate, checkEndDate) {      
	var arys1= new Array();      
    var arys2= new Array();      
	if(checkStartDate != null && checkEndDate != null) {      
	    arys1=checkStartDate.split('-');      
	      var sdate=new Date(arys1[0],parseInt(arys1[1]-1),arys1[2]);      
	    arys2=checkEndDate.split('-');      
	    var edate=new Date(arys2[0],parseInt(arys2[1]-1),arys2[2]);      
		if(sdate > edate) {      
		    return false;         
		}  else {   
		    return true;      
		}   
   }      
}

function msg(msg) {
    $.messager.show({
        title:'提示',
        msg:msg,
        showType:'show'
    });
}

//后端异常数据处理
function resHandle(res){
	if(res.code){
		if(res.code!=200 && res.msg){
			msg(res.msg)
		}
	}else{
		msg("服务器异常!")
	}
}

$.extend($.fn.validatebox.defaults.rules, {
    eqPwd : {
        validator : function(value, param) {
            return value == $(param[0]).val();
        },
        message : '两次输入密码不一致！'
    },
    idcard : {// 验证身份证
        validator : function(value) {
            return /^\d{15}(\d{2}[A-Za-z0-9])?$/i.test(value);
        },
        message : '身份证号码格式不正确'
    },
    minLength: {
        validator: function(value, param){
            return value.length >= param[0];
        },
        message: '请输入至少（2）个字符.'
    },
    length:{validator:function(value,param){
        var len=$.trim(value).length;
            return len>=param[0]&&len<=param[1];
        },
        message:"输入内容长度必须介于{0}和{1}之间."
    },
    mobile : {// 验证手机号码
        validator : function(value) {
            return /^(13|14|15|17|18)\d{9}$/i.test(value);
        },
        message : '手机号码格式不正确'
    },
    intOrFloat : {// 验证整数或小数
        validator : function(value) {
            return /^\d+(\.\d+)?$/i.test(value);
        },
        message : '请输入数字，并确保格式正确'
    },
    currency : {// 验证货币
        validator : function(value) {
            return /^\d+(\.\d+)?$/i.test(value);
        },
        message : '货币格式不正确'
    },
    qq : {// 验证QQ,从10000开始
        validator : function(value) {
            return /^[1-9]\d{4,9}$/i.test(value);
        },
        message : 'QQ号码格式不正确'
    },
    integer : {// 验证整数
        validator : function(value) {
            return /^[+]?[1-9]+\d*$/i.test(value);
        },
        message : '请输入整数'
    },
    age : {// 验证年龄
        validator : function(value) {
            return /^(?:[1-9][0-9]?|1[01][0-9]|120)$/i.test(value);
        },
        message : '年龄必须是0到120之间的整数'
    },
    chinese : {// 验证中文
        validator : function(value) {
            return /^[\Α-\￥]+$/i.test(value);
        },
        message : '请输入中文'
    },
    english : {// 验证英语
        validator : function(value) {
            return /^[A-Za-z]+$/i.test(value);
        },
        message : '请输入英文'
    },
    unnormal : {// 验证是否包含空格和非法字符
        validator : function(value) {
            return /.+/i.test(value);
        },
        message : '输入值不能为空和包含其他非法字符'
    },
    username : {// 验证用户名
        validator : function(value) {
            return /^[a-zA-Z][a-zA-Z0-9_]{5,15}$/i.test(value);
        },
        message : '用户名不合法（字母开头，允许6-16字节，允许字母数字下划线）'
    },
    faxno : {// 验证传真
        validator : function(value) {
            return /^((\(\d{2,3}\))|(\d{3}\-))?(\(0\d{2,3}\)|0\d{2,3}-)?[1-9]\d{6,7}(\-\d{1,4})?$/i.test(value);
        },
        message : '传真号码不正确'
    },
    zip : {// 验证邮政编码
        validator : function(value) {
            return /^[0-9]\d{5}$/i.test(value);
        },
        message : '邮政编码格式不正确'
    },
    ip : {// 验证IP地址
        validator : function(value) {
            return /d+.d+.d+.d+/i.test(value);
        },
        message : 'IP地址格式不正确'
    },
    name : {// 验证姓名，可以是中文或英文
            validator : function(value) {
                return /^[\Α-\￥]+$/i.test(value)|/^\w+[\w\s]+\w+$/i.test(value);
            },
            message : '请输入姓名'
    },
    msn:{
            validator : function(value){
                return /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/.test(value);
            },
            message : '请输入有效的msn账号(例：abc@hotnail(msn/live).com)'
    },
    orgCodeRepeat:{//组织code重复验证
    		validator : function(value){
    			var b;
    			$.ajax({
					url: base_url + '/org/validateCode',
					method: 'GET',
					data: {code : value },
			        dataType : "json",
			        async:false,
			        success:function(data){
			        	b = data;
			        },
			        error:function(){
			        	alert("error")
			        }
				});
				return b;
            },
            message : '组织编码重复!'
    },
    ugCodeRepeat:{//用户组code重复验证
    		validator : function(value){
    			var b;
    			$.ajax({
					url: base_url + '/ug/validateCode',
					method: 'GET',
					data: {code : value },
			        dataType : "json",
			        async:false,
			        success:function(data){
			        	b = data;
			        },
			        error:function(){
			        	alert("error")
			        }
				});
				return b;
            },
            message : '用户组编码重复!'
    },
    loginNameRepeat:{//用户账号重复验证
    		validator : function(value){
    			var b = /^[a-zA-Z][a-zA-Z0-9_]{2,15}$/i.test(value);
    			if(!b){
    				return false;
    			}
    			$.ajax({
					url: base_url + '/user/validateLoginName',
					method: 'GET',
					data: {loginName : value },
			        dataType : "json",
			        async:false,
			        success:function(data){
			        	b = data;
			        },
			        error:function(){
			        	alert("error")
			        }
				});
				return b;
            },
            message : '用户账号不合法或重复!(请输入3-16位,拉丁字母账号)'
    },
    cellPhoneRepeat:{//手机号重复验证
    		validator : function(value){
    			var b = /^(13|14|15|17|18)\d{9}$/i.test(value)
    			if(!b){
    				return false;
    			}
    			$.ajax({
					url: base_url + '/user/validateCellPhone',
					method: 'GET',
					data: {cellPhone : value },
			        dataType : "json",
			        async:false,
			        success:function(data){
			        	b = data;
			        },
			        error:function(){
			        	alert("error")
			        }
				});
				return b;
            },
            message : '手机号码格式不准确或重复!'
    }
});

