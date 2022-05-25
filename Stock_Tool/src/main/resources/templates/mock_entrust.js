/*处理url跳转的问题*/
// 虚拟持仓信息
var mockType = MOCK_MOCK_TYPE;

$(function () {
    $("#mockEntrust_table").init();
})
var mockEntrust_table_column=[
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
        width:"60px",
        formatter: dealTypeFormatter,
    },
    {
        title : '交易数量',
        field : 'entrustNum',
        align:"center",
        width:"60px"
    },
    {
        title : '交易价格',
        field : 'entrustPrice',
        align:"center",
        width:"60px"
    },
    {
        title : '交易的状态',
        field : 'entrustStatus',
        align:"center",
        width:"100px",
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
        width:"60px"
    },
    {
        title : '手续费',
        field : 'handMoney',
        align:"center",
        width:"60px"
    },
    {
        title : '总的费用',
        field : 'totalMoney',
        align:"center",
        width:"60px"
    },
    {
        title : '委托方式',
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

$('#mockEntrust_table').bootstrapTable({
    method : 'post',
    url : TRADE_ENTRUST_LIST,//请求路径
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
    columns : mockEntrust_table_column
})

/* table查询时参数的方法 */
function queryParams(params) {
    let query= {
        "pageSize" : params.limit, // 每页显示数量
        "pageNum" : (params.offset / params.limit) + 1, //当前页码,
        "mockType":mockType
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
/* 给每一行增加操作按钮 */
function operationFormatter(value, row, index) {
    if(row.entrustStatus ==1){
        return [
            '<a class="deal text-primary" href="javascript:void(0)" data-toggle="tooltip" title="手动成交">',
            '<i class="fa fa-info"></i>&nbsp;&nbsp;&nbsp;手动成交&nbsp;&nbsp;</a>',
            '<a class="revoke text-primary" href="javascript:void(0)" data-toggle="tooltip" title="撤销委托">',
            '<i class="fa fa-info"></i>&nbsp;&nbsp;&nbsp;撤销委托&nbsp;&nbsp;</a>',
        ].join('');
    }else{
        "已成交"
    }
}

///* 给操作按钮增加点击事件 */
window.operationEvents={
    //撤销委托信息
    'click .deal' : function(e, value, row, index) {
        //处理信息，并展示.
        selectedCode=row.code;
        new $.flavr({
            content     : "您确定将手动成交该委托吗？",
            dialog      : 'confirm',
            onConfirm   : function(){
                deal(row.id);
            }
        });
    },
    //撤销委托信息
    'click .revoke' : function(e, value, row, index) {
        //处理信息，并展示.
        selectedCode=row.code;
        new $.flavr({
            content     : "您确定将该委托撤销吗？",
            dialog      : 'confirm',
            onConfirm   : function(){
                revoke(row.id);
            }
        });
    }
};

// 手动成交
function deal(id){
    if(isEmpty(id)){
        Flavr.falert("请先选择要成交的委托记录")
        return false;
    }
    //进行请求
    let postResponse = postAjax(
        TRADE_DEAL,
        {
            "id":id,
            "mockType":mockType,
            "entrustType":1
        }
    );
    //如果成功，那么就是撤销委托成功
    if(postResponse.success){
        Flavr.falert("手动成交成功");
        $("#mockEntrust_table").bootstrapTable('refresh', '{silent: true}');
    }else{
        Flavr.falert(postResponse.message);
    }
}

// 撤销委托
function revoke(id){
    if(isEmpty(id)){
        Flavr.falert("请先选择要撤销的委托记录")
        return false;
    }
    //进行请求
    let postResponse = postAjax(
        TRADE_REVOKE,
        {
            "id":id,
            "mockType":mockType
        }
    );
    //如果成功，那么就是撤销委托成功
    if(postResponse.success){
        Flavr.falert("撤销委托成功");
        $("#mockEntrust_table").bootstrapTable('refresh', '{silent: true}');
    }else{
        Flavr.falert(postResponse.message);
    }
}