$(function(){
	searchUser();
	userInit();
	roleInit()
	
	userFormInit();
})

function mainOrgInit(){
	$('#mainOrg').combotreegrid({
		url: base_url+'/org/getOrgTree',
        method:"get",
        idField: 'id',
        fit: true,
        treeField: 'name',
        fitColumns: true,
        animate: true,
        multiple:false,
        cascadeCheck:false,
        columns: [[               
            { field: 'id', hidden: 'true'},
         	{ field: 'name', title: '组织名称',width:200},
         	{ field: 'typeName', title: '类型',width:100}
        ]]
	});
}

function partTimeOrgsInit(){
	$('#partTimeOrgs').combotreegrid({
		url: base_url+'/org/getOrgTree',
        method:"get",
        idField: 'id',
        fit: true,
        treeField: 'name',
        fitColumns: true,
        animate: true,
        multiple:true,
        cascadeCheck:false,
        columns: [[               
            { field: 'id', hidden: 'true'},
         	{ field: 'name', title: '组织名称',width:200},
         	{ field: 'typeName', title: '类型',width:100}
        ]]
	});
}

function mainPositionInit(){
	$('#mainPosition').combotreegrid({
		url: base_url+'/position/getOrgPostionTree',
        method:"get",
        idField: 'id',
        fit: true,
        treeField: 'name',
        fitColumns: true,
        animate: true,
        cascadeCheck:false,
        onSelect:function(r){
        	var b = r.id>0
        	if(!b){
        		msg("请选择岗位!")
        		$('#mainPosition').combotreegrid("uncheckNode",r.id);
        	}
        	return b
        },
        columns: [[               
            { field: 'id', hidden: 'true'},
         	{ field: 'name', title: '岗位名称',width:200},
         	{ field: 'typeName', title: '类型',width:100,formatter:posFmt}
        ]],
        loadFilter:function(data,parentId){
        	return data.data
        }
	});
}

function partTimePositionsInit(){
	$('#partTimePositions').combotreegrid({
		url: base_url+'/position/getOrgPostionTree',
        method:"get",
        idField: 'id',
        fit: true,
        treeField: 'name',
        fitColumns: true,
        animate: true,
        multiple:true,
        cascadeCheck:false,
        onBeforeCheckNode:function(r){
        	var b = r.id>0
        	if(!b){
        		msg("请选择岗位!")
        	}
        	return b
        },
        columns: [[               
            { field: 'id', hidden: 'true'},
         	{ field: 'name', title: '岗位名称',width:200},
         	{ field: 'typeName', title: '类型',width:100,formatter:posFmt}
        ]],
        loadFilter:function(data,parentId){
        	return data.data
        }
	});
}

function userGroupsInit(){
	$('#userGroups').combotreegrid({
		url: base_url+'/ug/getUgTree',
        method:"get",
        idField: 'id',
        fit: true,
        treeField: 'name',
        fitColumns: true,
        animate: true,
        multiple:true,
        cascadeCheck:false,
        columns: [[               
            { field: 'id', hidden: 'true'},
         	{ field: 'name', title: '用户组',width:200},
         	{ field: 'code', title: '编码',width:80}
        ]]
	});
}

function addUser(){
	$("input[name=loginName]").validatebox({disabled:false})
	$("input[name=cellPhone]").validatebox({disabled:false})
	$('#partTimeOrgs').combotreegrid("clear");
	$("#userForm").form('clear');
	$("#user_data").dialog("setTitle","添加用户").dialog("open");
}

function editUser(){
	var row = $("#userinfo_tab").datagrid('getSelected');
	if(row==null){
		msg("请选择用户!")
		return
	}
	$("input[name=loginName]").validatebox({disabled:true})
	$("input[name=cellPhone]").validatebox({disabled:true})
	$("#userForm").form('clear');
	$.ajax({
		url: base_url + '/user/getDetail/'+row.id,
		method: 'GET',
        dataType : "json",
        async:false,
        success:function(res){
        	if(res.code==200){
        		var user = res.data
				$('#userForm').form('load', user);
				$('#mainOrg').combotreegrid("setValue",user.mainOrg);
				$('#partTimeOrgs').combotreegrid("setValues",user.partTimeOrgs);
				$('#mainPosition').combotreegrid("setValue",user.mainPosition);
				$('#partTimePositions').combotreegrid("setValues",user.partTimePositions);
				$('#userGroups').combotreegrid("setValues",user.userGroups);
        	}else{
        		msg(res.msg)
        	}
        }
	});
	$("#user_data").dialog("setTitle","编辑用户").dialog("open");
}

