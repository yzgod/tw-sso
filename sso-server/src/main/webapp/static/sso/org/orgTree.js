/**
 * 获取组织架构树形表格
 */
function renderOrgTree($jq,type,columns,selectRow){
    $jq.data("flag","1");
    var option = {
        idField: 'id',
        fit: true,
        treeField: 'name',
        fitColumns: true,
        animate: true,
        columns: columns,
        onSelect: function (row) {
            if (selectRow)
                selectRow(row);
        },
        loader: function (params, success, error) {
            if ("1" === $jq.data("flag")) {
                $jq.data("flag", "2");
                $.ajax({
                    url: base_url + '/org/getOrgByParentId',
                    dataType: 'json',
                    data :{pId:0},
                    success: function (re) {
                        success(re);
                    }
                });
            } else {
                var pid = params.id;
                $.ajax({
                    url: base_url + '/org/getOrgByParentId',
                    data: {
                        pId: pid
                    },
                    dataType: 'json',
                    success: function (re) {
                        success(re);
                    }
                });
            }
        },
        loadFilter: function (data) {
            $.each(data, function (i, v) {
                v.state = 'closed';
            });
            return data;
        }
    };
    if(type==="treegrid"){
        $jq.treegrid(option);
    }else if(type==="combotreegrid"){
        $jq.combotreegrid(option);
    }
}

var columns_default =  [[
            {field: 'id', width: 180, hidden: 'true'},
            {field: 'name', title: '组织名称', width: 80, align: 'left'},
            {field: 'code', title: '编码', width: 80, align: 'left'},
            {field: 'orgType', title: '类型', width: 80, align: 'left',
           			formatter:function(v,r,i){
						return v.name;
					}
			}
        ]];
var columns_select =[[
            {field: 'id', width: 180, hidden: 'true'},
            {field: 'name', title: '组织名称', width: 80, align: 'left'}
        ]];