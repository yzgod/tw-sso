$(function(){
	orgInit();
	roleInit()
})

 
function orgInit(){
    $("#org_tab").treegrid({
        url:base_url+'/org/getOrgTree',
        method:"get",
        idField: 'id',
        fit: true,
        treeField: 'name',
        fitColumns: true,
        animate: true,
        loadFilter:function(data,parent){
        	if(parent==undefined){
	        	for (var i = 0; i < data.length; i++) {
	        		data[i].state='closed'
	        	}
	        	return data;
        	}
        },
        onClickRow :function(r){//选中组织
        	$("#role_tab").treegrid("uncheckAll")
        	$("#saveOrgRoleBtn").linkbutton("disable")
        	setTimeout(function(){
        		$.ajax({
        			url: base_url + '/role/getRoleIdsByOrgId/'+r.id,
        			dataType: 'json',
        			asnyc:false,
        			success: function (re) {
        				for (var i = 0; i < re.length; i++) {
        					$("#role_tab").treegrid("checkNode",re[i])
        				}
        				$("#role_tab").parent().find(".tree-checkbox").on("click", function () {
        					$("#saveOrgRoleBtn").linkbutton("enable")
        				});
        			}
        		});	
        	},100)
        },
        columns:[[
            {field:'id',title:'id',hidden:true, width:100},
            {field:'name',title:'组织名称', width:100},
            {field:'code',title:'组织编码', width:100},
            {field:'typeName',title:'组织分类',align:'center',width:100}
        ]]
    });
}

//角色tree初始化
function roleInit(){
    $("#role_tab").treegrid({
        url: base_url+'/role/getRoleTree',
        method:"get",
        idField: 'id',
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
        	var row = $("#org_tab").treegrid("getSelected")
        	if(!row){
        		msg("请选择组织机构!")
        	}
        }
    });
}

//保存角色分配
function saveOrgRole(){
	var row = $("#org_tab").treegrid("getSelected")
	var rs = $("#role_tab").treegrid("getCheckedNodes");
	var roleIds = new Array();
	for (var i = 0; i < rs.length; i++) {
		var r = rs[i]
		roleIds.push(r.id)
	}
	$.ajax({
        url: base_url + '/role/saveOrgRole',
        method:'post',
        data:{
        	orgId:row.id,
        	roleIds:roleIds.join()
        },
        dataType: 'json',
        success: function (res) {
            msg(res.msg)
        }
    });
}
