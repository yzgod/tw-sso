$(function(){
	logInit();
	searchInit();
})

function logInit(){
    $("#sms_tab").datagrid({
        url:base_url+'/sms/query',
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
            {field:'phones',title:'手机号', width:30},
            {field:'code',title:'短信模版号', width:15},
            {field:'signName',title:'签名', width:15},
            {field:'type',title:'类型', width:15},
            {field:'params',title:'参数', width:30},
            {field:'sendTime',title:'发送时间', width:30,formatter:longDateFmt}
        ]],
        queryParams: {
			code: "",
			startDate: "",
			endDate: ""
		}
    });
}

function searchInit(){
	$("#startDate").datebox({
		editable:false,
		onChange:function(n,o){
			var code = $("#code").textbox("getValue")
			var endDate = $("#endDate").datebox("getValue")
			searchSms(code,n,endDate)
		}
	})
	$("#endDate").datebox({
		editable:false,
		onChange:function(n,o){
			var code = $("#code").textbox("getValue")
			var startDate = $("#startDate").datebox("getValue")
			searchSms(code,startDate,n)
		}
	})
	$('#code').textbox({
		onChange:function(n,o){
			var startDate = $("#startDate").datebox("getValue")
			var endDate = $("#endDate").datebox("getValue")
			searchSms(n,startDate,endDate)
		}
	});
}

function searchSms(code,startDate,endDate){
	$("#sms_tab").datagrid("load",{
			code: code,
			startDate: startDate,
			endDate: endDate
	})
}

function longDateFmt(v,r,i){
	return new Date(v).Format("yyyy-MM-dd hh:mm:ss")
}