function userFormInit(){
	$("#userForm").form({
        url: base_url+'/user/save',
        onSubmit:function(){
         	return $(this).form("validate")
        },
        success: function (res) {
        	var res = JSON.parse(res)
        	if (res.code==200) {
                msg("保存成功！");
        		$('#user_data').dialog('close');
        		$("#userinfo_tab").datagrid("reload");
        	}else{
        		msg(res.msg);
        	}
        }
    });
    
    mainOrgInit();
	partTimeOrgsInit();
    mainPositionInit();
    partTimePositionsInit();
    userGroupsInit();
}

//提交表单
function userSubmit(){
	$("#userForm").form('submit');
}

//有效角色表初始化
function roleInit(){
	$("#role_tab").treegrid({
        url: base_url+'/user/getAppRoleByUserId',
        method:"get",
        animate:true,
	    idField:'id',  
	    treeField:'name',
	    fitColumns:true,
	    columns:[[    
	        {title:'应用/角色名称',field:'name',width:200},    
	        {title:'角色编码',field:'code',width:60},    
	        {title:'应用编码',field:'appCode',width:60},
	        {title:'角色说明',field:'remark',width:200} 
	    ]],
	    loadFilter:function(res){
	    	resHandle(res)
	    	if(res.code==200){
	    		return res.data
	    	}
	    },
	    queryParams:{
	    	uId:0
	    }
    });
}
 
function userInit(){
    $("#userinfo_tab").datagrid({
        url:base_url+'/user/queryPage',
        method:"get",
        checkOnSelect: true,
        pagination:true,
        pageSize:20,
        pageNumber:1,
        fitColumns:true,
        singleSelect:true,
        fit:true,
        rownumbers:true,
        onClickRow :function(i,r){//选中用户
        	$("#role_tab").treegrid("load",{uId:r.id});
        	if(r.forbidden){
        		$("#forbiddenUser_btn").find(".l-btn-text").text("启用登录")
        	}else{
        		$("#forbiddenUser_btn").find(".l-btn-text").text("禁止登录")
        	}
        },
        columns:[[
            {field:'id',title:'用户id',hidden:true, width:100},
            {field:'loginName',title:'用户账号', width:100},
            {field:'realName',title:'真实姓名', width:100},
            {field:'cellPhone',title:'电话号码',align:'center',width:100},
            {field:'email',title:'邮箱地址',align:'center',width:100},
            {field:'forbidden',title:'状态',align:'center',width:100,formatter:userStatusFmt},
            {field:'createTime',title:'创建日期',align:'center',width:100}
        ]],
        queryParams: {
			keyword: ""
		}
    });
}

function searchUser(){
	$('#search_user').searchbox({ 
		searcher:function(value,name){ 
			$("#userinfo_tab").datagrid('load',{
				keyword: value
			});
		}, 
		prompt:'姓名或手机号查询' 
	}); 
}

function userStatusFmt(value,row,index){
	if (!value){
		return "<span title='启用' class='iconfont icon-chenggong' style='color:#1AE61A'></span>";
	} 
	return "<span title='禁用' class='iconfont icon-iconset0187' style='color:red'></span>";
}

function forbiddenUser(){
	var row = $("#userinfo_tab").datagrid('getSelected');
	if(row==null){
		msg("请选择用户!")
	}
	if(row.forbidden){
		$.messager.confirm({
			width: 380,
    		height: 160, 
			title :'确认启用登录', 
			msg: '您确定要启用用户['+row.realName+']登录吗？', 
			fn : function(r){
				if (r){
					$.ajax({
						url: base_url + '/user/enable/'+row.id,
	                    dataType: 'json',
	                    success: function (re) {
	                    	$("#userinfo_tab").datagrid("reload")
	                    	msg(re.msg)
	                    }
					})
				}
			}
		});
	}else{
		$.messager.confirm({
			width: 380,
    		height: 160, 
			title :'确认禁止登录', 
			msg: '您确定要禁止用户['+row.realName+']登录吗？', 
			fn : function(r){
				if (r){
					$.ajax({
						url: base_url + '/user/forbidden/'+row.id,
	                    dataType: 'json',
	                    success: function (re) {
	                    	$("#userinfo_tab").datagrid("reload")
	                    	msg(re.msg)
	                    }
					})
				}
			}
		});
	}
	
}

function posFmt(value,row,index){
	return value ? value:"岗位";
}


