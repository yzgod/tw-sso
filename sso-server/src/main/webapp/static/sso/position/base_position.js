$(function(){
	positionInit();
	searchPositionInit();
	positionFormInit();
})

function searchPositionInit(){
	$('#search_position').searchbox({
		searcher:function(value,name){ 
			$("#position_tab").datagrid('load',{
				keyword: value
			});
		}, 
		prompt:'名称或编码查询' 
	}); 
}

function positionInit(){
    $("#position_tab").datagrid({
        url:base_url+'/base/position/query',
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
            {field:'name',title:'基础岗位名称', width:20},
            {field:'code',title:'基础岗位编码', width:20},
            {field:'remark',title:'备注', width:30}
        ]],
        queryParams: {
			keyword: ""
		}
    });
}

function positionFormInit(){
	$("#positionForm").form({
        url: base_url+'/base/position/save',
        onSubmit:function(){
         	return $(this).form("validate")
        },
        success: function (res) {
        	var res = JSON.parse(res)
        	if (res.code==200) {
                msg("保存成功！");
        		$('#position_data').dialog('close');
        		$("#position_tab").datagrid("reload");
        	}else{
        		msg(res.msg);
        	}
        }
    });
}

function addPosition(){
	$("#positionForm").form("clear")
	$("input[name=code]").validatebox("enable");
	$("#position_data").dialog("setTitle","添加基础岗位").dialog("open");
}

function editPosition(){
	var r = $("#position_tab").datagrid("getSelected");
	if(!r){
		msg("请选择基础岗位!");
		return;
	}
	$("#positionForm").form("clear");
	$("#positionForm").form("load",r);
	$("input[name=code]").validatebox('disable');
	$("#position_data").dialog("setTitle","编辑基础岗位").dialog("open");
}


