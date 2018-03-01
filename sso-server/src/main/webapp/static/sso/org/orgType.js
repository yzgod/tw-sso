$(function(){
	orgTypeInit();
	searchOrgTypeInit();
	orgTypeFormInit();
})

function searchOrgTypeInit(){
	$('#search_orgType').searchbox({
		searcher:function(value,name){ 
			$("#orgType_tab").datagrid('load',{
				keyword: value
			});
		}, 
		prompt:'名称或编码查询' 
	}); 
}

function orgTypeInit(){
    $("#orgType_tab").datagrid({
        url:base_url+'/base/orgType/query',
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
            {field:'name',title:'组织类型名称', width:20},
            {field:'code',title:'组织类型编码', width:20},
            {field:'remark',title:'备注', width:30}
        ]],
        queryParams: {
			keyword: ""
		}
    });
}

function orgTypeFormInit(){
	$("#orgTypeForm").form({
        url: base_url+'/base/orgType/save',
        onSubmit:function(){
         	return $(this).form("validate")
        },
        success: function (res) {
        	var res = JSON.parse(res)
        	if (res.code==200) {
                msg("保存成功！");
        		$('#orgType_data').dialog('close');
        		$("#orgType_tab").datagrid("reload");
        	}else{
        		msg(res.msg);
        	}
        }
    });
}

function addOrgType(){
	$("#orgTypeForm").form("clear")
	$("input[name=code]").validatebox("enable");
	$("#orgType_data").dialog("setTitle","添加组织类型").dialog("open");
}

function editOrgType(){
	var r = $("#orgType_tab").datagrid("getSelected");
	if(!r){
		msg("请选择组织类型!");
		return;
	}
	$("#orgTypeForm").form("clear");
	$("#orgTypeForm").form("load",r);
	$("input[name=code]").validatebox('disable');
	$("#orgType_data").dialog("setTitle","编辑组织类型").dialog("open");
}


