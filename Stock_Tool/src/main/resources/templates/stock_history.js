
//展示的查看历史的股票代码
var selectedCode="";
var clickRow=null;

/**
 * 页面刚进来时，初使用
 */
$(function () {
    $("#stock_selected_yes_table").init();
    $("#stock_history_table").init();
})
/**********************************股票历史记录信息******************************************/

let stock_history_table_column=[
    {
        title : '日期',
        field : 'currDate',
        align:"center",
        width:"240px",
        sortable:true
    },
    {
        title : '股票代码',
        field : 'code',
        align:"center",
        width:"100px"
    },
    {
        title : '名称',
        field : 'name',
        align:"center"
    },
    {
        title : '收盘价',
        field : 'closingPrice',
        align:"center",
    },
    {
        title : '最高价格',
        field : 'highestPrice',
        align:"center"
    },
    {
        title : '最低价格',
        field : 'lowestPrice',
        align:"center"
    },
    {
        title : '开盘价',
        field : 'openingPrice',
        align:"center"
    },
    {
        title : '昨天的收盘价',
        field : 'yesClosingPrice',
        align:"center",
    },
    {
        title : '涨跌额',
        field : 'amplitude',
        align:"center"
    },
    {
        title : '涨跌幅',
        field : 'amplitudeProportion',
        align:"center",
    },
    {
        title : '成交量',
        field : 'tradingVolume',
        align:"center"
    },
    {
        title : '成交金额',
        field : 'tradingValue',
        align:"center",
    }
]

let stock_selected_yes_table_column=[
    {
        title : '日期',
        field : 'currDate',
        align:"center",
        width:"240px",
        sortable:true
    },
    {
        title : '股票代码',
        field : 'code',
        align:"center",
        width:"100px"
    },
    {
        title : '名称',
        field : 'name',
        align:"center"
    },
    {
        title : '收盘价',
        field : 'closingPrice',
        align:"center",
    },
    {
        title : '最高价格',
        field : 'highestPrice',
        align:"center"
    },
    {
        title : '最低价格',
        field : 'lowestPrice',
        align:"center"
    },
    {
        title : '开盘价',
        field : 'openingPrice',
        align:"center"
    },
    {
        title : '昨天的收盘价',
        field : 'yesClosingPrice',
        align:"center",
    },
    {
        title : '涨跌额',
        field : 'amplitude',
        align:"center"
    },
    {
        title : '涨跌幅',
        field : 'amplitudeProportion',
        align:"center",
    },
    {
        title : '成交量',
        field : 'tradingVolume',
        align:"center"
    },
    {
        title : '成交金额',
        field : 'tradingValue',
        align:"center",
    },
    {
        title:"操作",
        field : 'operation',
        align:"center",
        formatter: operationFormatter,
        events: "operationEvents"
    }
]



$('#stock_selected_yes_table').bootstrapTable({
    method : 'post',
    url : STOCK_SELECTED_YES_LIST_URL,//请求路径
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
    toolbar: '#custom-his_custom-toolbar',
    toolbarAlign: "right",
    showRefresh : true,//刷新按钮
    queryParams : queryYesParams,
    responseHandler: handleYesHistoryClientData,
    columns : stock_selected_yes_table_column
})


/*股票的历史展示信息*/
/* table查询时参数的方法 */
function queryYesParams(params) {
    let query= {
        "pageSize" : params.limit, // 每页显示数量
        "pageNum" : (params.offset / params.limit) + 1
    }
    return query;
}
//处理机构返回数据
function handleYesHistoryClientData(res){
    let data= res.data || [];
    return {
        total: data.total,
        rows: data.list
    }
}

/* 给每一行增加操作按钮 */
function operationFormatter(value, row, index) {
    return [
        '<a class="history text-primary" href="javascript:void(0)" data-toggle="tooltip" title="查看基金的历史">',
        '<i class="fa fa-history"></i>&nbsp;查看历史&nbsp;&nbsp;</a>'
    ].join('');
}

///* 给操作按钮增加点击事件 */
window.operationEvents={
    //查询股票具体信息
    'click .history' : function(e, value, row, index) {
        //处理信息，并展示.
        showHistory(row.code);
        $("#history_stock_name").text(row.name);
        clickRow=row;
        $("#history_popup").modal('show');
    }
};

/***-------------查询股票的历史记录信息------------------***/
function showHistory(code){
    selectedCode=code;
    //刷新表格
    $("#stock_history_table").bootstrapTable('refresh', '{silent: true}');
}

$('#stock_history_table').bootstrapTable({
    method : 'post',
    url : STOCK_HISTORY_LIST_URL,//请求路径
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
    toolbar: '#custom-his_custom-toolbar',
    toolbarAlign: "right",
    showRefresh : true,//刷新按钮
    queryParams : queryHistoryParams,
    responseHandler: handleHistoryClientData,
    columns : stock_history_table_column
})


/*股票的历史展示信息*/
/* table查询时参数的方法 */
function queryHistoryParams(params) {
    let query= {
        "pageSize" : params.limit, // 每页显示数量
        "pageNum" : (params.offset / params.limit) + 1, //当前页码
        "code":selectedCode,
        "startDate":$("#historyStartDate").val(),
        "endDate":$("#historyEndDate").val()
    }
    return query;
}
//处理机构返回数据
function handleHistoryClientData(res){
    let data= res.data;
    return {
        total: data.total,
        rows: data.list
    }
}
$("#historyDateRange").daterangepicker({
        showDropdowns: true,
        autoUpdateInput: false,
        "locale": {
            format: 'YYYY-MM-DD',
            applyLabel: "应用",
            cancelLabel: "取消",
            resetLabel: "重置",
        }
    },
    function(start, end, label) {
        if(!this.startDate){
            this.element.val('');
        }else{
            this.element.val(this.startDate.format(this.locale.format));
        }
    }
);
$('#historyDateRange').on('apply.daterangepicker', function(ev, picker) {
    $("#historyStartDate").val(picker.startDate.format('YYYY-MM-DD'));
    $("#historyEndDate").val(picker.endDate.format('YYYY-MM-DD'));
    $("#stock_history_table").bootstrapTable('refresh', '{silent: true}');
});
//更改选取器的选定日期范围
var now = new Date(new Date().setDate(new Date().getDate() + 0));
var oneMonthBefore = new Date(new Date().setDate(new Date().getDate() -30));
$('#historyDateRange').data('daterangepicker').setStartDate(oneMonthBefore);
$('#historyDateRange').data('daterangepicker').setEndDate(now);
$("#historyDateRange").val(formatDate(oneMonthBefore)+"/"+formatDate(now));
$("#historyStartDate").val(formatDate(oneMonthBefore));
$("#historyEndDate").val(formatDate(now));