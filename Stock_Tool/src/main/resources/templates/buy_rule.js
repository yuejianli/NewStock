/*处理url跳转的问题*/
var mockType =1;
var ruleType =1;
var totalConditionList = [];

function initCondition() {
    //获取相关的条件参数信息
    let postResponse = postAjax(RULE_CONDITION_LIST,{});
    if(postResponse.success){
        totalConditionList= postResponse.data;
    }
    bind_bootstrap_select_data(totalConditionList,$("#add_conditionCode"),"code","name");
    bind_bootstrap_select_data(totalConditionList,$("#update_conditionCode"),"code","name");
}

$(function () {
    $("#buyrule_table").init();
    $("#rulestock_table").init();
    initCondition();
})
var buyrule_table_column=[
    {
        title : '编号',
        field : 'id',
        visible: false,
        align:"center",
        width:"150px"
    },
    {
        title : '名称',
        field : 'name',
        align:"center",
        width:"150px"
    },
    {
        title : '规则条件编号',
        field : 'conditionName',
        align:"center",
        width:"150px"
    },
    {
        title : '比较类型',
        field : 'ruleValueType',
        align:"center",
        formatter: ruleValueTypeFormatter
    },
    {
        title : '规则对应值',
        field : 'ruleValue',
        align:"center"
    },
    {
        title : '交易股票数',
        field : 'tradeNum',
        align:"center"
    },
    {
        title : '交易差值类型',
        field : 'tradeValueType',
        formatter: tradeValueTypeFormatter,
        align:"center"
    },
    {
        title : '交易差值',
        field : 'tradePrice',
        align:"center"
    },
    {
        title : '规则类型',
        field : 'ruleType',
        formatter: ruleTypeFormatter,
        align:"center"
    },
    {
        title : '状态',
        field : 'status',
        formatter: statusFormatter,
        align:"center"
    },
    {
        title : '创建时间',
        field : 'createTime',
        align:"center"
    },
    {
        title : '修改时间',
        field : 'updateTime',
        align:"center"
    },
    {
        title:"操作",
        field : 'operation',
        align:"center",
        width:"450px",
        formatter: operationFormatter,
        events: "operationEvents"
    }
]

$('#buyrule_table').bootstrapTable({
    method : 'post',
    url : RULE_LIST,//请求路径
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
    toolbarAlign: "left",
    showRefresh : true,//刷新按钮
    ajaxOptions:{
        headers: {"Authorization": getToken()}
    },
    queryParams : queryParams,
    responseHandler: handleClientData,
    columns : buyrule_table_column
})

