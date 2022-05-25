/*处理url跳转的问题*/
$(function () {
    $("#trade_method_table").init();
})
var holiday_table_column=[
    {
        title : '编码',
        field : 'code',
        align:"center",
        width:"150px"
    },
    {
        title : '名称',
        field : 'name',
        align:"center",
        width:"240px"
    },
    {
        title : '地址',
        field : 'url',
        align:"center",
        width:"240px"
    },
    {
        title : '描述',
        field : 'description',
        align:"center",
        width:"240px"
    },
    {
        title : '创建时间',
        field : 'createTime',
        align:"center",
        width:"240px"
    }
]

$('#trade_method_table').bootstrapTable({
    method : 'post',
    url : TRADE_METHOD_LIST_URL,//请求路径
    striped : true, //是否显示行间隔色
    pageNumber : 1, //初始化加载第一页
    pagination : true,//是否分页
    sidePagination : 'server',//server:服务器端分页|client：前端分页
    pageSize : 15,//单页记录数
    pageList : [5,10,20,50,100,200],//可选择单页记录数
    cache: true, //设置缓存
    sortable: true,  //是否启用排序
    sortOrder: "asc", //排序方式
    search: false,                      //是否显示表格搜索
    strictSearch: true,
    showColumns: true,                  //是否显示所有的列（选择显示的列）
    showRefresh: true,                  //是否显示刷新按钮
    clickToSelect: true,                //是否启用点击选中行
    uniqueId: "id",                     //每一行的唯一标识，一般为主键列
    showToggle: true,                   //是否显示详细视图和列表视图的切换按钮
    cardView: false,                    //是否显示详细视图
    toolbar: '#custom-toolbar',
    toolbarAlign: "right",
    showRefresh : true,//刷新按钮
    ajaxOptions:{
        headers: {"Authorization": getToken()}
    },
    queryParams : queryParams,
    responseHandler: handleClientData,
    columns : holiday_table_column
})

/* table查询时参数的方法 */
function queryParams(params) {
    let query= {
        "pageSize" : params.limit, // 每页显示数量
        "pageNum" : (params.offset / params.limit) + 1 //当前页码
    }
    return query;
}
//处理机构返回数据
function handleClientData(res){
    let data= res.data ||[];
    if(data.list==null||data.list.length<1){
        return {
            total: 0,
            rows: []
        };
    }
    return {
        total: data.total,
        rows: data.list
    };
}