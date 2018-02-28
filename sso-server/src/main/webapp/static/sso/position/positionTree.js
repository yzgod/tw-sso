function renderPositionTree($jq,selectRow){
    $jq.data("flag","1");
    var option = {
        idField: 'id',
        fit: true,
        treeField: 'name',
        fitColumns: true,
        animate: true,
        columns: columns_select,
        onSelect: function (row) {
            if (selectRow)
                selectRow(row);
        },
        loader: function (params, success, error) {
            if ("1" === $jq.data("flag")) {
                $jq.data("flag", "2");
                $.ajax({
                    url: base_url + '/position/getPositionByParentId',
                    dataType: 'json',
                    data :{pId:0},
                    success: function (re) {
                        success(re);
                    }
                });
            } else {
                var pid = params.id;
                $.ajax({
                    url: base_url + '/position/getPositionByParentId',
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
    $jq.combotreegrid(option);
}
var columns_select =[[
            {field: 'id', width: 180, hidden: 'true'},
            {field: 'name', title: '岗位名称', width: 80, align: 'left'}
        ]];