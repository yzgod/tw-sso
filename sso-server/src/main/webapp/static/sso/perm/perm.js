$(function(){
	pgInit();
	permInit();
	roleInit();
	pgFormInit();
	permFormInit();
	roleFormInit();
})

//权限组tree初始化
function pgInit(){
    $("#pg_tab").treegrid({
        url: base_url+'/pg/getPermGroupTree',
        method:"get",
        idField: 'id',
        fit: true,
        treeField: 'name',
        fitColumns: true,
        animate: true,
        singleSelect:true,
        columns: [[               
            { field: 'id', hidden: 'true'},
         	{ field: 'name', title: '应用/权限组名称',width:200},
         	{ field: 'code', title: '权限组编码',width:100},
         	{ field: 'appCode', title: '应用编码',width:100}
        ]],
        onClickRow :function(r){
        	$("#perm_tab").datagrid("load",{pgId:r.id})//加载权限列表
        	$("#role_tab").datagrid("loadData",{rows:{}})//清空角色列表
        }
    });
}

//权限初始化
function permInit(){
	$("#perm_tab").datagrid({
        url: base_url+'/perm/getPermsByPgId',
        method:"get",
	    fitColumns:true,
	    rownumbers:true,
	    fit:true,
	    singleSelect:true,
	    columns:[[
	        {title:'id',field:'id',hidden:true,width:10},    
	        {title:'权限名称',field:'name',width:100},    
	        {title:'权限编码',field:'code',width:100},
	        {title:'权限组编码',field:'groupCode',width:100},
	        {title:'应用编码',field:'appCode',width:50},
	        {title:'权限说明',field:'remark',width:150} 
	    ]],
	    onClickRow :function(i,r){
        	$("#role_tab").datagrid("load",{permId: r.id})//加载直接角色列表
        },
	    queryParams :{
        	pgId:0
        }
    });
}
//直接角色列表初始化
function roleInit(){
    $("#role_tab").datagrid({
        url:base_url+'/role/getRolesByPermId' ,
        method:'get',
        checkOnSelect: true,
        fitColumns:true,
        singleSelect:false,
        fit:true,
        rownumbers:true,
        columns:[[
            {field:'id',title:'id',hidden:true, width:100},
            {field:'name',title:'角色名称', width:150},
            {field:'code',title:'角色编码', width:100},
            {field:'appCode',title:'所属应用编码', width:100}
        ]],
        queryParams :{
        	permId: 0
        }
    });
}

function pgFormInit(){
	$("#pgForm").form({  
        url: base_url + '/pg/save',
        onSubmit:function(){
         	return $(this).form("validate")
        },
        success: function (res) {
        	var res = JSON.parse(res)
        	if (res.code==200) {
        		$('#pg_data').dialog('close');
        		$("#pg_tab").treegrid("reload")//刷新
        	}
        	msg(res.msg);
        }
    })
}

//提交表单
function pgSubmit(){
	$("#pgForm").form('submit');
}


/** 新增权限组 */
function addPg(){
	$("#pgCode").validatebox({disabled:false})
	var row = $("#pg_tab").treegrid("getSelected")
	if(row==null){
		msg("请选择 应用/权限组 添加权限组!")
		return;
	}
	$("#pgForm").form('clear');
	//请求应用名
	$.ajax({
        url: base_url + '/app/getByAppCode',
        dataType: 'json',
        data:{appCode:row.appCode},
        asnyc:false,
        success: function (res) {
			$("#pgAppName").val(res.name)
        }
    });
    $("#pgAppCode").val(row.appCode);
    renderPgTree($("#parentPg"),row.appCode);
    if(row.id<0){
		$("#parentPg").combotreegrid("setValue",{id:0,name:"根权限组"})
    }else{
		$("#parentPg").combotreegrid("setValue",{id:row.id,name:row.name})
    }
    $("#pg_data").dialog("setTitle","添加权限组").dialog("open");
	
}

/** 编辑权限组 */
function editPg(){
	var row = $("#pg_tab").treegrid("getSelected")
	if(row==null || row.id<0){
		msg("请选择权限组编辑!")
		return;
	}
	$("#pgCode").validatebox({disabled:true})
	$("#pgForm").form('clear');
	//请求应用名
	$.ajax({
        url: base_url + '/app/getByAppCode',
        dataType: 'json',
        data:{appCode:row.appCode},
        asnyc:false,
        success: function (res) {
			$("#pgAppName").val(res.name)
        }
    });
    $("#pgAppCode").val(row.appCode);
    renderPgTree($("#parentPg"),row.appCode);
    $('#pgForm').form('load', row);
    $("#pg_data").dialog("setTitle","编辑权限组").dialog("open");
}


//删除权限组
function deletePg(){
	var row = $("#pg_tab").treegrid("getSelected")
	if(row==null || row.id<0){
		msg("请选择需要删除的权限组!")
		return;
	}
	$.ajax({
        url: base_url + '/pg/delete/'+row.id,
        dataType: 'json',
        success: function (res) {
            msg(res.msg)
            $("#pg_tab").treegrid("reload")//刷新
        }
    });
}

//权限form
function permFormInit(){
	$("#permForm").form({  
        url: base_url + '/perm/save',
        onSubmit:function(){
         	return $(this).form("validate")
        },
        success: function (res) {
        	var res = JSON.parse(res)
        	if (res.code==200) {
        		$('#perm_data').dialog('close');
        		$("#perm_tab").datagrid("reload")//刷新
        	}
       		msg(res.msg);
        }
    })
}

