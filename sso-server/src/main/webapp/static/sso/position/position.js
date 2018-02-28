$(function(){
    orgInit();
    userInit();
    roleInit();
    positionInit();
    basePosInit();
    positionFormInit();
    positionForm2Init();
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

function basePosInit(){
	$("#basePos").combobox({
		url: base_url +'/position/getBasePositions',
	    method:'get',
	    valueField:'id',
	    textField:'name',
	    onSelect:function(rec){
	      $('#nameAdd').val(rec.name)
	      var o = $('#org_tab').treegrid('getSelected');
	      $('#codeAdd').val(o.code+'_'+rec.code)
	    }
	})
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

function positionForm2Init(){
    $("#positionForm2").form({
        url: base_url+'/position/save',
        success: function (res) {
            var res = JSON.parse(res)
            if (res.code==200) {
                msg("保存成功！");
                $('#position_data2').dialog('close');
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
function position2Submit(){
    $("#positionForm2").form('submit');
}

function addPos(){
	var org = $("#org_tab").treegrid("getSelected");
	if(!org){
		msg("请选择组织添加岗位!")
		return;
	}
	$("basePos").combobox("enable")
	$("#parentPosAdd").combotreegrid("enable");
	$("#positionForm").form('clear');
	renderPositionTree($("#parentPosAdd"))
	
	var po = $("#position_tab").treegrid("getSelected");
	if(po){
	   $("#parentPosAdd").combotreegrid("setValue",{id:po.id,name:po.name})
	}
	
	$("input[name=orgId]").val(org.id);
	$("#orgName").val(org.name);
    $("#position_data").dialog("setTitle","添加岗位").dialog("open");
}

function editPos(){
	var po = $("#position_tab").treegrid("getSelected");
	if(!po){
		msg("请选择岗位!")
		return;
	}
	$("#positionForm2").form('clear');
	$("#positionForm2").form('load',po);
	$.ajax({
        url: base_url + '/position/getPositionById/'+po.id,
        dataType: 'json',
        success: function (re) {
            $("#positionForm2").form("load",re);
            var org = $("#org_tab").treegrid("getSelected");
            $("#nameEdit").val(re.name)
            $("#codeEdit").val(re.code)
            if(re.parentPosition){
	            $("#parentPosEdit").val(re.parentPosition.name);
            }
        }
    });
    $("#position_data2").dialog("setTitle","编辑岗位").dialog("open");
}
