var yzmKey="yzm";
/**
 * 拼接验证码信息
 * @returns 验证码地址+随机数
 */
var randomNum  ;
function concatRealYzmUrl() {
    //从缓存中，获取相关的方法.
    let yzmUrl = getYzmUrl();
    return yzmUrl+randomNum();
}
function randomNum(){
    //将其封装，添加数字.
    let timeStamp=new Date().getTime();
    randomNum = "0.903"+timeStamp;
    return randomNum;
}
function reloadYzm() {
    //将其转换，封装成真正的 url
    let realYzmUrl = concatRealYzmUrl();
    //封装成 url
    $("#yzm").attr("src",realYzmUrl);
}

$(function(){
   reloadYzm();　　　　　　　　
})
$("#yzm").click(function(){
    reloadYzm();
})


/**
 * 交易用户的相关处理
 */
//点击登录
$("#login").click(function(){
    const info = getFillInfo();
    //进行验证
    if(!validateSubmit(info)){
        return ;
    }
    info.randNum=randomNum;
    let postResponse = postAjax(TRADE_USER_LOGIN_URL,info);
    //如果成功，那么就是登录成功.
    if(postResponse.success){
        Flavr.falert("交易用户登录成功");
        //进行跳转,跳转到首页
        window.location.href = "page.html";
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
    let identifyCode = $("#identifyCode").val();
    const info = {
        "password": "二货，我不输入密码了",
        "identifyCode": identifyCode
    };
    return info;
}
// 验证用户登录信息
function validateSubmit(info){
    if(isEmpty(info.identifyCode)||info.identifyCode.length!=4||!/^\d{4}$/.test(info.identifyCode)){
        Flavr.falert("验证码必须为4位纯数字");
        return false;
    }
    return true;
}

function getYzmUrl() {
    //看session 里面是否有对应的地址
    let data=sessionStorage.getItem(yzmKey);
    if(!isEmpty(data)){
        return data;
    }
    let getResponse = getAjax(TRADE_USER_LOGIN_YZM_URL);
    let yzmResult=getResponse.data;
    sessionStorage.setItem(yzmKey,yzmResult);
    return yzmResult;
}