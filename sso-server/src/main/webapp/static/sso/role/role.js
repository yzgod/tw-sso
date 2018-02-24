$(function(){
	roleInit();
	roleFormInit();
	permInit();
	menuInit();
})

//角色tree初始化
function roleInit(){
    $("#role_tab").treegrid({
        url: base_url+'/role/getRoleTree',
        method:"get",
        idField: 'id',
        fit: true,
        treeField: 'name',
        fitColumns: true,
        animate: true,
        singleSelect:true,
        columns:[[
            {field:'id',title:'id',hidden:true, width:100},
            {field:'name',title:'角色名称', width:150},
            {field:'code',title:'角色编码', width:60},
            {field:'appCode',title:'所属应用编码', width:60},
            {field:'remark',title:'说明', width:100}
        ]],
        onClickRow :function(r){
        	if(r.id<0){
        		msg("请选择角色!")
        	}else{
	        	$("#menu_tab").treegrid("load",{appCode:r.appCode})
	        	$("#menu_tab").treegrid("uncheckAll")
	        	$("#saveRoleMenuBtn").linkbutton("disable")
	        	setTimeout(function(){//不延迟会有bug,dom未处理完uncheckAll,数据先到了,延时100
	        		$.ajax({
	        			url: base_url + '/menu/getMenusByRoleIdAndAppCode',
	        			dataType: 'json',
	        			data:{appCode:r.appCode,roleId:r.id},
	        			asnyc:false,
	        			success: function (re) {
	        				for (var i = 0; i < re.length; i++) {
	        					$("#menu_tab").treegrid("checkNode",re[i].id)
	        				}
	        				$("#menu_tab").parent().find(".tree-checkbox").on("click", function () {
	        					$("#saveRoleMenuBtn").linkbutton("enable")
	        				});
	        			}
	        		});	
	        	},100)
	        	
	        	$("#perm_tab").treegrid("load",{appCode:r.appCode})
	        	$("#perm_tab").treegrid("uncheckAll")
	        	$("#saveRolePermBtn").linkbutton("disable")
	        	
	        	setTimeout(function(){//不延迟会有bug,dom未处理完uncheckAll,数据先到了,延时100
	        		$.ajax({
	        			url: base_url + '/perm/getPermIdsByRoleIdAndAppCode',
	        			dataType: 'json',
	        			data:{appCode:r.appCode,roleId:r.id},
	        			asnyc:false,
	        			success: function (re) {
	        				for (var i = 0; i < re.length; i++) {
	        					$("#perm_tab").treegrid("checkNode",re[i])
	        				}
	        				$("#perm_tab").parent().find(".tree-checkbox").on("click", function () {
	        					$("#saveRolePermBtn").linkbutton("enable")
	        				});
	        			}
	        		});	
	        	},100)
	        	
        	}
        }
    });
}
//角色表单
function roleFormInit(){
	$("#roleForm").form({  
        url: base_url + '/role/save',
        onSubmit:function(){
         	return $(this).form("validate")
        },
        success: function (res) {
        	var res = JSON.parse(res)
        	if (res.code==200) {
        		$('#role_data').dialog('close');
        		$("#role_tab").treegrid("reload")//刷新
        	}
        	msg(res.msg);
        }
    })
}
//提交表单
function roleSubmit(){
	$("#roleForm").form('submit');
}
//新建角色
function addRole(){
	$("#roleCode").validatebox({disabled:false})
	var row = $("#role_tab").treegrid("getSelected")
	if(row==null){
		msg("请选择 应用 添加新角色!")
		return;
	}
	$("#roleForm").form('clear');
	//请求应用名
	$.ajax({
        url: base_url + '/app/getByAppCode',
        dataType: 'json',
        data:{appCode:row.appCode},
        asnyc:false,
        success: function (res) {
			$("#roleAppName").val(res.name)
        }
    });
    $("#roleAppCode").val(row.appCode);
    
    renderRoleTree($("#parentRole"),row.appCode);
    
    if(row.id<0){
		$("#parentRole").combotreegrid("setValue",{id:0,name:"root"})
    }else{
		$("#parentRole").combotreegrid("setValue",{id:row.id,name:row.name})
    }
    $("#role_data").dialog("setTitle","添加角色").dialog("open");
}
//编辑角色
function editRole(){
	$("#roleCode").validatebox({disabled:true})
	var row = $("#role_tab").treegrid("getSelected")
	if(row==null || row.id<0){
		msg("请选择 角色 编辑!")
		return;
	}
	$("#roleForm").form('clear');
	//请求应用名
	$.ajax({
        url: base_url + '/app/getByAppCode',
        dataType: 'json',
        data:{appCode:row.appCode},
        asnyc:false,
        success: function (res) {
			$("#roleAppName").val(res.name)
        }
    });
    $("#roleAppCode").val(row.appCode);
    renderRoleTree($("#parentRole"),row.appCode);
    $("#roleForm").form('load',row);
    $("#role_data").dialog("setTitle","编辑角色").dialog("open");
}
//删除角色
function deleteRole(){
	var row = $("#role_tab").treegrid("getSelected")
	if(row==null || row.id<0){
		msg("请选择需要删除的角色!")
		return;
	}
	$.ajax({
        url: base_url + '/role/delete/'+row.id,
        dataType: 'json',
        success: function (res) {
            if(res.code==200){
	            $("#role_tab").treegrid("reload")//刷新
            }
            msg(res.msg)
        }
    });
}