/* table查询时参数的方法 */
function queryParams(params) {
    let query= {
        "pageSize" : params.limit, // 每页显示数量
        "pageNum" : (params.offset / params.limit) + 1,
        "mockType": mockType,
        "ruleType": ruleType
    }
   return query;
}
//处理机构返回数据
function handleClientData(res){
    let data= res.data ||[];
    return {
        total: data.total,
        rows: data.list
    };
}
function ruleValueTypeFormatter(value){
    return value==1?"金额":"比例";
}
function tradeValueTypeFormatter(value){
    return value==1?"金额":"比例";
}
function ruleTypeFormatter(value){
    return value==1?"买入":"卖出";
}
function statusFormatter(value){
    return value==1?"正常":"禁用";
}
/* 给每一行增加操作按钮 */
function operationFormatter(value, row, index) {

    var text = "";
   text += '<a class="update text-primary" href="javascript:void(0)" data-toggle="tooltip" title="修改">'
       +'<i class="fa fa-pencil"></i>&nbsp;修改&nbsp;&nbsp;</a>';
   text +=  '<a class="delete text-primary" href="javascript:void(0)" data-toggle="tooltip" title="删除规则">'
    +'<i class="fa fa-minus"></i>&nbsp;删除规则&nbsp;&nbsp;</a>';
    if(row.status==1){
        //禁用
        text += '<a class="disable text-warning" href="javascript:void(0)" data-toggle="tooltip" title="禁用">'
        +'<i class="fa fa-times-circle"></i>&nbsp;禁用&nbsp;&nbsp;</a>';
    }else{
        //启用
        text += '<a class="enable text-success" href="javascript:void(0)" data-toggle="tooltip" title="启用">'
            +'<i class="fa fa-check-circle"></i>&nbsp;启用&nbsp;&nbsp;</a>';
    }
    text += '<a class="config text-primary" href="javascript:void(0)" data-toggle="tooltip" title="配置股票">'
        +'<i class="fa fa-cog"></i>&nbsp;配置股票&nbsp;&nbsp;</a>';
    return [text].join('');
}
var old_info ;
//* 给操作按钮增加点击事件 */
window.operationEvents={
    'click .update' : function(e, value, row, index) {
        //展示信息
        old_info= row;
        //处理信息，并展示.
        showInfo(row);
        $("#update_popup").modal('show');
    },
    //禁用规则
    'click .disable' : function(e, value, row, index) {
        //处理信息，并展示.
        new $.flavr({
            content     : "您确定要禁用该规则吗 ？",
            dialog      : 'confirm',
            onConfirm   : function(){
                disableRule(row.id);
            }
        });
    },
    'click .enable' : function(e, value, row, index) {
        //处理信息，并展示.
        new $.flavr({
            content     : "您确定要启用该规则吗 ？",
            dialog      : 'confirm',
            onConfirm   : function(){
                enableRule(row.id);
            }
        });
    },
    'click .delete' : function(e, value, row, index) {
        //处理信息，并展示.
        new $.flavr({
            content     : "您确定要删除该规则吗 ？",
            dialog      : 'confirm',
            onConfirm   : function(){
                deleteRule(row.id);
            }
        });
    },
    'click .config' : function(e, value, row, index) {
        //处理信息，并展示.
        //展示信息
        old_info= row;
        click_index= old_info.id;
        $("#rulestock_table").bootstrapTable('refresh', '{silent: true}');
        $("#config_popup").modal('show');
    }
};


function disableRule(id){
    if(isEmpty(id)){
        Flavr.falert("请选择要禁用的规则")
        return false;
    }
    //进行请求
    let postResponse = postAjax(
        RULE_DISABLE,
        {"id":id}
    );
    //如果成功，那么就是登录成功.
    if(postResponse.success){
        Flavr.falert("禁用规则成功");
        $("#buyrule_table").bootstrapTable('refresh', '{silent: true}');
    }else{
        Flavr.falert(postResponse.message);
    }
}
function enableRule(id){
    if(isEmpty(id)){
        Flavr.falert("请选择要启用的规则")
        return false;
    }
    //进行请求
    let postResponse = postAjax(
        RULE_ENABLE,
        {"id":id}
    );
    //如果成功，那么就是登录成功.
    if(postResponse.success){
        Flavr.falert("启用规则成功");
        $("#buyrule_table").bootstrapTable('refresh', '{silent: true}');
    }else{
        Flavr.falert(postResponse.message);
    }
}
function deleteRule(id){
    if(isEmpty(id)){
        Flavr.falert("请选择要删除的规则")
        return false;
    }
    //进行请求
    let postResponse = postAjax(
        RULE_DELETE,
        {"id":id}
    );
    //如果成功，那么就是登录成功.
    if(postResponse.success){
        Flavr.falert("删除规则成功");
        $("#buyrule_table").bootstrapTable('refresh', '{silent: true}');
    }else{
        Flavr.falert(postResponse.message);
    }
}
$("#add").click(function(){
    //添加规则
    $("#add_conditionCode").selectpicker('refresh');
    $("#add_ruleValueType").selectpicker('refresh');
    $("#add_tradeValueType").selectpicker('refresh');
    $("#add_ruleValue").val("");
    $("#add_tradeNum").val("");
    $("#add_tradePrice").val("");
    $("#add_popup").modal('show');
})
//点击添加规则按钮里
$("#add_submit").click(function(){

    let add_name = $("#add_name").val();
    if(isEmpty(add_name)){
        Flavr.falert("名称不能为空");
        return ;
    }

    let add_conditionCode = $("#add_conditionCode").val();
    if(isEmpty(add_conditionCode)){
        Flavr.falert("选择条件不能为空");
        return ;
    }

    let add_ruleValueType = $("#add_ruleValueType").val();
    if(isEmpty(add_ruleValueType)){
        Flavr.falert("对应类型不能为空");
        return ;
    }

    let add_tradeValueType = $("#add_tradeValueType").val();
    if(isEmpty(add_tradeValueType)){
        Flavr.falert("交易差值类型不能为空");
        return ;
    }

    let add_ruleValue = $("#add_ruleValue").val();
    if(isEmpty(add_ruleValue)){
        Flavr.falert("对应关系值不能为空");
        return ;
    }

    let add_tradeNum = $("#add_tradeNum").val();
    if(isEmpty(add_tradeNum)){
        Flavr.falert("交易股票数不能为空");
        return ;
    }

    let add_tradePrice = $("#add_tradePrice").val();
    if(isEmpty(add_tradePrice)){
        Flavr.falert("交易差值不能为空");
        return ;
    }

    let addInfo ={
        "name":add_name,
        "conditionCode":add_conditionCode,
        "ruleValueType":add_ruleValueType,
        "ruleValue":add_ruleValue,
        "tradeNum":add_tradeNum,
        "tradeValueType":add_tradeValueType,
        "tradePrice":add_tradePrice,
        "ruleType":ruleType,
        "mockType":mockType
    }

    let postResponse = postAjax(RULE_ADD,
        addInfo
        );
    //如果成功，那么就是登录成功.
    if(postResponse.success){
        Flavr.falert("添加规则成功");
        $("#buyrule_table").bootstrapTable('refresh', '{silent: true}');
        $("#add_popup").modal('hide');
    }else{
        Flavr.falert(postResponse.message);
    }
})

