/* 启动时加载 */
$(function(){
	initOrgType();  //初始化组织类别
    renderOrgTree($('#org_tab'), "treegrid",columns_default,selectOrg);
    renderOrgTree($('#parentorg'), "combotreegrid",columns_select);
    userInit();
    roleInit();
    orgFormInit();
});

function orgFormInit(){
	$("#orgForm").form({
        url: base_url+'/org/save',
        onSubmit:function(){
         	return $(this).form("validate")
        },
        success: function (res) {
        	var res = JSON.parse(res)
        	if (res.code==200) {
                msg("保存成功！");
        		$('#org_data').dialog('close');
        		renderOrgTree($('#org_tab'), "treegrid",columns_default,selectOrg);
        	}else{
        		msg(res.msg);
        	}
        }
    })
}

//人员表初始化
function userInit(){
	 $("#user_tab").datagrid({
	        url: base_url+'/user/queryPage',
	        method:'get',
	        checkOnSelect: true,
	        pagination:true,
			pageSize:20,
			pageNumber:1,
            fitColumns:true,
            singleSelect:true,
            rownumbers:true,
            fit:true,
	        columns: [[               
	            { field: 'id', hidden: 'true'},
	         	{ field: 'loginName', title: '账号',width:100},
	         	{ field: 'realName', title: '姓名',width:100},
	         	{ field: 'cellPhone', title: '手机号码',width:100},
	         	{ field: 'forbidden', title: '状态',width:100,
	         		formatter:function(v){
	         			return v?"禁用":"启用"
	         		}
	         	}
	        ]],
	        queryParams :{
	        	oId:0
	        }
	    });
}

//角色表初始化
function roleInit(){
	$("#role_tab").treegrid({
        url: base_url+'/org/queryRolesByOrgId',
        method:"get",
        animate:true,
	    idField:'id',  
	    treeField:'name',
	    fitColumns:true,
	    columns:[[    
	        {title:'应用/角色名称',field:'name',width:200},    
	        {title:'角色编码',field:'code',width:60},    
	        {title:'应用编码',field:'appCode',width:60}
	    ]],
	    loadFilter:function(res){
	    	resHandle(res)
	    	if(res.code==200){
	    		return res.data
	    	}
	    },
	    queryParams :{
	        oId:0
	    }
    });
}

/** 初始化新增组织 */
function addOrg(){
	$("input[name=code]").validatebox({disabled:false})
	$("#orgForm").form('clear');
	$("#org_data").dialog("setTitle","添加组织").dialog("open");
}
//提交表单
function orgSubmit(){
	$("#orgForm").form('submit');
}

/*编辑组织*/
function editOrg(){
	var selectRow = $("#org_tab").datagrid("getSelected");
	if(selectRow != null){
		$("input[name=code]").validatebox({disabled:true})
		showParentOrg(selectRow.parentId);
		$('#orgForm').form('load', selectRow);
		$('#org_data').dialog('open').dialog('setTitle', '编辑组织');
	}else{
		$.messager.alert("提示", "请选择要修改的组织！", 'info');
	}
}
	
/*选择公司后联动选中其对应的人员*/
function selectOrg(row) {
    var orgId = row.id;
    $("#user_tab").datagrid("load",{oId:orgId})
    $("#role_tab").treegrid("load",{oId:orgId});
}
	
/*初始化组织类别*/
function initOrgType(){
	$('#orgtype').combobox({
		url:base_url + "/org/getOrgType",
		method:'get',
		valueField:'id',
		textField:'name'
	});
}

/* 父组织回显*/
function showParentOrg(id){
	if(!id){
		id=0;
	}
	$('#parentorg').combotree({
		url:base_url + "/org/getOrgById?id="+id,
		method:"get",
		onLoadSuccess :function(node,res){
			var data = res.data;
			if(!data){
				$("#parentorg").combotree('setValue',{id:0,text:'root'})
			}else{
				$("#parentorg").combotree('setValue',{id:data.id,text:data.name})
			}
		}
	});
}
