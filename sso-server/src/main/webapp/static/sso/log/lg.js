$(function(){
	logInit();
	searchInit();
})

function logInit(){
    $("#lg_tab").datagrid({
        url:base_url+'/log/login/query',
        method:"get",
        checkOnSelect: true,
        pagination:true,
        pageSize:20,
        pageNumber:1,
        fitColumns:true,
        singleSelect:true,
        fit:true,
        rownumbers:true,
        columns:[[
            {field:'id',title:'id',hidden:true, width:10},
            {field:'loginName',title:'用户账号', width:30},
            {field:'realName',title:'用户姓名', width:30},
            {field:'ip',title:'ip地址', width:30},
            {field:'type',title:'类型', width:30,formatter:loginTypeFmt},
            {field:'createDate',title:'时间', width:30,formatter:longDateFmt}
        ]],
        queryParams: {
			loginName: "",
			startDate: "",
			endDate: ""
		}
    });
}

function searchInit(){
	$("#startDate").datebox({
		editable:false,
		onChange:function(n,o){
			var loginName = $("#loginName").textbox("getValue")
			var endDate = $("#endDate").datebox("getValue")
			searchLog(loginName,n,endDate)
		}
	})
	$("#endDate").datebox({
		editable:false,
		onChange:function(n,o){
			var loginName = $("#loginName").textbox("getValue")
			var startDate = $("#startDate").datebox("getValue")
			searchLog(loginName,startDate,n)
		}
	})
	$('#loginName').textbox({
		onChange:function(n,o){
			var startDate = $("#startDate").datebox("getValue")
			var endDate = $("#endDate").datebox("getValue")
			searchLog(n,startDate,endDate)
		}
	});
}

function searchLog(loginName,startDate,endDate){
	$("#lg_tab").datagrid("load",{
			loginName: loginName,
			startDate: startDate,
			endDate: endDate
	})
}

function longDateFmt(v,r,i){
	return new Date(v).Format("yyyy-MM-dd hh:mm:ss")
}

function loginTypeFmt(v,r,i){
	return v?
		(v==1?"<span style='color:green'>用户登录</span>":
		(v==2?"<span style='color:red'>禁止登录</span>":
		(v==3?"<span style='color:red'>强制下线</span>":"未知"))):
		"<span style='color:blue'>用户注销</span>";
}



