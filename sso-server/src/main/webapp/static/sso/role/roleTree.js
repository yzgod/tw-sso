/**
 * 获取角色树形表格
 */
function renderRoleTree($jq,appCode,selectRow){
    $jq.data("flag","1");
    var option = {
        idField: 'id',
        fit: true,
        treeField: 'name',
        fitColumns: true,
        animate: true,
        columns: columns_select,
        loader: function (params, success, error) {
            if ("1" === $jq.data("flag")) {
                $jq.data("flag", "2");
                $.ajax({
                    url: base_url + '/role/getRoleByParentId',
                    dataType: 'json',
                    data :{appCode:appCode,pId:0},
                    success: function (re) {
                    	re.unshift({id:0,name:'root',state:"open"})
                        success(re);
                    }
                });
            } else {
                var pid = params.id;
                $.ajax({
                    url: base_url + '/role/getRoleByParentId',
                    data :{appCode:appCode,pId:pid},
                    dataType: 'json',
                    success: function (re) {
                        success(re);
                    }
                });
            }
        },
        loadFilter: function (data) {
            $.each(data, function (i, v) {
            	if(v.id!=0){
	                v.state = 'closed';
            	}
            });
            return data;
        }
    };
    $jq.combotreegrid(option);
}
var columns_select =[[
            {field: 'id', width: 180, hidden: 'true'},
            {field: 'name', title: '角色名称', width: 80, align: 'left'}
        ]];