function showInfo(ruleInfo) {
    //添加规则
    $("#update_conditionCode").val(ruleInfo.conditionCode);
    $("#update_conditionCode").selectpicker('refresh');
    $("#update_ruleValueType").val(ruleInfo.ruleValueType);
    $("#update_ruleValueType").selectpicker('refresh');
    $("#update_tradeValueType").val(ruleInfo.tradeValueType);
    $("#update_tradeValueType").selectpicker('refresh');
    $("#update_name").val(ruleInfo.name);
    $("#update_ruleValue").val(ruleInfo.ruleValue);
    $("#update_tradeNum").val(ruleInfo.tradeNum);
    $("#update_tradePrice").val(ruleInfo.tradePrice);
}
//点击添加规则按钮里
$("#update_submit").click(function(){
    let update_name = $("#update_name").val();
    if(isEmpty(update_name)){
        Flavr.falert("名称不能为空");
        return ;
    }

    let update_conditionCode = $("#update_conditionCode").val();
    if(isEmpty(update_conditionCode)){
        Flavr.falert("选择条件不能为空");
        return ;
    }

    let update_ruleValueType = $("#update_ruleValueType").val();
    if(isEmpty(update_ruleValueType)){
        Flavr.falert("对应类型不能为空");
        return ;
    }

    let update_tradeValueType = $("#update_tradeValueType").val();
    if(isEmpty(update_tradeValueType)){
        Flavr.falert("交易差值类型不能为空");
        return ;
    }

    let update_ruleValue = $("#update_ruleValue").val();
    if(isEmpty(update_ruleValue)){
        Flavr.falert("对应关系值不能为空");
        return ;
    }

    let update_tradeNum = $("#update_tradeNum").val();
    if(isEmpty(update_tradeNum)){
        Flavr.falert("交易股票数不能为空");
        return ;
    }

    let update_tradePrice = $("#update_tradePrice").val();
    if(isEmpty(update_tradePrice)){
        Flavr.falert("交易差值不能为空");
        return ;
    }
    let updateInfo ={
        "id":old_info.id,
        "name":update_name,
        "conditionCode":update_conditionCode,
        "ruleValueType":update_ruleValueType,
        "ruleValue":update_ruleValue,
        "tradeNum":update_tradeNum,
        "tradeValueType":update_tradeValueType,
        "tradePrice":update_tradePrice,
        "ruleType":ruleType,
        "mockType":mockType
    }

    let postResponse = postAjax(RULE_UPDATE,
        updateInfo
    );
    //如果成功，那么就是登录成功.
    if(postResponse.success){
        Flavr.falert("修改规则成功");
        $("#buyrule_table").bootstrapTable('refresh', '{silent: true}');
        $("#update_popup").modal('hide');
    }else{
        Flavr.falert(postResponse.message);
    }
})


