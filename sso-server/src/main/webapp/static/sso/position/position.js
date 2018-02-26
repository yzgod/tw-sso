/* 启动时加载 */
$(function(){
    orgInit();
    userInit();
    roleInit();
    positionInit();
    positionFormInit();
});

//组织机构初始化
function orgInit(){
	 $("#org_tab").treegrid({
	        url: base_url+'/org/getOrgTree',
	        method:"get",
	        idField: 'id',
	        fit: true,
	        treeField: 'name',
	        fitColumns: true,
	        animate: true,
	        singleSelect:true,
	        columns: [[               
	            { field: 'id', hidden: 'true'},
	         	{ field: 'name', title: '组织名称',width:170},
	         	{ field: 'typeName', title: '类型',width:100}
	        ]],
	        onClickRow :function(row){
	        	var oId = row.id
	        	$("#position_tab").treegrid("load",{oId:oId})
	        	$('#role_tab').treegrid('loadData',{code:200,data:{}})//清空
	        	$('#user_tab').datagrid('loadData',{rows:{}})//清空
	        }
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
	    onClickRow :function(row){
        	var posId = row.id
        	$("#role_tab").treegrid("load",{posId:posId})
        	$("#user_tab").datagrid("load",{posId:posId})
        }
    });
}

//人员表初始化
function userInit(){
	 $("#user_tab").datagrid({
	        url: base_url+'/user/queryPage',
	        method:"get",
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
	         	{ field: 'cellPhone', title: '手机号码',width:150},
	         	{ field: 'forbidden', title: '状态',width:100,
	         		formatter:function(v){
	         			return v?"禁用":"启用"
	         		}
	         	}
	        ]],
	        queryParams :{
	        	posId:0
	        }
	    });
}

//岗位有效角色tree初始化
function roleInit(){
	$("#role_tab").treegrid({
        url: base_url+'/position/queryRolesByPosId',
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
        	posId:0
        }
    });
}

function positionFormInit(){
    $("#positionForm").form({
        url: base_url+'/position/save',
        onSubmit:function(){
            return $(this).form("validate")
        },
        success: function (res) {
            var res = JSON.parse(res)
            if (res.code==200) {
                msg("保存成功！");
                $('#position_data').dialog('close');
                $("#position_tab").treegrid("reload")
            }else{
                msg(res.msg);
            }
        }
    })
}

//提交表单
function positionSubmit(){
    $("#positionForm").form('submit');
}

function addPos(){
//    $("#orgtype").combobox("enable")
    $("#position_data").dialog("setTitle","添加岗位").dialog("open");
}

function editPos(){
//    $("#orgtype").combobox("enable")
    $("#position_data").dialog("setTitle","编辑岗位").dialog("open");
}
