/**
 * 获取用户组树形表格
 */
function renderUgTree($jq,type,columns,selectRow){
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
                    url: base_url + '/ug/getUGByParentId',
                    dataType: 'json',
                    data :{pId:0},
                    success: function (re) {
                        success(re);
                    }
                });
            } else {
                var pid = params.id;
                $.ajax({
                    url: base_url + '/ug/getUGByParentId',
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
            {field: 'id', width: 10, hidden: 'true'},
            {field: 'name', title: '用户组名称', width: 100, align: 'left'},
            {field: 'code', title: '编码', width: 80, align: 'left'},
            {field: 'remark', title: '说明', width: 80, align: 'left'}
        ]];
var columns_select =[[
            {field: 'id', width: 180, hidden: 'true'},
            {field: 'name', title: '用户组名称', width: 80, align: 'left'}
        ]];