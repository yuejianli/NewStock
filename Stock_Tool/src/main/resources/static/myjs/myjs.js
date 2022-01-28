var Authorization ="Authorization";
function getAjax(url,info){
    let responseData=null;
    $.ajax({
        async:false,
        type:"get",
        url:url,
        data:JSON.stringify(info),
        dataType:"json",
        contentType:"application/json;charset=utf-8",
        headers: {
            Authorization:getToken() //自定义请求头
        },
        success:function(data){
            responseData=data;
        }
    });
    return responseData;
}

function postAjax(url,info){
    let responseData=null;
    if(!info){
        info={};
    }
    $.ajax({
        async:false,
        type:"post",
        url:url,
        data:JSON.stringify(info),
        dataType:"json",
        contentType:"application/json;charset=utf-8",
        headers: {
            Authorization:getToken() //自定义请求头
        },
        success:function(data){
            responseData=data;
        }
    });
    return responseData;
}
function getToken(){
    return sessionStorage.getItem(Authorization)|| "";
}

function bind_bootstrap_select_data(data, target, value, text) {
    target.empty();
    $.each(data, function(idx, item) {
        var val = item[value], txt = item[text];
        jQuery("<option value='" + val + "'>" + txt + "</option>").appendTo(
            target);
    });
}

//判断字符是否为空的方法
function isEmpty(obj){
    if(typeof obj == "undefined" || obj == null || obj == ""){
        return true;
    }else{
        return false;
    }
}