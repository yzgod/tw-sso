$(function(){
	orgInit();
	positionInit();
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
        	$("#position_tab").treegrid("load",{oId:r.id})
        },
        columns:[[
            {field:'id',title:'id',hidden:true, width:100},
            {field:'name',title:'组织名称', width:200},
            {field:'code',title:'组织编码', width:100},
            {field:'typeName',title:'组织分类',align:'center',width:100}
        ]]
    });
}

//岗位tree初始化
function positionInit(){
	$("#position_tab").treegrid({
        url: base_url+'/position/getPostionTreeByOrgId',
        method:"get",
        animate:true,
	    idField:'id',  
	    treeField:'name',
	    fitColumns:true,
	    columns:[[    
	        {title:'岗位名称',field:'name',width:100},    
	        {title:'岗位编码',field:'code',width:50}
	    ]],
	    loadFilter:function(res){
	    	resHandle(res)
	    	if(res.code==200){
	    		return res.data
	    	}
	    },
	    queryParams:{
	    	oId:0
	    },
	    onClickRow :function(r){
			$("#role_tab").treegrid("uncheckAll")
        	$("#savePositionRoleBtn").linkbutton("disable")
        	setTimeout(function(){
        		$.ajax({
        			url: base_url + '/role/getRoleIdsByPositionId/'+r.id,
        			dataType: 'json',
        			asnyc:false,
        			success: function (re) {
        				for (var i = 0; i < re.length; i++) {
        					$("#role_tab").treegrid("checkNode",re[i])
        				}
        				$("#role_tab").parent().find(".tree-checkbox").on("click", function () {
        					$("#savePositionRoleBtn").linkbutton("enable")
        				});
        			}
        		});	
        	},100)
        }
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
function savePositionRole(){
	var row = $("#position_tab").treegrid("getSelected")
	var rs = $("#role_tab").treegrid("getCheckedNodes");
	var roleIds = new Array();
	for (var i = 0; i < rs.length; i++) {
		var r = rs[i]
		roleIds.push(r.id)
	}
	$.ajax({
        url: base_url + '/role/savePositionRole',
        method:'post',
        data:{
        	positionId:row.id,
        	roleIds:roleIds.join()
        },
        dataType: 'json',
        success: function (res) {
            msg(res.msg)
        }
    });
}
