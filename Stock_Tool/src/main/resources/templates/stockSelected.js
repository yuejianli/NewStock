/*处理url跳转的问题*/

$(function () {
    $("#batchDel").hide();
    $("#stockSelected_table").init();
})
var stockSelected_table_column=[
    {
        field : "checkbox",
        align : "center",
        valign : 'middle',
        checkbox : true
    },
    {
        title : '编号',
        field : 'id',
        visible: false,
        align:"center",
        width:"150px"
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
    },
    {
        title : '添加自选时间',
        field : 'createTime',
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

$('#stockSelected_table').bootstrapTable({
    method : 'post',
    url : "stockSelected/list",//请求路径
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
    columns : stockSelected_table_column
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
    let data= res.data.result ||[];
    return {
        total: data.total,
        rows: data.list
    };
}
/* 给每一行增加操作按钮 */
function operationFormatter(value, row, index) {
    return [
        '<a class="delSelected text-primary" href="javascript:void(0)" data-toggle="tooltip" title="移除自选表">',
        '<i class="fa fa-info"></i>&nbsp;移除自选&nbsp;&nbsp;</a>',
    ].join('');
}

///* 给操作按钮增加点击事件 */
window.operationEvents={
    //查询基金具体信息
    'click .delSelected' : function(e, value, row, index) {
        //处理信息，并展示.
        historyCode=row.code;
        new $.flavr({
            content     : "您确定将该股票【"+row.stockName+"】移出自选吗 ？",
            dialog      : 'confirm',
            onConfirm   : function(){
                delSelfSelect(row.stockCode);
            }
        });
    }
};

$("#keywords").blur(function(){
    $("#stockSelected_table").bootstrapTable('refresh', '{silent: true}');
})

//  加入自选
function delSelfSelect(code){
    if(isEmpty(code)){
        Flavr.falert("请先选择移出自选的股票")
        return false;
    }
    //进行请求
    let postResponse = postAjax(
        "../stockSelected/deleteByCode",
        {"stockCode":code}
    );
    //如果成功，那么就是登录成功.
    if(postResponse.success){
        Flavr.falert("移出自选成功");
        $("#stockSelected_table").bootstrapTable('refresh', '{silent: true}');
    }else{
        Flavr.falert(postResponse.message);
    }
}

//复选框选中事件
$("#stockSelected_table").on('check.bs.table uncheck.bs.table '
    + 'check-all.bs.table uncheck-all.bs.table', function() {
    //获取选中的行
    let rows = getRowSelections()||[];
    //id 集合
    if(rows.length>0){
        //让批量删除按照可以使用
        $("#batchDel").show(100);
    }else{
        $("#batchDel").hide();
    }

});
//获取选择列
function getRowSelections() {
    return $.map($("#stockSelected_table").bootstrapTable('getSelections'), function(row) {
        return row;
    });
}
//批量删除
$("#batchDel").click(function(){
    let rows = getRowSelections()||[];
    if(rows.length<=0){
       Flavr.falert("请选择要删除的自选记录");
        return false;
    }
    var idArr =new Array();
    $.each(rows,function(index,item){
        idArr.push(item.id);
    })
    //获取了集合，接下来进行删除的操作
    //进行请求
    let postResponse = postAjax(
        "../stockSelected/delete",
        {"ids":idArr}
    );
    //如果成功，那么就是登录成功.
    if(postResponse.success){
        Flavr.falert("批量移出自选成功");
        $("#batchDel").hide();
        $("#stockSelected_table").bootstrapTable('refresh', '{silent: true}');
    }else{
        Flavr.falert(postResponse.message);
    }
})

$("#add").click(function(){
    $("#stockNameGroup").hide();
    $("#stockCode").val("");
    $("#add_popup").modal('show');
})
$("#stockCode").blur(function(){
    //检测
    var code =$("#stockCode").val();
    if(!validateCode(code)){
        return ;
    }
    //查询股票信息
    let postResponse=getStockInfo(code);
    if(postResponse.success){
        //回显数据
        $("#stockNameLabel").text(postResponse.data.result.name);
        $("#hiddenStockCode").val(postResponse.data.result.code);
        $("#stockNameGroup").show();
    }else{
        Flavr.falert(postResponse.message);
        return ;
    }
})

$("#add_submit").click(function(){
    let postResponse = postAjax("../stockSelected/add",
        {
            "stockCode":$("#hiddenStockCode").val()
        });
    //如果成功，那么就是登录成功.
    if(postResponse.success){
        Flavr.falert("添加自选成功");
        $("#stockSelected_table").bootstrapTable('refresh', '{silent: true}');
        $("#add_popup").modal('hide');
    }else{
        Flavr.falert(postResponse.message);
    }
})
/**
 * 股票股票信息的接口
 * @param code
 */
function getStockInfo(code){
    const info = {"code":code};
    return postAjax("../stock/getStockInfo",info);
}

/**
 * 验证股票的编号
 * @param code 股票的编号
 * @returns {boolean} 验证通过，返回 true, 验证不通过，返回false
 */
function validateCode(code){
    //验证股票代码
    if(isEmpty(code)){
        Flavr.falert("股票代码不能为空");
        return false;
    }
    //获取前两位的值
    const prefix = code.substr(0, 2);
    const realCode = code.substr(2);
    if(!(prefix=="sh"||prefix=="sz")){
        Flavr.falert("股票代码前缀地区必须以 sh(上海) 或者 sz(深圳) 开头");
        return false;
    }
    if(isEmpty(realCode)||realCode.length!=6||!/^\d{6}$/.test(realCode)){
        Flavr.falert("股票的实际代码编号必须为六位纯数字");
        return false;
    }
    return true;
}