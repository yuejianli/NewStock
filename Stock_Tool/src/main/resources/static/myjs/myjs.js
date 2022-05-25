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
    return sessionStorage.getItem(Authorization)||  AUTHORIZATION ;
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

function formatTime(shijian){
    let date = new Date(shijian)
    var y = date.getFullYear();
    var m = date.getMonth() + 1;
    m = m < 10 ? ('0' + m) : m;
    var d = date.getDate();
    d = d < 10 ? ('0' + d) : d;
    var h = date.getHours();
    h=h < 10 ? ('0' + h) : h;
    var minute = date.getMinutes();
    minute = minute < 10 ? ('0' + minute) : minute;
    var second=date.getSeconds();
    second=second < 10 ? ('0' + second) : second;
    return y + '-' + m + '-' + d+' '+h+':'+minute+':'+second;
};
function formatDate(shijian){
    let date = new Date(shijian)
    var y = date.getFullYear();
    var m = date.getMonth() + 1;
    m = m < 10 ? ('0' + m) : m;
    var d = date.getDate();
    d = d < 10 ? ('0' + d) : d;
    return y + '-' + m + '-' + d;
};
