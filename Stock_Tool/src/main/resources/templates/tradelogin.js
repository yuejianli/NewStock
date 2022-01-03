var yzmKey="yzm";
var timeStamp="";
function getRealYzmUrl() {
    //从缓存中，获取相关的方法.
    let yzmUrl = getYzmUrl();
    //将其封装，添加数字.
    timeStamp=new Date().getTime();
    return yzmUrl+"0.903"+timeStamp;
}

function reloadYzm() {
    //将其转换，封装成真正的 url
    let realYzmUrl = getRealYzmUrl();
    //封装成 url
    $("#yzm").attr("src",realYzmUrl);
}

$(function(){
   reloadYzm();　　　　　　　　
})
$("#yzm").click(function(){
    reloadYzm();
})

//点击登录
$("#login").click(function(){
    const info = getFillInfo();
    //进行验证
    if(!validateSubmit(info)){
        return ;
    }
    info.randNum="0.903"+timeStamp;
    let postResponse = postAjax("../tradeUser/login",info);
    //如果成功，那么就是登录成功.
    if(postResponse.success){
        Flavr.falert("交易用户登录成功");
        //进行跳转
        window.location.href = "../index";
    }else{
        Flavr.falert(postResponse.message);
    }
});

/**
* 获取填充的用户信息
* @returns 返回填充信息的对象
*/
function getFillInfo(){
    //获取用户填入的信息
    let password = $("#password").val();
    let identifyCode = $("#identifyCode").val();
    const info = {
        "password": password,
        "identifyCode": identifyCode
    };
    return info;
}
// 验证用户登录信息
function validateSubmit(info){

    if(isEmpty(info.password)){
        Flavr.falert("证券密码不能为空");
        return false;
    }
    if(isEmpty(info.identifyCode)||info.identifyCode.length!=4||!/^\d{4}$/.test(info.identifyCode)){
        Flavr.falert("验证码必须为4位纯数字");
        return false;
    }
    return true;
}

function getYzmUrl() {
    //看session 里面是否有对应的地址
    var data=sessionStorage.getItem(yzmKey);
    if(!isEmpty(data)){
        return data;
    }
    let getResponse = getAjax("../tradeMethod/yzm");
    let yzmResult=getResponse.data.result;
    sessionStorage.setItem(yzmKey,yzmResult);
    return yzmResult;
}
//判断字符是否为空的方法
function isEmpty(obj){
    if(typeof obj == "undefined" || obj == null || obj == ""){
        return true;
    }else{
        return false;
    }
}