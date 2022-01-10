
$(function () {
    $("#mockHisEntrust_table").init();
})
var mockHisEntrust_table_column=[
    {
        field : "id",
        align : "center",
        visible: false
    },
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
        title : '委托时间',
        field : 'entrustDate',
        align:"center",
        width:"200px"
    },
    {
        title : '交易类型',
        field : 'dealType',
        align:"center",
        width:"200px",
        formatter: dealTypeFormatter,
    },
    {
        title : '交易数量',
        field : 'entrustNum',
        align:"center",
        width:"200px"
    },
    {
        title : '交易价格',
        field : 'entrustPrice',
        align:"center",
        width:"200px"
    },
    {
        title : '交易的状态',
        field : 'entrustStatus',
        align:"center",
        width:"200px",
        formatter: statusFormatter,
    },
    {
        title : '委托编号',
        field : 'entrustCode',
        align:"center",
        width:"200px"
    },
    {
        title : '委托费用',
        field : 'entrustMoney',
        align:"center",
        width:"200px"
    },
    {
        title : '手续费',
        field : 'handMoney',
        align:"center",
        width:"200px"
    },
    {
        title : '总的费用',
        field : 'totalMoney',
        align:"center",
        width:"200px"
    },
    {
        title : '委托方式',
        field : 'entrustType',
        align:"center",
        width:"200px",
        formatter: typeFormatter,
    }
]

$('#mockHisEntrust_table').bootstrapTable({
    method : 'post',
    url : "tradeEntrust/history",//请求路径
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
    columns : mockHisEntrust_table_column
})

/* table查询时参数的方法 */
function queryParams(params) {
    let query= {
        "pageSize" : params.limit, // 每页显示数量
        "pageNum" : (params.offset / params.limit) + 1, //当前页码,
        "mockType":1
    }
   return query;
}
//处理机构返回数据
function handleClientData(res){
    let data= res.data.result ||[];
    return data;
}

function dealTypeFormatter(value){
    return value==1?"买":"卖";
}
function statusFormatter(value){
    if(value==1){
        return "委托中";
    }else if(value==2){
        return "成交";
    }else{
        return "已撤销";
    }
}
function typeFormatter(value){
    return value==1?"手动":"自动";
}