$(function(){
	deptInit();
	searchDeptInit();
	deptFormInit();
})

function searchDeptInit(){
	$('#search_dept').searchbox({
		searcher:function(value,name){ 
			$("#dept_tab").datagrid('load',{
				keyword: value
			});
		}, 
		prompt:'名称或编码查询' 
	}); 
}

function deptInit(){
    $("#dept_tab").datagrid({
        url:base_url+'/base/dept/query',
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
            {field:'name',title:'基础部门名称', width:20},
            {field:'code',title:'基础部门编码', width:20},
            {field:'remark',title:'备注', width:30}
        ]],
        queryParams: {
			keyword: ""
		}
    });
}

function deptFormInit(){
	$("#deptForm").form({
        url: base_url+'/base/dept/save',
        onSubmit:function(){
         	return $(this).form("validate")
        },
        success: function (res) {
        	var res = JSON.parse(res)
        	if (res.code==200) {
                msg("保存成功！");
        		$('#dept_data').dialog('close');
        		$("#dept_tab").datagrid("reload");
        	}else{
        		msg(res.msg);
        	}
        }
    });
}

function addDept(){
	$("#deptForm").form("clear")
	$("input[name=code]").validatebox("enable");
	$("#dept_data").dialog("setTitle","添加基础部门").dialog("open");
}

function editDept(){
	var r = $("#dept_tab").datagrid("getSelected");
	if(!r){
		msg("请选择基础部门!");
		return;
	}
	$("#deptForm").form("clear");
	$("#deptForm").form("load",r);
	$("input[name=code]").validatebox('disable');
	$("#dept_data").dialog("setTitle","编辑基础部门").dialog("open");
}