// 配置股票信息

var rulestock_table_column=[
    {
        field : "checkbox",
        align : "center",
        valign : 'middle',
        checkbox : true,
        formatter :checkFormatter
    },
    {
        title : '股票代码',
        field : 'stockCode',
        align:"center",
        width:"150px"
    },
    {
        title : '股票名称',
        field : 'stockName',
        align:"center",
        width:"240px"
    }
]

$('#rulestock_table').bootstrapTable({
    method : 'post',
    url : RULE_STOCK_APPLYLIST,//请求路径
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
    toolbar: '#stock_custom-toolbar',
    toolbarAlign: "left",
    showRefresh : true,//刷新按钮
    ajaxOptions:{
        headers: {"Authorization": getToken()}
    },
    queryParams : stockQueryParams,
    responseHandler: stockHandleClientData,
    columns : rulestock_table_column
})
var click_index =0;
function stockQueryParams(params) {
    let query= {
       "id": click_index
    }
    return query;
}
//处理机构返回数据
var responseStockData ={
    "allList":[],
    "applyList":[],
    "otherApplyList":[]
};
function stockHandleClientData(res){
    let data= res.data ||[];
    responseStockData = data;
    //对数据进行处理.
    let applyList = responseStockData.applyList ||[];
    apply = new Array();
    if(applyList.length>0){
        $.each(applyList,function(index,item){
            apply.push(item.stockCode);
        })
    }

    let otherApplyList = responseStockData.otherApplyList ||[];
    otherApply= new Array();
    if(otherApplyList.length>0){
        $.each(otherApplyList,function(index,item){
            otherApply.push(item.stockCode);
        })
    }
    return data.allList||[];
}

var apply = new Array();;
var otherApply= new Array();
//格式化选择框
function checkFormatter(value,row,index){
    if(!click_index){
        return {
            checked:false,
            disabled:false
        }
    }
    let checked =false;
    let disabled =false;

    if(apply.indexOf(row.stockCode)>=0){
        checked =true;
        disabled=false;
    }else  if(otherApply.indexOf(row.stockCode)>=0){
        checked =true;
        disabled=true;
    }else{
        checked =false;
        disabled =false;
    }
    return {
        checked:checked,
        disabled:disabled
    }

}

$("#rulestock_table").on('check.bs.table uncheck.bs.table '
    + 'check-all.bs.table uncheck-all.bs.table', function() {

});
//获取选择列
function getRowSelections() {
    return $.map($("#rulestock_table").bootstrapTable('getSelections'), function(row) {
        return row;
    });
}

/**
 * 处理移除的信息配置
 * @param nowSelectList
 * @returns {any[]}
 */
function getRemoveCodeList(nowSelectList) {
    nowSelectList = nowSelectList||[];
    let result =new Array();
    if(nowSelectList.length<1){
        return result;
    }
    //处理其他适用的
    let otherApplyList = responseStockData.otherApplyList ||[];
    let otherApply = new Array();
    if(otherApplyList.length>0){
        $.each(otherApplyList,function(index,item){
            otherApply.push(item.stockCode);
        })
    }
    console.log("其他适用:"+otherApply);
    //取两个集合的交集， 就是本次移除 的集合信息
    return nowSelectList.minus(otherApply);
}

$("#config_submit").click(function(){
    console.log("输出信息");
    let rows = getRowSelections()||[];
    let idArr =new Array();
    if(rows.length>0){
        $.each(rows,function(index,item){
            idArr.push(item.stockCode);
        })
    }
    console.log("全选:"+idArr);
    let applyCodeList = getRemoveCodeList(idArr);
    console.log("剩余添加的:"+applyCodeList);
    let removeCodeList = [];
    //进行请求
    let info = {
        "id": click_index,
        "applyCodeList": applyCodeList,
        "removeCodeList": removeCodeList
    };
    let postResponse = postAjax(
        RULE_STOCK_APPLY,
        info
    );
    //如果成功，那么就是登录成功.
    if(postResponse.success){
        Flavr.falert("配置股票成功");
        $("#rulestock_table").bootstrapTable('refresh', '{silent: true}');
    }else{
        Flavr.falert(postResponse.message);
    }
})