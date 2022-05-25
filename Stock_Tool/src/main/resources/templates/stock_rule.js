/*处理url跳转的问题*/
var MOCK_TYPE=1;
$(function () {
    $("#stockrule2_table").init();
})
var stockrule_table2_column=[
    {
        title : '股票编码',
        field : 'stockCode',
        align:"center",
        width:"150px"
    },
    {
        title : '股票名称',
        field : 'stockName',
        align:"center",
        width:"240px"
    },
    {
        title : '买入规则名称',
        field : 'buyRuleName',
        align:"center",
        width:"240px"
    },
    {
        title : '配置时间',
        field : 'buyCreateTime',
        align:"center",
        width:"240px"
    },
    {
        title : '买入规则状态',
        field : 'buyRuleStatus',
        formatter: buyStatusFormatter,
        align:"center",
        width:"240px"
    },
    {
        title : '卖出规则名称',
        field : 'sellRuleName',
        align:"center",
        width:"240px"
    },
    {
        title : '卖出规则配置时间',
        field : 'sellCreateTime',
        align:"center",
        width:"240px"
    },
    {
        title : '卖出规则状态',
        field : 'sellRuleStatus',
        formatter: sellStatusFormatter,
        align:"center",
        width:"240px"
    }
]

$('#stockrule2_table').bootstrapTable({
    method : 'post',
    url : RULE_STOCK_RULE_LIST,//请求路径
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
    columns : stockrule_table2_column
})

/* table查询时参数的方法 */
function queryParams(params) {
    let keyword = $("#keywords").val();
    let query= {
        "pageSize" : params.limit, // 每页显示数量
        "pageNum" : (params.offset / params.limit) + 1, //当前页码
        "keyword":keyword,
        "mockType":MOCK_TYPE
    }
    return query;
}
//处理机构返回数据
function handleClientData(res){
    let data= res.data ||[];
    return data;
}

function buyStatusFormatter(value,row,index){
    if(row.buyRuleId){
        return value==1?"正常":"禁用";
    }else{
        return "";
    }
}
function sellStatusFormatter(value,row,index){
    if(row.sellRuleId){
        return value==1?"正常":"禁用";
    }else{
        return "";
    }
}
$("#keywords").blur(function(){
    $("#stockrule2_table").bootstrapTable('refresh', '{silent: true}');
})