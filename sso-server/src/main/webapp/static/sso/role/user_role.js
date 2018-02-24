$(function(){
	searchUser();
	userInit();
	roleInit()
})

 
function userInit(){
    $("#user_tab").datagrid({
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
        	$("#role_tab").treegrid("uncheckAll")
        	$("#saveUserRoleBtn").linkbutton("disable")
        	setTimeout(function(){
        		$.ajax({
        			url: base_url + '/role/getRoleIdsByUserId/'+r.id,
        			dataType: 'json',
        			asnyc:false,
        			success: function (re) {
        				for (var i = 0; i < re.length; i++) {
        					$("#role_tab").treegrid("checkNode",re[i])
        				}
        				$("#role_tab").parent().find(".tree-checkbox").on("click", function () {
        					$("#saveUserRoleBtn").linkbutton("enable")
        				});
        			}
        		});	
        	},100)
        },
        columns:[[
            {field:'id',title:'用户id',hidden:true, width:100},
            {field:'loginName',title:'用户账号', width:100},
            {field:'realName',title:'真实姓名', width:100},
            {field:'cellPhone',title:'电话号码',align:'center',width:100},
            {field:'email',title:'邮箱地址',align:'center',width:100},
            {field:'forbidden',title:'状态',align:'center',width:100,
            	formatter:function(v,r,i){
            		return v?"禁用":"启用"
            }},
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
        checkOnSelect:true,
	    cascadeCheck:false,
	    checkbox: function(r){
	    	return r.id>0
	    },
        columns:[[
            {field:'id',title:'id',hidden:true, width:100},
            {field:'name',title:'角色名称', width:150},
            {field:'code',title:'角色编码', width:60},
            {field:'appCode',title:'所属应用编码', width:60},
            {field:'remark',title:'说明', width:100}
        ]],
        onCheckNode:function(r){
        	var row = $("#user_tab").treegrid("getSelected")
        	if(!row){
        		msg("请选择用户!")
        	}
        }
    });
}

//保存角色分配
function saveUserRole(){
	var row = $("#user_tab").treegrid("getSelected")
	var rs = $("#role_tab").treegrid("getCheckedNodes");
	var roleIds = new Array();
	for (var i = 0; i < rs.length; i++) {
		var r = rs[i]
		roleIds.push(r.id)
	}
	$.ajax({
        url: base_url + '/role/saveUserRole',
        method:'post',
        data:{
        	userId:row.id,
        	roleIds:roleIds.join()
        },
        dataType: 'json',
        success: function (res) {
            msg(res.msg)
        }
    });
}
