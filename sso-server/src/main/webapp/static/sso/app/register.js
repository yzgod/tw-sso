$(function(){
	appInit();
	appFormInit();
})

function appInit(){
    $("#app_tab").datagrid({
        url:base_url+'/app/getAll',
        method:"get",
        checkOnSelect: true,
        fitColumns:true,
        singleSelect:true,
        fit:true,
        rownumbers:true,
        onClickRow :function(i,r){
        },
        columns:[[
            {field:'id',title:'id',hidden:true, width:100},
            {field:'name',title:'应用名称', width:100},
            {field:'appCode',title:'应用编码', width:30},
            {field:'author',title:'负责人',width:30},
            /*{field:'location',title:'部署地址',width:100},*/
            /*{field:'alertEmail',title:'报警邮件',width:100},
            {field:'isAlert',title:'报警开启',align:"center",width:30,formatter:appStatusFmt},*/
            {field:'remark',title:'备注',width:100}
        ]]
    });
}

function appFormInit(){
	$("#appForm").form({  
        url: base_url + '/app/save',
        onSubmit:function(){
         	return $(this).form("validate")
        },
        success: function (res) {
        	var res = JSON.parse(res)
        	if (res.code==200) {
        		$("#app_tab").datagrid("reload");
        		$('#app_data').dialog('close');
        	}
        	msg(res.msg);
        }
    })
}

function addApp(){
	$("input[name=appCode]").validatebox({disabled:false})
	$("#appForm").form('clear');
	$("#app_data").dialog("setTitle","添加应用").dialog("open");
}

function editApp(){
	var row = $("#app_tab").datagrid("getSelected")
	if(!row){
		msg("请选择应用!")
	}
	$("#appForm").form('clear');
	$("#appForm").form('load',row);
	$("input[name=appCode]").validatebox({disabled:true})
	$("#app_data").dialog("setTitle","编辑应用").dialog("open");
}

function appSubmit(){
	$("#appForm").form('submit');
}

//删除
function deleteApp(){
	var row = $("#app_tab").datagrid("getSelected")
	if(row==null || row.id<0){
		msg("请选择需要删除的应用!")
		return;
	}
	$.ajax({
        url: base_url + '/app/delete/'+row.id,
        dataType: 'json',
        success: function (res) {
            if(res.code==200){
	            $("#app_tab").datagrid("reload")//刷新
            }
            msg(res.msg)
        }
    });
}

function appStatusFmt(value,row,index){
	if (value){
		return "<span title='预警开启,单击关闭' onclick='enableAlert()' class='iconfont icon-chenggong' style='color:#1AE61A'></span>";
	} 
	return "<span title='预警关闭,单击开启' onclick='enableAlert()' class='iconfont icon-iconset0187' style='color:red'></span>";
}

function enableAlert(){
	var row = $("#app_tab").datagrid("getSelected")
	$.ajax({
        url: base_url + '/app/alert/'+row.id+"/"+ (row.isAlert?0:1) ,
        dataType: 'json',
        success: function (re) {
            if(re.code==200){
            	$("#app_tab").datagrid("reload");
            }
            msg(re.msg)
        }
    });
}


