/*处理url跳转的问题*/
var clickRow;
$(function () {
    $("#cache_table").init();
})
var cache_table_column=[
    {
        title : 'Key值',
        field : 'key',
        align:"center",
        width:"150px"
    },
    {
        title : '内容信息',
        field : 'value',
        align:"center",
        width:"240px"
    },
    {
        title:"操作",
        field : 'operation',
        align:"center",
        formatter: operationFormatter,
        events: "operationEvents"
    }
]

$('#cache_table').bootstrapTable({
    method : 'post',
    url : "cache/list",//请求路径
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
    columns : cache_table_column
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
        '<a class="update text-primary" href="javascript:void(0)" data-toggle="tooltip" title="更新缓存内容">',
        '<i class="fa fa-info"></i>&nbsp;更新&nbsp;&nbsp;</a>',
        '<a class="delete text-primary" href="javascript:void(0)" data-toggle="tooltip" title="删除缓存内容">',
        '<i class="fa fa-info"></i>&nbsp;删除缓存&nbsp;&nbsp;</a>'
    ].join('');
}

///* 给操作按钮增加点击事件 */
window.operationEvents={
    'click .update' : function(e, value, row, index) {
        //进行修改
        $("#key").val(row.key);
        $("#value").val(row.value);
        clickRow=row;
        $("#update_popup").modal('show');

    },
    //重置
    'click .delete' : function(e, value, row, index) {
        new $.flavr({
            content     : "您确定将该缓存【"+row.key+"】删除吗？",
            dialog      : 'confirm',
            onConfirm   : function(){
                deleteCache(row.key);
            }
        });
    }
};
$("#keywords").blur(function(){
    $("#cache_table").bootstrapTable('refresh', '{silent: true}');
})
//  加入自选
function deleteCache(key){
    if(isEmpty(key)){
        Flavr.falert("请先选择要删除的缓存记录")
        return false;
    }
    //进行请求
    let postResponse = postAjax(
        "../cache/delete",
        {"key":key}
    );
    if(postResponse.success){
        Flavr.falert("删除缓存成功");
        $("#cache_table").bootstrapTable('refresh', '{silent: true}');
    }else{
        Flavr.falert(postResponse.message);
    }
}

$("#update_submit").click(function(){
    let key=$("#key").val();
    let value=$("#value").val();
    if(isEmpty(key)){
        Flavr.falert("请输入缓存的key");
        return false;
    }
    if(isEmpty(value)){
        Flavr.falert("请输入缓存内容");
        return false;
    }
    var info ={
        "key":key,
        "value":value
    }

    let postResponse = postAjax("../cache/update",
        info);
    if(postResponse.success){
        Flavr.falert("修改缓存成功");
        $("#cache_table").bootstrapTable('refresh', '{silent: true}');
        $("#update_popup").modal('hide');
    }else{
        Flavr.falert(postResponse.message);
    }
})