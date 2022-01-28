/*处理url跳转的问题*/
var clickRow;
$(function () {
    $("#condition_table").init();
})
var condition_table_column=[
    {
        title : '编号',
        field : 'id',
        visible: false,
        align:"center",
        width:"150px"
    },
    {
        title : '条件编码',
        field : 'code',
        align:"center",
        width:"150px"
    },
    {
        title : '条件名称',
        field : 'name',
        align:"center",
        width:"240px"
    },
    {
        title : '条件描述',
        field : 'description',
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
        title : '最后修改时间',
        field : 'updateTime',
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

$('#condition_table').bootstrapTable({
    method : 'post',
    url : "tradeRuleCondition/list",//请求路径
    striped : true, //是否显示行间隔色
    pageNumber : 1, //初始化加载第一页
    pagination : true,//是否分页
    sidePagination : 'client',//server:服务器端分页|client：前端分页
    pageSize : 15,//单页记录数
    pageList : [5,10,20,50,100,200],//可选择单页记录数
    cache: true, //设置缓存
    sortable: true,  //是否启用排序
    sortOrder: "asc", //排序方式
    search: true,                      //是否显示表格搜索
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
    columns : condition_table_column
})

/* table查询时参数的方法 */
function queryParams(params) {
   return {};
}
//处理机构返回数据
function handleClientData(res){
    let data= res.data.result ||[];
    return data;
}
/* 给每一行增加操作按钮 */
function operationFormatter(value, row, index) {
    return [
        '<a class="update text-primary" href="javascript:void(0)" data-toggle="tooltip" title="修改配置信息">',
        '<i class="fa fa-info"></i>&nbsp;修改配置信息&nbsp;&nbsp;</a>'
    ].join('');
}

///* 给操作按钮增加点击事件 */
window.operationEvents={
    'click .update' : function(e, value, row, index) {
        //进行修改
        $("#name").val(row.name);
        $("#description").val(row.description);
        clickRow=row;
        $("#update_popup").modal('show');

    }
};
$("#update_submit").click(function(){
    let name=$("#name").val();
    let description=$("#description").val();
    if(isEmpty(name)){
        Flavr.falert("请输入名称");
        return false;
    }
    if(isEmpty(description)){
        Flavr.falert("请输入参数内容");
        return false;
    }
    var info ={
        "id":clickRow.id,
        "name":name,
        "description":description
    }

    let postResponse = postAjax("../tradeRuleCondition/update",
        info);
    if(postResponse.success){
        Flavr.falert("修改成功");
        $("#condition_table").bootstrapTable('refresh', '{silent: true}');
        $("#update_popup").modal('hide');
    }else{
        Flavr.falert(postResponse.message);
    }
})