/*处理url跳转的问题*/
var clickRow;
$(function () {
    $("#config_table").init();
})
var config_table_column=[
    {
        title : '编号',
        field : 'id',
        visible: false,
        align:"center",
        width:"150px"
    },
    {
        title : '参数编码',
        field : 'code',
        align:"center",
        width:"150px"
    },
    {
        title : '参数名称',
        field : 'name',
        align:"center",
        width:"240px"
    },
    {
        title : '参数值',
        field : 'codeValue',
        align:"center",
        width:"240px"
    },
    {
        title : '添加时间',
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

$('#config_table').bootstrapTable({
    method : 'post',
    url : CONFIG_LIST_URL,//请求路径
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
    columns : config_table_column
})

/* table查询时参数的方法 */
function queryParams(params) {
    let query= {
        "pageSize" : params.limit, // 每页显示数量
        "pageNum" : (params.offset / params.limit) + 1 //当前页码
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
/* 给每一行增加操作按钮 */
function operationFormatter(value, row, index) {
    let userId =row.userId||0;
    if(userId==0){
        return [
            '<a class="update text-primary" href="javascript:void(0)" data-toggle="tooltip" title="自定义配置">',
            '<i class="fa fa-info"></i>&nbsp;自定义配置&nbsp;&nbsp;</a>',
        ].join('');
    }
    return [
        '<a class="update text-primary" href="javascript:void(0)" data-toggle="tooltip" title="自定义配置">',
        '<i class="fa fa-info"></i>&nbsp;自定义配置&nbsp;&nbsp;</a>',
        '<a class="reset text-primary" href="javascript:void(0)" data-toggle="tooltip" title="重置配置">',
        '<i class="fa fa-info"></i>&nbsp;重置配置&nbsp;&nbsp;</a>'
    ].join('');
}

///* 给操作按钮增加点击事件 */
window.operationEvents={
    'click .update' : function(e, value, row, index) {
        //进行修改
        $("#code").val(row.code);
        $("#name").val(row.name);
        $("#codeValue").val(row.codeValue);
        clickRow=row;
        $("#update_popup").modal('show');

    },
    //重置
    'click .reset' : function(e, value, row, index) {
        new $.flavr({
            content     : "您确定将该股票【"+row.code+"】重置吗？",
            dialog      : 'confirm',
            onConfirm   : function(){
                resetConfig(row.id);
            }
        });
    }
};

//  加入自选
function resetConfig(id){
    if(isEmpty(id)){
        Flavr.falert("请先选择要重置的配置参数记录")
        return false;
    }
    //进行请求
    let postResponse = postAjax(
        CONFIG_RESET_URL,
        {"id":id}
    );
    if(postResponse.success){
        Flavr.falert("重置参数成功");
        $("#config_table").bootstrapTable('refresh', '{silent: true}');
    }else{
        Flavr.falert(postResponse.message);
    }
}

$("#update_submit").click(function(){
    let name=$("#name").val();
    let codeValue=$("#codeValue").val();
    if(isEmpty(clickRow.id)){
        Flavr.falert("请选择要自定义的参数记录");
        return false;
    }
    if(isEmpty(name)){
        Flavr.falert("请输入名称");
        return false;
    }
    if(isEmpty(codeValue)){
        Flavr.falert("请输入参数内容");
        return false;
    }
    var info ={
        "id":clickRow.id,
        "name":name,
        "codeValue":codeValue
    }

    let postResponse = postAjax(CONFIG_UPDATE_URL,
        info);
    if(postResponse.success){
        Flavr.falert("修改成功");
        $("#config_table").bootstrapTable('refresh', '{silent: true}');
        $("#update_popup").modal('hide');
    }else{
        Flavr.falert(postResponse.message);
    }
})