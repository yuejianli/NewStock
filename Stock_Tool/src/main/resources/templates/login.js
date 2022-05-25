$(function(){
    　　　　　　　　　　　
})
//点击登录
$("#login").click(function(){
    const info = getFillInfo();
    //进行验证
    if(!validateSubmit(info)){
        return ;
    }
    let postResponse = postAjax(LOGIN_URL,info);
    //如果成功，那么就是登录成功.
    if(postResponse.success){
        Flavr.falert("用户登录成功");
        //获取登录的数据
        let user = postResponse.data.currentUser;
        sessionStorage.setItem("loginUserId",user.id);
        sessionStorage.setItem("Authorization",user.token);
        AUTHORIZATION = user.token;
        //进行跳转
        window.location.href = "tradelogin";
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
    let account = $("#account").val();
    let password = $("#password").val();
    let readAgreement=0;
    //看是否被选中
    if($('#readAgreement').is(":checked")){
        readAgreement = 1;
    }
    let autoLogin=0;
    //看是否被选中
    if($('#autoLogin').is(":checked")){
        autoLogin = 1;
    }
    let rememberPs=0;
    //看是否被选中
    if($('#rememberPs').is(":checked")){
        rememberPs = 1;
    }
    const info = {
        "account": account,
        "password": password,
        "readAgreement": readAgreement,
        "autoLogin":autoLogin,
        "rememberPs":rememberPs
    };
    return info;
}
// 验证用户登录信息
function validateSubmit(info){
    if(isEmpty(info.account)){
        Flavr.falert("登录账号不能为空");
        return false;
    }
    if(isEmpty(info.password)){
        Flavr.falert("密码不能为空");
        return false;
    }
    if(info.readAgreement!=1){
        Flavr.falert("请阅读并勾选我已知晓部分");
        return false;
    }
    return true;
}