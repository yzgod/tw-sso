/* 启动时加载 */
$(function(){
    renderUgTree($('#ug_tab'), "treegrid",columns_default,selectUg);
    renderUgTree($('#parentUg'), "combotreegrid",columns_select);
    userInit();
    roleInit();
    ugFormInit();
});

function ugFormInit(){
	$("#ugForm").form({  
        url: base_url + '/ug/save',
        onSubmit:function(){
         	return $(this).form("validate")
        },
        success: function (res) {
        	var res = JSON.parse(res)
        	if (res.code==200) {
                msg("保存成功！");
        		$('#ug_data').dialog('close');
        		renderUgTree($('#ug_tab'), "treegrid",columns_default,selectUg);
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
	        	ugId:0
	        }
	    });
}

//角色表初始化
function roleInit(id){
	$("#role_tab").treegrid({
        url: base_url+'/ug/queryRolesByUserGroupIds',
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
        	ugId:0
        }
    });
}

/** 初始化新增用户组 */
function addUg(){
	$("input[name=code]").validatebox({disabled:false})
	$("#ugForm").form('clear');
	$("#ug_data").dialog("setTitle","添加用户组").dialog("open");
}
//提交表单
function ugSubmit(){
	$("#ugForm").form('submit');
}

/*编辑用户组*/
function editUg(){
	var selectRow = $("#ug_tab").datagrid("getSelected");
	if(selectRow != null){
		$("input[name=code]").validatebox({disabled:true})
		showParent(selectRow.parentId);
		$('#ugForm').form('load', selectRow);
		$('#ug_data').dialog('open').dialog('setTitle', '编辑用户组');
	}else{
		$.messager.alert("提示", "请选择要修改的用户组！", 'info');
	}
}
	
/*选择用户组后联动选中其对应的人员*/
function selectUg(row) {
    var ugId = row.id;
    $("#role_tab").treegrid("load",{ugId:ugId})
    $("#user_tab").datagrid("load",{ugId:ugId})
}
	
/* 父用户组回显*/
function showParent(id){
	if(!id){
		id=0;
	}
	$('#parentUg').combotree({
		url:base_url + "/ug/getUgById?id="+id,
		method:"get",
		onLoadSuccess :function(node,res){
			var data = res.data;
			if(!data){
				$("#parentUg").combotree('setValue',{id:0,text:'root'})
			}else{
				$("#parentUg").combotree('setValue',{id:data.id,text:data.name})
			}
		}
	});
}