//菜单tree
function menuInit(){
	$("#menu_tab").treegrid({
		url: base_url + '/menu/getMenuTreeByAppCode',
        method:"get",
        animate:true,
	    idField:'id',  
	    treeField:'name',
	    fitColumns:true,
	    checkOnSelect:true,
	    cascadeCheck:false,
	    columns:[[    
	        {title:'id',field:'id',hidden:true,width:10},    
	        {title:'菜单名称',field:'name',width:150},    
	        {title:'菜单url',field:'url',width:100},
	        {title:'pattern',field:'pattern',width:50},
	        {title:'应用编码',field:'appCode',width:50}
	    ]],
        checkbox: true,
	    queryParams :{
        	appCode:""
        }
    });
}

// 保存授予的菜单
function saveRoleMenu(){
	var row = $("#role_tab").treegrid("getSelected")
	var ms = $("#menu_tab").treegrid("getCheckedNodes");
	var menuIds = new Array();
	for (var i = 0; i < ms.length; i++) {
		var m = ms[i]
		menuIds.push(m.id)
	}
	$.ajax({
        url: base_url + '/role/saveRoleMenu',
        method:'post',
        data:{
        	roleId:row.id,
        	menuIds:menuIds.join()
        },
        dataType: 'json',
        success: function (res) {
            msg(res.msg)
        }
    });
}

//权限tree
function permInit(){
	$("#perm_tab").treegrid({
		url: base_url + '/perm/getPermTree',
        method:"get",
        animate:true,
	    idField:'id',  
	    treeField:'name',
	    fitColumns:true,
	    checkOnSelect:true,
	    cascadeCheck:false,
	    columns:[[    
	        {title:'id',field:'id',hidden:true,width:10},    
	        {title:'权限组/权限名称',field:'name',width:150},    
	        {title:'编码',field:'code',width:70},
	        {title:'应用编码',field:'appCode',width:70}
	    ]],
        checkbox: function(r){
        	return r.id>0;
        },
	    queryParams :{
        	appCode:""
        }
    });
}

//保存授予的权限
function saveRolePerm(){
	var row = $("#role_tab").treegrid("getSelected")
	var ps = $("#perm_tab").treegrid("getCheckedNodes");
	var permIds = new Array();
	for (var i = 0; i < ps.length; i++) {
		var p = ps[i]
		permIds.push(p.id)
	}
	$.ajax({
        url: base_url + '/role/saveRolePerm',
        method:'post',
        data:{
        	roleId:row.id,
        	permIds:permIds.join()
        },
        dataType: 'json',
        success: function (res) {
            msg(res.msg)
        }
    });
}

