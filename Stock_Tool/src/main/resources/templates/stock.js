
//展示的查看历史的股票代码
var selectedCode="";
var clickRow=null;

/**
 * 页面刚进来时，初使用
 */
$(function () {
    //将其清空
    $("#codeSync").hide();
    $("#stock_table").init();
    $("#stock_history_table").init();
})


/**
 *
 * 表格展示信息
 */

let stock_table_column=[
    {
        title : '股票代码',
        field : 'code',
        align:"center",
        width:"100px"
    },
    {
        title : '股票名称',
        field : 'name',
        align:"center",
        width:"100px"
    },
    {
        title : '股票类型',
        field : 'exchange',
        align:"center",
        formatter: exchangeFormat
    },
    {
        title : '股票代码全称',
        field : 'fullCode',
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

$('#stock_table').bootstrapTable({
    method : 'post',
    url : STOCK_LIST_URL,//请求路径
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
    queryParams : queryParams,
    responseHandler: handleClientData,
    columns : stock_table_column
})

/* table查询时参数的方法 */
function queryParams(params) {
    let keyword = $("#keywords").val();
    let query= {
        "pageSize" : params.limit, // 每页显示数量
        "pageNum" : (params.offset / params.limit) + 1, //当前页码
        "keyword":keyword
    }
   return query;
}
//处理机构返回数据
function handleClientData(res){
    let data= res.data;
    if(data.list==null||data.list.length<1){
        //将其清空
        $("#codeSync").show();
        return [];
    }else{
        $("#codeSync").hide();
    }
    return {
        total: data.total,
        rows: data.list
    };
}
function exchangeFormat(value){
    let message ="";
    switch(value){
        case 0:{
            message = "深圳交易所股票";
            break;
        }
        case 1:{
            message = "上海交易所股票";
            break;
        }
        case 2:{
            message = "北京交易所股票";
            break;
        }
        default:{

            break;
        }
    }
    return message;
}


$("#keywords").blur(function(){
    $("#stock_table").bootstrapTable('refresh', '{silent: true}');
})

/* 给每一行增加操作按钮 */
function operationFormatter(value, row, index) {
    return [
        '<a class="info text-primary" href="javascript:void(0)" data-toggle="tooltip" title="查看股票具体信息">',
        '<i class="fa fa-info"></i>&nbsp;查看详细信息&nbsp;&nbsp;</a>',
        '<a class="history text-primary" href="javascript:void(0)" data-toggle="tooltip" title="查看基金的历史">',
        '<i class="fa fa-history"></i>&nbsp;查看历史&nbsp;&nbsp;</a>',
        '<a class="selfSelect text-primary" href="javascript:void(0)" data-toggle="tooltip" title="加入自选">',
        '<i class="fa fa-history"></i>&nbsp;加入自选&nbsp;&nbsp;</a>',
        '<a class="min text-danger" href="javascript:void(0)" data-toggle="tooltip" title="查看分钟K线">',
        '<i class="fa fa-line-chart"></i>&nbsp;分钟k线&nbsp;&nbsp;</a>',
        '<a class="daily text-danger" href="javascript:void(0)" data-toggle="tooltip" title="查看天K线">',
        '<i class="fa fa-line-chart"></i>&nbsp;天k线&nbsp;&nbsp;</a>',
        '<a class="weekly text-danger" href="javascript:void(0)" data-toggle="tooltip" title="查看周K线">',
        '<i class="fa fa-line-chart"></i>&nbsp;周k线&nbsp;&nbsp;</a>',
        '<a class="monthly text-danger" href="javascript:void(0)" data-toggle="tooltip" title="查看月K线">',
        '<i class="fa fa-line-chart"></i>&nbsp;月k线&nbsp;&nbsp;</a>',
    ].join('');
}

///* 给操作按钮增加点击事件 */
window.operationEvents={
    //查询股票具体信息
    'click .info' : function(e, value, row, index) {
        //处理信息，并展示.
        showInfo(row.code);
        $("#info_popup").modal('show');
    },
    //查询股票具体信息
    'click .history' : function(e, value, row, index) {
        //处理信息，并展示.
        showHistory(row.code);
        $("#history_stock_name").text(row.name);
        clickRow=row;
        $("#history_popup").modal('show');
    },
    //加入自选
    'click .selfSelect' : function(e, value, row, index) {
        new $.flavr({
            content     : "您确定将该股票【"+row.name+"】加入自选吗 ？",
            dialog      : 'confirm',
            onConfirm   : function(){
                addSelfSelect(row.code);
            }
        });
    },

    //查看分钟线
    'click .min' : function(e, value, row, index) {
        //处理信息，并展示.
        showKInfo(row.fullCode,1);
        $("#min_popup").modal('show');
    },
    //查看天钟线
    'click .daily' : function(e, value, row, index) {
        //处理信息，并展示.
        showKInfo(row.fullCode,2);
        $("#daily_popup").modal('show');
    },
    //查看周线
    'click .weekly' : function(e, value, row, index) {
        //处理信息，并展示.
        showKInfo(row.fullCode,3);
        $("#weekly_popup").modal('show');
    },
    //查看月线
    'click .monthly' : function(e, value, row, index) {
        //处理信息，并展示.
        showKInfo(row.fullCode,4);
        $("#monthly_popup").modal('show');
    }
};

/*----------------查看股票的 info 简单信息 -------------------**/
/*股票的查看信息相关接口*/
function showInfo(code){
    //将以前的信息清空
    $("#showInfo").find(".info").text("");
    //获取这个股票的相关信息
    let stockInfo=getStockInfo(code);
    //进行展示股票的相关信息
    fillShowInfo(stockInfo);
}

function fillShowInfo(stockInfo){
    $("#code").text(stockInfo.code);
    $("#name").text(stockInfo.name);
    $("#openingPrice").text(stockInfo.openingPrice);
    $("#yesClosingPrice").text(stockInfo.yesClosingPrice);
    $("#highestPrice").text(stockInfo.highestPrice);
    $("#lowestPrice").text(stockInfo.lowestPrice);
    $("#closingPrice").text(stockInfo.closingPrice);
    $("#nowPrice").text(stockInfo.nowPrice);
    $("#tradingVolume").text(stockInfo.tradingVolume);
    $("#tradingValue").text(stockInfo.tradingValue);
    $("#amplitude").text(stockInfo.amplitude);
    $("#amplitudeProportion").text(stockInfo.amplitudeProportion);
    $("#peRatio").text(stockInfo.peRatio);
}

/**
 * 股票股票信息的接口
 * @param code
 */
function getStockInfo(code){
    const info = getFillInfo(code);
    let returnData;
    $.ajax({
        async:false,
        type:"post",
        url:STOCK_CRAWLER_INFO_URL,
        data:JSON.stringify(info),
        dataType:"json",
        contentType:"application/json;charset=utf-8",
        success:function(data){
            returnData= data.data;
        }
    });
    return returnData;
}


/***-------------查询股票的历史记录信息------------------***/
function showHistory(code){
    selectedCode=code;
    //刷新表格
    $("#stock_history_table").bootstrapTable('refresh', '{silent: true}');
}

/**
 * 获取填充的用户信息
 * @param code
 * @returns 返回填充信息的对象
 */
function getFillInfo(code,type,exchage){
    const info = {
        "code": code,
        "type":type,
        "exchange":exchage
    };
    return info;
}

/*********************************展示 K线的相关信息***********************************************/
/**
 * 1 为分钟
 * 2 为天
 * 3为周
 * 4为月
 *
 * */
let base64_gif="data:image/gif;base64,";
/*查看K线的相关操作*/
function showKInfo(code,type){
    //将以前的信息清空
    let url=STOCK_KLINE_URL;
    //获取这个股票的相关信息
    const info = getFillInfo(code,type);
    let base64Result=getStockK(info,url);
    //进行展示股票的K线图
    let base64Str=base64_gif+base64Result;
    switch (type) {
        case 2:{
            $("#k_daily").attr("src",base64Str);
            break;
        }
        case 3:{
            $("#k_weekly").attr("src",base64Str);
            break;
        }
        case 4:{
            $("#k_monthly").attr("src",base64Str);
            break;
        }
        default:{
            $("#k_min").attr("src",base64Str);
            break;
        }
    }
}
/**
 * 股票股票信息的接口
 * @param code
 */
function getStockK(info,url){
    let returnData;
    $.ajax({
        async:false,
        type:"post",
        url:url,
        data:JSON.stringify(info),
        dataType:"json",
        contentType:"application/json;charset=utf-8",
        success:function(data){
            returnData= data.data;
        }
    });
    return returnData;
}


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

/****************************股票同步****************************************/


$("#syncDateRange").daterangepicker({
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
        $("#selfStartDate").val(start);
        $("#selfEndDate").val(end);
     }
     );
$("#syncHistoryBtn").click(function () {
    //进行展示信息
    $("#history_popup").modal('hide');
    $("#selfHistoryDate").hide();
    $("#syncHistorySelect").val("1");
    $("#syncHistorySelect").selectpicker('refresh');

    $("#sync_history_popup").modal('show');
})
$("#syncHistorySelect").change(function () {
    //获取值.
    let type=$(this).val();
    if(type==0){
        $("#selfHistoryDate").show();
    }else{
        $("#selfHistoryDate").hide();
    }
})
/**进行同步*/
$("#codeSync").click(function(){
    //执行同步的操作
    const info = getFillInfo("",0);
    $.ajax({
        async:false,
        type:"post",
        url:STOCK_ASYNC_URL,
        data:JSON.stringify(info),
        dataType:"json",
        contentType:"application/json;charset=utf-8",
        success:function(data){
            //进行同步，同步成功之后，刷新表格
            Flavr.falert("同步股票信息成功");
            $("#codeSync").hide();
            $("#stock_table").bootstrapTable('refresh', '{silent: true}');
        }
    });

})

/**历史进行同步*/
$("#codeHistorySync").click(function(){
    //执行同步的操作
    let type=$("#syncHistorySelect").val();
    const info = getFillInfo(clickRow.code,type,clickRow.exchange);
    if(type=='0'){
        info.startDate=formatTime($("#selfStartDate").val());
        info.endDate=formatTime($("#selfEndDate").val());
    }
    info.exchange=0;
    $.ajax({
        async:false,
        type:"post",
        url:STOCK_HISTORY_ASYNC_URL,
        data:JSON.stringify(info),
        dataType:"json",
        contentType:"application/json;charset=utf-8",
        success:function(data){
            //进行同步，同步成功之后，刷新表格
            Flavr.falert("同步该股票历史记录信息成功");
            //进行展示信息
            $("#selfHistoryDate").hide();
            $("#sync_history_popup").modal('hide');
            $("#stock_history_table").bootstrapTable('refresh', '{silent: true}');
            $("#history_popup").modal('show');
        }
    });

})
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

/***************************股票加入自选**************************************/
//  加入自选
function addSelfSelect(code){
    if(isEmpty(code)){
        Flavr.falert("请先选择加入自选的股票")
        return false;
    }
    //进行请求
   let postResponse = postAjax(
       STOCK_SELECTED_ADD_URL,
        {"stockCode":code}
    );
    //如果成功，那么就是登录成功.
    if(postResponse.success){
        Flavr.falert("添加自选成功");
    }else{
        Flavr.falert(postResponse.message);
    }
}