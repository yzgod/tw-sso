$(function(){
	searchInit();
	logInit();
})

function logInit(){
    $("#access_tab").datagrid({
        url:base_url+'/log/access/query',
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
            {field:'appCode',title:'访问应用编码', width:30},
            {field:'loginName',title:'访问用户', width:30},
            {field:'msg',title:'访问信息', width:50},
            {field:'date',title:'访问时间', width:40,formatter:longDateFmt},
            {field:'method',title:'访问形式', width:30},
            {field:'url',title:'访问地址',width:50},
            {field:'ip',title:'远程ip',width:50},
            {field:'parameter',title:'访问参数',width:100},
            {field:'timeUsed',title:'耗时',width:30, align:'right',formatter:timeUsedFmt}
        ]],
        queryParams: {
			appCode:  $("#appCode").combobox("getValue"),
			startDate: "",
			endDate: ""
		}
    });
}

function searchInit(){
	$("#startDate").datebox({
		editable:false,
		value:new Date().Format("yyyy-MM-dd ")+"00:00:00",
		onChange:function(n,o){
			var appCode = $("#appCode").combobox("getValue")
			var endDate = $("#endDate").datebox("getValue")
			searchLog(appCode,n,endDate)
		}
	})
	$("#endDate").datebox({
		editable:false,
		value:new Date().Format("yyyy-MM-dd ")+"00:00:00",
		onChange:function(n,o){
			var appCode = $("#appCode").combobox("getValue")
			var startDate = $("#startDate").datebox("getValue")
			searchLog(appCode,startDate,n)
		}
	})
	
	var appCode;
	$.ajax({
        url: base_url + '/app/getAll',
        async :false,
        dataType: 'json',
        success: function (re) {
        	if(re.length){
	            appCode = re[0].appCode
        	}
            
        }
    });
	$('#appCode').combobox({
		url : base_url + "/app/getAll",
		method:'get',
		valueField:'appCode',
		value:appCode,
		textField:'name',
		onChange:function(n,o){
			var startDate = $("#startDate").datebox("getValue")
			var endDate = $("#endDate").datebox("getValue")
			searchLog(n,startDate,endDate)
		}
	});
	
}

function searchLog(appCode,startDate,endDate){
	$("#access_tab").datagrid("load",{
			appCode: appCode,
			startDate: startDate,
			endDate: endDate
	})
}



function longDateFmt(v,r,i){
	return new Date(v).Format("yyyy-MM-dd hh:mm:ss")
}

function timeUsedFmt(v,r,i){
	if(!v)
		return "";
	return (v<1000?"<b style='color:green'>"+v+"</b>":"<b style='color:red'>"+v+"</b>") +" 毫秒";
}