//提交表单
function permSubmit(){
	$("#permForm").form('submit');
}


/** 新增权限 */
function addPerm(){
	$("#permGroup").validatebox({disabled:false})
	$("#permCode").validatebox({disabled:false})
	var row = $("#pg_tab").treegrid("getSelected")
	if(row==null){
		msg("请选择 应用/权限组 添加权限!")
		return;
	}
	$("#permForm").form('clear');
	$("#permAppCode").val(row.appCode)
	//请求应用名
	$.ajax({
        url: base_url + '/app/getByAppCode',
        dataType: 'json',
        data:{appCode:row.appCode},
        asnyc:false,
        success: function (res) {
			$("#permAppName").val(res.name)
        }
    });
    renderPgTree($("#permGroup"),row.appCode);
    if(row.id<0){
		$("#permGroup").combotreegrid("setValue",{id:0,name:"根权限组"})
    }else{
		$("#permGroup").combotreegrid("setValue",{id:row.id,name:row.name})
    }
    $("#perm_data").dialog("setTitle","添加权限").dialog("open");
	
}

/** 编辑权限 */
function editPerm(){
	$("#permGroup").validatebox({disabled:true})
	$("#permCode").validatebox({disabled:true})
	var row = $("#perm_tab").datagrid("getSelected")
	if(row==null){
		msg("请选择权限!")
		return;
	}
	$("#permForm").form('clear');
	$("#permAppCode").val(row.appCode)
	//请求应用名
	$.ajax({
        url: base_url + '/app/getByAppCode',
        dataType: 'json',
        data:{appCode:row.appCode},
        asnyc:false,
        success: function (res) {
			$("#permAppName").val(res.name)
        }
    });
    renderPgTree($("#permGroup"),row.appCode);
	$('#permForm').form('load', row);
    $("#perm_data").dialog("setTitle","编辑权限").dialog("open");
	
}


//删除权限
function deletePerm(){
	var row = $("#perm_tab").datagrid("getSelected")
	if(row==null || row.id<0){
		msg("请选择需要删除的权限!")
		return;
	}
	$.ajax({
        url: base_url + '/perm/delete/'+row.id,
        dataType: 'json',
        success: function (res) {
            msg(res.msg)
            $("#perm_tab").datagrid("reload")//刷新
        }
    });
}

//角色授予
function giveRoles(){
	var row = $("#pg_tab").datagrid("getSelected")
	if(row==null || row.id<0){
		msg("请选择授予角色的权限组!")
		return;
	}
	
	$("input[name=pgName]").val(row.name)
	roleIdsCombo(row.appCode)
	permIdsCombo(row.id)
	$("#role_data").dialog("setTitle","角色授予").dialog("open");
	
}

function giveRolesSubmit(){
	$("#roleForm").form('submit');
}

function roleFormInit(){
	$("#roleForm").form({  
        url: base_url + '/role/savePermRole',
        onSubmit:function(){
        	var b = $(this).form("validate")
        	if(b){
        		var roleIds = $("#roleIds").combobox("getValues").join();
        		$("input[name=roleIds]").val(roleIds)
        		var permIds = $("#permIds").combobox("getValues").join();
        		$("input[name=permIds]").val(permIds)
        	}
         	return b;
        },
        success: function (res) {
        	var res = JSON.parse(res)
        	if (res.code==200) {
                msg("授予角色成功！");
        		$('#role_data').dialog('close');
        		$("#role_tab").datagrid("reload")//刷新
        	}else{
        		msg(res.msg);
        	}
        }
    })
}

function roleIdsCombo(appCode){
	$("#roleIds").combobox({                
		url : base_url + "/role/getRolesByAppCode",
		method :'get',
		valueField:"id",
		textField:"name",
		groupField:'appCode',
		multiple:true,
		groupFormatter: function(group){
			var name;
			$.ajax({
				url: base_url + '/app/getByAppCode',
		        dataType: 'json',
		        data:{
		        	appCode:group
		        },
		        async :false,
		        success: function (res) {
		        	name = res.name
		        }
			})
			return '<b>' + name + '</b>';
		},
		queryParams: {
			appCode : appCode
		}
	});
}

function permIdsCombo(pgId){
	$("#permIds").combobox({                
		url : base_url + "/perm/getPermsByPgId",
		method :'get',
		valueField:"id",
		textField:"name",
		multiple:true,
		queryParams: {
			pgId : pgId
		}
	});
}

//取消指定菜单对角色的授权
function cancelRole(){
	var row = $("#pg_tab").treegrid("getSelected")
	if(row==null){
		msg("请选择权限组!")
		return;
	}
	
	var pRow = $("#perm_tab").treegrid("getSelected")
	if(pRow==null || pRow.appCode!=row.appCode){
		msg("请选择取消授予角色的权限!")
		return;
	}
	
	var rRows = $("#role_tab").datagrid("getSelections")
	if(rRows.length==0){
		msg("请选择取消授予权限的角色!")
		return
	}
	var rIds = new Array();
	for(j = 0;j<rRows.length; j++) {
   		rIds.push(rRows[j].id)
	}
	
	//取消逻辑
	$.ajax({
		url: base_url + '/role/deleteRolePerm',
		method:"post",
        dataType: 'json',
        data:{
        	permId:pRow.id,
        	roleIds:rIds.join()
        },
        async :false,
        success: function (res) {
        	msg(res.msg)
        	$("#role_tab").datagrid("reload")//刷新
        }
	})
}
