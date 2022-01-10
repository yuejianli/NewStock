/*处理url跳转的问题*/
/**
 * 处理资产的展示信息
 * @param mockType
 */
function handlerMoneyShow(mockType) {
    let info ={
        "mockType":mockType
    }
  //进行提交
    let postResponse = postAjax("../tradeMoney/list",info);
    //如果成功，那么就是登录成功.
    if(postResponse.success){
        //获取信息
        let money = postResponse.data.result;
        $("#totalMoney").text(money.totalMoney);
        $("#marketMoney").text(money.marketMoney);
        $("#useMoney").text(money.useMoney);
        $("#takeoutMoney").text(money.takeoutMoney);
    }else{
        Flavr.falert(postResponse.message);
        let default_money = "0.00";
        $("#totalMoney").text(default_money);
        $("#marketMoney").text(default_money);
        $("#useMoney").text(default_money);
        $("#takeoutMoney").text(default_money);

    }
}

$(function () {
    //处理资产的信息
    handlerMoneyShow(1);
    $("#mockPosition_table").init();
})
var mockPosition_table_column=[
    {
        title : '股票编号',
        field : 'code',
        visible: false,
        align:"center",
        width:"150px"
    },
    {
        title : '股票的名称',
        field : 'name',
        align:"center",
        width:"150px"
    },
    {
        title : '总数量',
        field : 'allAmount',
        align:"center",
        width:"240px"
    },
    {
        title : '可用数量',
        field : 'useAmont',
        align:"center",
        width:"200px"
    },
    {
        title : '成本价',
        field : 'avgPrice',
        align:"center",
        width:"200px"
    },
    {
        title : '当前价',
        field : 'price',
        align:"center",
        width:"200px"
    },
    {
        title : '总的市值',
        field : 'allMoney',
        align:"center",
        width:"200px"
    },
    {
        title : '浮动盈亏',
        field : 'floatMoney',
        align:"center",
        width:"200px"
    },
    {
        title : '盈亏比例',
        field : 'floatProportion',
        align:"center",
        width:"200px"
    },
    {
        title:"操作",
        field : 'operation',
        align:"center",
        formatter: operationFormatter,
        events: "operationEvents"
    }
]

$('#mockPosition_table').bootstrapTable({
    method : 'post',
    url : "tradePosition/list",//请求路径
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
    showRefresh : true,//刷新按钮,
    rowStyle:rowStyle,//通过自定义函数设置行样式
    ajaxOptions:{
        headers: {"Authorization": getToken()}
    },
    queryParams : queryParams,
    responseHandler: handleClientData,
    columns : mockPosition_table_column
})

/* table查询时参数的方法 */
function queryParams(params) {
    let query= {
        "pageSize" : params.limit, // 每页显示数量
        "pageNum" : (params.offset / params.limit) + 1, //当前页码
        "mockType":1,
        "selectType":$("#selectType").val()
    }
   return query;
}
//处理机构返回数据
function handleClientData(res){
    let data= res.data.result ||[];
    return data;
}
/* 给每一行增加操作按钮 */
function operationFormatter(value, row, index) {
    return [
        '<a class="buy text-primary" href="javascript:void(0)" data-toggle="tooltip" title="买入">',
        '<i class="fa fa-info"></i>&nbsp;买入委托&nbsp;&nbsp;</a>',
        '<a class="sell text-primary" href="javascript:void(0)" data-toggle="tooltip" title="卖出">',
        '<i class="fa fa-history"></i>&nbsp;卖出委托&nbsp;&nbsp;</a>'
    ].join('');
}

function rowStyle(row, index) {
    var style = {};
    if(row.selectType==1){
        style={css:{'color':'#EE0000'}};
    }else{
        style={css:{'color':'#990000'}};
    }
    return style;
}
$("#selectType").change(function () {
    $("#mockPosition_table").bootstrapTable('refresh', '{silent: true}');
})
var clickRow ;
///* 给操作按钮增加点击事件 */
window.operationEvents={
    //查询股票具体信息
    'click .buy' : function(e, value, row, index) {
       //进行买入
        clickRow= row;
        $("#buy_codeName").text(row.name);
        $("#buy_popup").modal('show');
    },
    //查询股票具体信息
    'click .sell' : function(e, value, row, index) {
      //进行卖出
        clickRow= row;
        $("#sell_codeName").text(row.name);
        $("#sell_popup").modal('show');
    }
};

$("#buy_submit").click(function(){
    //进行买入， 获取相关的信息
    let amount=$("#buy_amount").val();
    let price=$("#buy_price").val();
    if(!validateNumber(amount)){
        return ;
    }
    if(!validatePrice(price)){
        return ;
    }
    //构建信息
    let buyInfo = {
        "code":clickRow.code,
        "name":clickRow.name,
        "mockType":1,
        "amount":amount,
        "price":price
    }
    //进行提交
    let postResponse = postAjax("../buy/buy",buyInfo);
    //如果成功，那么就是登录成功.
    if(postResponse.success){
        Flavr.falert("买入股票委托成功");
        $("#buy_popup").modal('hide');
    }else{
        Flavr.falert(postResponse.message);
    }
})


$("#sell_submit").click(function(){
    //进行买入， 获取相关的信息
    let amount=$("#sell_amount").val();
    let price=$("#sell_price").val();
    if(!validateNumber(amount)){
        return ;
    }
    if(!validatePrice(price)){
        return ;
    }
    //构建信息
    let sellInfo = {
        "code":clickRow.code,
        "name":clickRow.name,
        "mockType":1,
        "amount":amount,
        "price":price
    }
    //进行提交
    let postResponse = postAjax("../sell/sell",sellInfo);
    //如果成功，那么就是登录成功.
    if(postResponse.success){
        Flavr.falert("卖出股票委托成功");
        $("#sell_popup").modal('hide');
    }else{
        Flavr.falert(postResponse.message);
    }
})

/**
 * 验证股票的买入数量 必须为100的倍数
 * @param price
 * @returns {boolean}
 */
function validateNumber(number){
    //验证股票买入数量
    if(isEmpty(number)){
        Flavr.falert("股票数不能为空");
        return false;
    }
    const regs = /^\d+$/;
    if(!regs.test(number)){
        Flavr.falert("股票数必须为正整数");
        return false;
    }
    if(parseInt(number)%100!=0){
        Flavr.falert("股票数必须是100的倍数");
        return false;
    }
    return true;
}

/**
 * 验证股票的价格
 * @param price
 * @returns {boolean}
 */
function validatePrice(price){
    //验证股票价格
    if(isEmpty(price)){
        Flavr.falert("股票价格不能为空");
        return false;
    }
    const regs = /^(\d+)(\.\d+)?$/;
    if(!regs.test(price)){
        Flavr.falert("股票价格必须为正数");
        return false;
    }
    return true;
}