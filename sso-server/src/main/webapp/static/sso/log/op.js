$(function(){
	searchInit();
	logInit();
})

function logInit(){
    $("#op_tab").datagrid({
        url:base_url+'/log/op/query',
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
            {field:'appCode',title:'应用编码', width:30},
            {field:'loginName',title:'操作用户', width:30},
            {field:'msg',title:'操作内容', width:100},
            {field:'date',title:'操作时间', width:30,formatter:longDateFmt}
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
		onChange:function(n,o){
			var appCode = $("#appCode").combobox("getValue")
			var endDate = $("#endDate").datebox("getValue")
			searchLog(appCode,n,endDate)
		}
	})
	$("#endDate").datebox({
		editable:false,
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
		textField:'name',
		value:appCode,
		onChange:function(n,o){
			var startDate = $("#startDate").datebox("getValue")
			var endDate = $("#endDate").datebox("getValue")
			searchLog(n,startDate,endDate)
		}
	});
	
}

function searchLog(appCode,startDate,endDate){
	$("#op_tab").datagrid("load",{
			appCode: appCode,
			startDate: startDate,
			endDate: endDate
	})
}

function longDateFmt(v,r,i){
	return new Date(v).Format("yyyy-MM-dd hh:mm:ss")
}



