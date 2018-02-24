$(function(){
	appInit();
	menuInit();
	roleInit();
	menuFormInit();
	roleFormInit();
})

//菜单tree初始化
function menuInit(){
	$("#menu_tab").treegrid({
        url: base_url+'/menu/getMenuTreeByAppCode',
        method:"get",
        animate:true,
	    idField:'id',  
	    treeField:'name',
	    fitColumns:true,
	    columns:[[    
	        {title:'id',field:'id',hidden:true,width:10},    
	        {title:'菜单名称',field:'name',width:200},    
	        {title:'应用编码',field:'appCode',width:60},
	        {title:'菜单url',field:'url',width:100},
	        {title:'菜单pattern',field:'pattern',width:100},
	        {title:'菜单说明',field:'remark',width:200} 
	    ]],
	    onClickRow :function(r){
        	$("#role_tab").datagrid("load",{mId: r.id})//加载直接角色列表
        },
	    queryParams :{
        	appCode:""
        }
    });
}
//应用列表初始化
function appInit(){
    $("#app_tab").datagrid({
        url:base_url+'/app/getAll' ,
        method:'get',
        checkOnSelect: true,
        fitColumns:true,
        singleSelect:true,
        fit:true,
        rownumbers:true,
        onClickRow :function(i,r){//选中后展示菜单树
        	$("#role_tab").datagrid("loadData",{rows:{}})//清空角色列表
        	$("#menu_tab").treegrid("load",{appCode:r.appCode})
        },
        columns:[[
            {field:'name',title:'应用名称', width:100},
            {field:'appCode',title:'应用编码', width:100}
        ]]
    });
}
//直接角色列表初始化
function roleInit(){
    $("#role_tab").datagrid({
        url:base_url+'/role/getRolesByMenuId' ,
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
        	mId: 0
        }
    });
}

/** 新增菜单 */
function addMenu(){
	var row = $("#app_tab").datagrid("getSelected")
	if(row==null){
		msg("请选择应用添加菜单!")
		return;
	}
	$("#menuForm").form('clear');
	$("#appName").val(row.name)
	$("input[name=appCode]").val(row.appCode);
	renderMenuTree($("#parentMenu"),row.appCode);
	var mRow = $("#menu_tab").treegrid("getSelected")
	if(mRow!=null && mRow.appCode==row.appCode){//判断添加显示默认父菜单
		$("#parentMenu").combotreegrid("setValue",{id:mRow.id,name:mRow.name})
	}else{
		$("#parentMenu").combotreegrid("setValue",{id:0,name:"根菜单"})
	}
	$("#menu_data").dialog("setTitle","添加菜单").dialog("open");
}

/** 编辑菜单 */
function editMenu(){
	var row = $("#app_tab").datagrid("getSelected")
	if(row==null){
		msg("请选择应用!")
		return;
	}
	
	var mRow = $("#menu_tab").treegrid("getSelected")
	if(mRow==null){
		msg("请选择修改的菜单!")
		return;
	}
	
	$("#menuForm").form('clear');
	$("#appName").val(row.name);
	renderMenuTree($("#parentMenu"),mRow.appCode);
	$('#menuForm').form('load', mRow);
	
	$.ajax({
        url: base_url + '/menu/get/'+mRow.parentId,
        dataType: 'json',
        asnyc:false,
        success: function (res) {
			$("#parentMenu").combotreegrid("setValue",{id:res.id,name:res.name})
        }
    });
    
	$("#menu_data").dialog("setTitle","编辑菜单").dialog("open");
}



//删除菜单
function deleteMenu(){
	var row = $("#menu_tab").treegrid("getSelected")
	if(row==null){
		msg("请选择删除的菜单!")
		return;
	}
	$.ajax({
        url: base_url + '/menu/delete/'+row.id,
        dataType: 'json',
        success: function (res) {
            msg(res.msg)
            $("#menu_tab").treegrid("reload")//刷新
        }
    });
}

function menuFormInit(){
	$("#menuForm").form({  
        url: base_url + '/menu/save',
        onSubmit:function(){
         	return $(this).form("validate")
        },
        success: function (res) {
        	var res = JSON.parse(res)
        	if (res.code==200) {
                msg("保存成功！");
        		$('#menu_data').dialog('close');
        		$("#menu_tab").treegrid("reload")//刷新
        	}else{
        		msg(res.msg);
        	}
        }
    })
}

//提交表单
function menuSubmit(){
	$("#menuForm").form('submit');
}

//角色授予
function giveRoles(){
	var row = $("#menu_tab").treegrid("getSelected")
	if(row==null){
		msg("请选择授予角色的菜单!")
		return;
	}
	$("input[name=menuId]").val(row.id)
	$("input[name=menuName]").val(row.name)
	roleIdsCombo(row.appCode)
	$("#role_data").dialog("setTitle","角色授予").dialog("open");
	
}

function giveRolesSubmit(){
	$("#roleForm").form('submit');
}

function roleFormInit(){
	$("#roleForm").form({  
        url: base_url + '/role/saveMenuRole',
        onSubmit:function(){
        	var b = $(this).form("validate")
        	if(b){
        		var roleIds = $("#roleIds").combobox("getValues").join();
        		$("input[name=roleIds]").val(roleIds)
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

//取消指定菜单对角色的授权
function cancelRole(){
	var row = $("#app_tab").datagrid("getSelected")
	if(row==null){
		msg("请选择应用!")
		return;
	}
	
	var mRow = $("#menu_tab").treegrid("getSelected")
	if(mRow==null || mRow.appCode!=row.appCode){
		msg("请选择取消授予角色的菜单!")
		return;
	}
	
	
	var rRows = $("#role_tab").datagrid("getSelections")
	if(rRows.length==0){
		msg("请选择取消授予的角色!")
		return
	}
	var rIds = new Array();
	for(j = 0;j<rRows.length; j++) {
   		rIds.push(rRows[j].id)
	}
	
	//取消逻辑
	$.ajax({
		url: base_url + '/role/deleteRoleMenu',
		method:"post",
        dataType: 'json',
        data:{
        	menuId:mRow.id,
        	roleIds:rIds.join()
        },
        async :false,
        success: function (res) {
        	msg(res.msg)
        	$("#role_tab").datagrid("reload")//刷新
        }
	})
}
