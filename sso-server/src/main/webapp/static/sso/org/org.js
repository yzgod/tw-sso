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
	$("#orgForm2").form({
        url: base_url+'/org/save',
        onSubmit:function(){
         	return $(this).form("validate")
        },
        success: function (res) {
        	var res = JSON.parse(res)
        	if (res.code==200) {
                msg("保存成功！");
        		$('#org_data2').dialog('close');
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
	$("#orgtype").combobox("enable")
	$("#orgCode").text("组织编码：");
    $("#orgCodeInput").validatebox("destroy");
    setTimeout(function(){
        $("#ogDiv").append($('<input id="orgCodeInput" type="text" name="code" data-options="required:true,validType:[\'orgCodeRepeat\']" class="form-control-150 easyui-validatebox" style="width: 250px" >'));
        var cb = $("#ogDiv .combo")
        if(cb.length!=0){
        	cb.remove();
        }
        $("#orgCodeInput").validatebox({disabled:false});
    },100)
	
	var row = $("#org_tab").treegrid("getSelected");
	$("#orgForm").form('clear');
	if(!row){
        $("#parentorg").combotreegrid("setValue",{id:0,name:"root"})
    }else{
        $("#parentorg").combotreegrid("setValue",{id:row.id,name:row.name})
    }
	$("#org_data").dialog("setTitle","添加组织").dialog("open");
}
//提交表单
function orgSubmit(){
	$("#orgForm").form('submit');
}
//提交表单
function orgSubmit2(){
	$("#orgForm2").form('submit');
}

/*编辑组织*/
function editOrg(){
	var row = $("#org_tab").datagrid("getSelected");
	if(row != null){
		$("#orgForm2 input[name=id]").val(row.id)
		$("#orgForm2 input[name=name]").val(row.name)
		$("#orgTypeEdit").textbox({
		  value:row.orgType.name,
		  readonly:true
		})
		$("#orgCodeEdit").textbox({
		  value:row.code,
		  readonly:true
		})
		if(row.parentId!=0){
    		$.ajax({
                url: base_url + '/org/getOrgById',
                dataType: 'json',
                data :{id:row.parentId},
                success: function (re) {
                    if(re.code==200){
                    	$("#parentOrgEdit").textbox({
                          value:re.data.name,
                          readonly:true
                        })
                    }
                }
            });
		}else{
		    $("#parentOrgEdit").textbox({
                value:"--",
                readonly:true
            })
		}
		
		$("#ordEdit").numberbox({
		  value:row.ord
		})
		$("remarkEdit").numberbox({
		  value:row.remark
		})
		
		$('#org_data2').dialog('open').dialog('setTitle', '编辑组织');
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
		textField:'name',
		onChange:function(n,o){
			if(n != undefined && n!=''){
    			$.ajax({
                    url: base_url + '/org/getOrgTypeById/'+n,
                    dataType: 'json',
                    success: function (re) {
                        if(re.code=='D'){//部门
                            changeOrgCodeDomToBaseDept();
                            $("#nameAdd").validatebox({
                                readonly:true
                            })
                        }
                    }
                });
			}
		}
	});
}

function changeOrgCodeDomToBaseDept(){
    $("#orgCode").text("部门选择：");
    $("#orgCodeInput").combobox({
        url:base_url + '/org/getBaseDept',
        method:'get',
        valueField:'code',    
        textField:'name',
        onSelect:function(rec){
            $("#nameAdd").val(rec.name)
        }
    });
}