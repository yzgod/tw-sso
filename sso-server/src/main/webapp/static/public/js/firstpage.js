
/*function showPlantList(){
	$("#plant_list_tab").datagrid({
		 url:getRootPath__()+'/fistPages/getPlantList' ,
		    checkOnSelect: true,
	        pagination:true,
			pageSize:10,
			pageNumber:1,
	        fitColumns:true,
	        singleSelect:true,
	        fit:true,
	        rownumbers:true,
	        onDblClickCell:function(i,f,v){
	        	var row = $("#plant_list_tab").datagrid('getSelected');
	        	var titles=row.powername;
	        	var plantId=row.id;
	        	var providerType=row.providerType;
	        	var urls= '/power_manage/single_station/detailStation?plantId='+plantId+'&providerType='+providerType;
	        	top.addTab(titles,urls);
	        },
		    columns:[[    
		        {field:'id',title:'电站信息ID',hidden:true, width:100},
		        {field:'userId',title:'业主ID',hidden:true, width:100},
		        {field:'orgid',title:'公司ID',hidden:true, width:100},
		        {field:'powername',title:'电站名称',halign:'center',sortable:true,width:100},    
		        {field:'installedcapacity',title:'装机容量(KW)',sortable:true,align:'center',width:100},    
		        {field:'electric',title:'今日发电量(KW.h)',sortable:true,align:'center',width:100},
		        {field:'total_electric',title:'累计发电量(KW.h)',sortable:true,align:'center',width:100},
		        // {field:'currentPower',title:'当前功率(Kw)',sortable:true,align:'center',width:100},
		        {field:'putproductiontime',title:'投产发电时间',sortable:true,align:'center',width:100,
	         		formatter: function(value,row,index){
	         			if(value!=null){
	         				var time=getTime(value).toString().substring(0,10);
	         			}else{
	         				var time="";
	         			}
	         			return time;
	         		}
                },    
		        {field:'iconcls',title:'电站图片',align:'center',width:100,
	         		formatter: function(value,row,index){
	         			var imgName=row.powername;
	         			if(value!=null && value!=""){
	         				var image=value.substring(1,value.length - 1);
	         			}else{
	         				    image="";
	         			}
	         			return  "<a href='#' onclick=\"openWin('"+imgName+"','"+image+"')\"><span class='iconfont icon-tupian'></span></a>";
	         		}
		        }
		    ]]    
		});
	}


function openWin(title,image){
	$('#win').window({    
	    width:650,
	    height:500,
	    title:title,
	    closed:true,
	    modal:true   
	});
	if(image==undefined || image==""){
		$.messager.alert("提示","未上传图片","warning");
	}else{
		var path =image.split("\/");
		var imgName=path[path.length-1];
        var imgPath=getRootPath__()+"/fistPages/img?imgName="+imgName;
	    $("#stationImg").attr('src',imgPath);
		$("#win").window("open");
	}
}*/
function getData(params,callback) {
    var url = getRootPath__() + "/produce_inf_report";
        url += "/month_produce_inf_report/produceInfReport";
    $.ajax({
        url: url,
        method: "post",
        data: params,
        dataType: "json",
        success: function (re) {
            callback(re);
        }
    });
}
//发电信息
function dayPlantInfo(callback){
	 $.ajax({
	     url: getRootPath__() + '/fistPages/getPlantInfo',
	     dataType: 'json',
	     complete: function (data) {
	        if(data.status==200){
	        	callback(data.responseJSON)
	        }
	     }
	 });

}

/*//下拉框初始化
function comboxSelect(id){
    $('#select_org').combobox({
        url:getRootPath__() +"/sys_manage/org_manage/getOrgList?pid="+id,
        valueField:'id',      
        textField:'orgname',
        editable:false,
        onSelect:function(rec){
            $("#select_area").combobox('readonly',false);
        	var url=getRootPath__() +"/sys_manage/area_manage/getAreaList?orgId="+rec.id;
        	$('#select_area').combobox('reload',url);
		}
    });
}


//条件查询
function searchPlantList(){
    var powername = $('#search_plant').val(); //公司名称
    var orgId=$('#select_org').combobox('getValue');//公司ID
    var areaId=$('#select_area').combobox('getValue');//区域ID
	var upElectric=$('#upElectric').val();//今日发电量起始值
	var downElectric=$('#downElectric').val();//今日发电量结束值
	if(upElectric!=""){
        isNum(upElectric,'今日发电量起始值');
	}
	if(downElectric!=""){
        isNum(downElectric,'今日发电量结束值');
	}
	if(upElectric!="" && downElectric!=""){
        if(parseFloat(downElectric)<parseFloat(upElectric)){
        	msg('今日发电量结束值不能小于起始值');
			return;
		}
	}
    $('#plant_list_tab').datagrid('options').url=getRootPath__()+'/fistPages/getPlantList';
	$('#plant_list_tab').datagrid('reload',{
		powername:powername,
		orgId:orgId,
		areaId:areaId,
        upElectric:upElectric,
        downElectric:downElectric
	}); 
}
function isNum(t,text) {
    s =  $.trim(t);
    var p2=/^[0-9]+\.?[0-9]*$/;
    //var p =/^[1-9](\d+(\.\d{1,2})?)?$/;
   // var p1=/^[0-9](\.\d{1,2})?$/;
    if(!p2.test(s)){
        msg(text+'必须为数字！');
        return;
	}
}
//重置查询
function reset(id){
    $('#upElectric').val("");
    $('#downElectric').val("");
    $('#search_plant').val(""); //公司名称
      comboxSelect(id);
    $("#select_area").combobox('clear');
    $("#select_area").combobox('readonly',true);
    $("#plant_list_tab").datagrid("load", {});

}*/
