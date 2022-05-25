/*处理url跳转的问题*/
var MOCK_TYPE = MOCK_MOCK_TYPE;
$(function () {
    $("#mockDeal_table").init();
})
var mockDeal_table_column=[
    {
        title : '股票代码',
        field : 'code',
        align:"center",
        width:"150px"
    },
    {
        title : '股票名称',
        field : 'name',
        align:"center",
        width:"240px"
    },
    {
        title : '成交时间',
        field : 'dealDate',
        align:"center",
        width:"200px"
    },
    {
        title : '成交类型',
        field : 'dealType',
        align:"center",
        width:"60px",
        formatter: dealTypeFormatter,
    },
    {
        title : '成交数量',
        field : 'dealNum',
        align:"center",
        width:"60px"
    },
    {
        title : '成交价格',
        field : 'dealPrice',
        align:"center"
    },
    {
        title : '成交金额',
        field : 'dealMoney',
        align:"center"
    },
    {
        title : '成交编号',
        field : 'dealCode',
        align:"center",
        width:"200px"
    },
    {
        title : '委托编号',
        field : 'entrustCode',
        align:"center",
        width:"200px"
    },
    {
        title : '成交方式',
        field : 'entrustType',
        align:"center",
        width:"60px",
        formatter: typeFormatter,
    },

    {
        title:"操作",
        field : 'operation',
        align:"center",
        formatter: operationFormatter,
        events: "operationEvents"
    }
]

$('#mockDeal_table').bootstrapTable({
    method : 'post',
    url : TRADE_DEAL_LIST,//请求路径
    striped : true, //是否显示行间隔色
    pageNumber : 1, //初始化加载第一页
    pagination : true,//是否分页
    sidePagination : 'client',//server:服务器端分页|client：前端分页
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
    columns : mockDeal_table_column
})

/* table查询时参数的方法 */
function queryParams(params) {
    let query= {
        "pageSize" : params.limit, // 每页显示数量
        "pageNum" : (params.offset / params.limit) + 1, //当前页码,
        "mockType":MOCK_TYPE
    }
   return query;
}
//处理机构返回数据
function handleClientData(res){
    let data= res.data ||[];
    return data;
}

function dealTypeFormatter(value){
    return value==1?"买":"卖";
}
function typeFormatter(value){
    return value==1?"手动":"自动";
}
/* 给每一行增加操作按钮 */
function operationFormatter(value, row, index) {
    return [
        '<a class="info text-primary" href="javascript:void(0)" data-toggle="tooltip" title="查看委托信息">',
        '<i class="fa fa-info"></i>&nbsp;查看委托信息&nbsp;&nbsp;</a>',
    ].join('');
}

///* 给操作按钮增加点击事件 */
window.operationEvents={
    //撤销委托信息
    'click .info' : function(e, value, row, index) {

    }
};

