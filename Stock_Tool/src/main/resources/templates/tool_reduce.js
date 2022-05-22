var DEFAULT_STOCK_CODE="002415";
$(function(){
    showExampleInfo(
        DEFAULT_STOCK_CODE
    );
})

function showExampleInfo(code){
    let stockInfo=getStockInfo(code);
    if(stockInfo==null){
        // Flavr.falert("输入的股票代码不正确");
        return ;
    }
    const nowPrice = stockInfo.nowPrice;
    $("#code").val(stockInfo.code);
    $("#price").val(nowPrice);
    $("#number").val("100");
    $("#secnumber").val("100");
    $("#sec_price").val(nowPrice+2);
    $("#make_price").val(nowPrice-2);
    $("#make_proportion").val("2");
    $("#platform_fee").val("0.03");
    $("#tradingArea").val("0");
    $("#tradingArea").selectpicker('refresh');
    $("#nameType").val("0");
    $("#nameType").selectpicker('refresh');

}

//重置
$("#reset").click(function(){
    //清空展示信息
    showExampleInfo(
        DEFAULT_STOCK_CODE
    );
    $(".info").text(" ");
});
//金额计算
$("#sec_price_submit").click(function(){
    const info = getFillInfo(1);
    //进行验证
    if(!validateSubmit(info)){
        return ;
    }
    $.ajax({
        async:false,
        type:"post",
        url:TOOL_MONEY_REDUCE,
        data:JSON.stringify(info),
        dataType:"json",
        contentType:"application/json;charset=utf-8",
        success:function(data){
            showInfo(data.data);
        }
    });
});


//价格计算
$("#make_price_submit").click(function(){
    const info = getFillInfo(2);
    //进行验证
    if(!validateSubmit(info)){
        return ;
    }
    $.ajax({
        async:false,
        type:"post",
        url:TOOL_MONEY_REDUCE,
        data:JSON.stringify(info),
        dataType:"json",
        contentType:"application/json;charset=utf-8",
        success:function(data){
            showInfo(data.data);
        }
    });
});
/**
 * 获取填充的用户信息
 * @param type
 * @returns 返回填充信息的对象
 */
function getFillInfo(type){
    //获取用户填入的信息
    const code = $("#code").val();
    const price = $("#price").val();
    const number = $("#number").val();
    const platformFee = $("#platform_fee").val();
    const tradingArea = $("#tradingArea").val();
    const nameType = $("#nameType").val();
    const secNumber = $("#secnumber").val();
    const secPrice = $("#sec_price").val();
    const makePrice = $("#make_price").val();
    const info = {
        "code": code,
        "price": price,
        "number": number,
        "secNumber":secNumber,
        "platformFee":platformFee,
        "tradingArea":tradingArea,
        "nameType":nameType,
        "secPrice": secPrice,
        "makePrice": makePrice,
        "type":type
    };
    return info;
}

/**
 * 验证提示信息
 * @param info
 * @returns {boolean}
 */
function validateSubmit(info){
   if(!validateCode(info.code)){
       return false;
   }
    if(!validatePrice(info.price)){
        return false;
    }
    if(!validateNumber(info.number)){
        return false;
    }
    if(!validatePlatformFee(info.platformFee)){
        return false;
    }
    if(!validateTradingArea(info.tradingArea)){
        return false;
    }
    if(info.type==1&&!validateSecPrice(info.secPrice)){
        return false;
    }
    if(info.type==2&&!validateMakePrice(info.makePrice)){
        return false;
    }
    return true;
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
    const realCode = code;
    if(isEmpty(realCode)||realCode.length!=6||!/^\d{6}$/.test(realCode)){
        Flavr.falert("股票的实际代码编号必须为六位纯数字");
        return false;
    }
    return true;
}

/**
 * 验证股票的价格
 * @param price
 * @returns {boolean}
 */
function validatePrice(price){
    //验证股票价格
    if(isEmpty(price)){
        Flavr.falert("买入时的股票价格不能为空");
        return false;
    }
    const regs = /^(\d+)(\.\d+)?$/;
    if(!regs.test(price)){
        Flavr.falert("买入时的股票价格必须为正数");
        return false;
    }
    return true;
}

/**
 * 验证股票的买入数量 必须为100的倍数
 * @param price
 * @returns {boolean}
 */
function validateNumber(number){
    //验证股票买入数量
    if(isEmpty(number)){
        Flavr.falert("买入的股票数不能为空");
        return false;
    }
    const regs = /^\d+$/;
    if(!regs.test(number)){
        Flavr.falert("买入时的股票数必须为正整数");
        return false;
    }
    if(parseInt(number)%100!=0){
        Flavr.falert("买入时的股票数必须是100的倍数");
        return false;
    }
    return true;
}

/**
 * 验证平台手续费
 * @param platformFee
 */
function validatePlatformFee(platformFee){
    //验证想赚的钱数
    if(isEmpty(platformFee)){
        Flavr.falert("请输入平台佣金比例");
        return false;
    }
    const regs = /^(\d+)(\.\d+)?$/;
    if(!regs.test(platformFee)){
        Flavr.falert("平台佣金比例必须为正数");
        return false;
    }
    //转换成 double
    const platForm=parseFloat(platformFee);
    if(platForm<0.02||platForm>0.03){
        Flavr.falert("平台佣金比例在 0.02%~0.03%之间");
        return false;
    }
    return true;
}

/**
 * 验证交易地区，用于处理 通讯费
 * @param tradingArea
 * @returns {boolean}
 */
function validateTradingArea(tradingArea){
    //验证想赚的钱数
    if(isEmpty(tradingArea)){
        Flavr.falert("请选择交易的地区");
        return false;
    }
    return true;
}
/**
 * 验证第二次卖出的价格
 * @param secPrice
 * @returns {boolean}
 */
function validateSecPrice(secPrice){
    //验证想赚的钱数
    if(isEmpty(secPrice)){
        Flavr.falert("第二次卖出的价格不能为空");
        return false;
    }
    const regs = /^(\d+)(\.\d+)?$/;
    if(!regs.test(secPrice)){
        Flavr.falert("卖出时的股票价格必须为正数");
        return false;
    }
    return true;
}

/**
 * 验证想要的股票的平均价格
 * @param price
 * @returns {boolean}
 */
function validateMakePrice(price){
    //验证股票价格
    if(isEmpty(price)){
        Flavr.falert("想要的股票平均价格不能为空");
        return false;
    }
    const regs = /^(\d+)(\.\d+)?$/;
    if(!regs.test(price)){
        Flavr.falert("想要的股票价格必须为正数");
        return false;
    }
    return true;
}

/**
 * 展示结果的相关信息
 * @param data
 */
function showInfo(result){
    //展示手续费等信息
    let calcBuyMoneyVoList=result.calcBuyMoneyVoList;
    $("#buyCharge1").text(calcBuyMoneyVoList[0].buyCharge);
    $("#buyTransferFee1").text(calcBuyMoneyVoList[0].buyTransferFee);
    $("#buyCommunications1").text(calcBuyMoneyVoList[0].buyCommunications);
    $("#dealMoney1").text(calcBuyMoneyVoList[0].dealMoney);
    $("#totalBuyCharge1").text(calcBuyMoneyVoList[0].totalBuyCharge);

    //展示卖出
    let calcSellMoneyVoList=result.calcSellMoneyVoList;
    $("#sellMoney1").text(calcSellMoneyVoList[0].sellMoney);
    $("#totalSellCharge1").text(calcSellMoneyVoList[0].totalSellCharge);
    $("#sellStampDuty1").text(calcSellMoneyVoList[0].sellStampDuty);
    $("#sellCharge1").text(calcSellMoneyVoList[0].sellCharge);
    $("#sellTransferFee1").text(calcSellMoneyVoList[0].sellTransferFee);
    $("#sellCommunications1").text(calcSellMoneyVoList[0].sellCommunications);


    $("#totalCharge").text(result.totalCharge);
    //展示操作的信息
    $("#totalNum").text(result.totalNum);
    $("#totalMoney").text(result.totalMoney);
    $("#avgPrice").text(result.avgPrice);
    $("#nowOperationPrice").text(result.nowOperationPrice);
}

function getText(value){
    return "   "+value+"  ";
}

/*输入代码,展示相关的走势图*/
$("#show").click(function(){
    //将以前的信息清空
    $("#showInfo").find(".info").text("");
    //获取这个股票的相关信息
    var code=$("#code").val();
    let stockInfo=getStockInfo(code);
    if(stockInfo==null){
        Flavr.falert("输入的股票代码不正确");
        return ;
    }
    //进行展示股票的相关信息
    fillShowInfo(stockInfo);
    //将以前的信息清空
    let url=STOCK_KLINE_URL;
    //获取这个股票的相关信息
    const info = getKFillInfo(stockInfo.fullCode,1);
    let base64Result=getStockK(info,url);
    //进行展示股票的K线图
    if(base64Result==null||base64Result.length<1){
        Flavr.falert("输入的股票代码有误");
        return ;
    }
    var base64_gif="data:image/gif;base64,";
    var base64Str=base64_gif+base64Result;
    $("#k_min").attr("src",base64Str);
    $("#min_popup").modal('show');
})

function fillShowInfo(stockInfo){
    $("#showCode").text(stockInfo.code);
    $("#name").text(stockInfo.name);
    $("#openingPrice").text(stockInfo.openingPrice);
    $("#yesClosingPrice").text(stockInfo.yesClosingPrice);
    $("#highestPrice").text(stockInfo.highestPrice);
    $("#lowestPrice").text(stockInfo.lowestPrice);
    $("#closingPrice").text(stockInfo.closingPrice);
    $("#nowPrice").text(stockInfo.nowPrice);
    $("#tradingVolume").text(stockInfo.tradingVolume);
    $("#tradingValue").text(stockInfo.tradingValue);
    $("#amplitude").text(stockInfo.amplitude);
    $("#amplitudeProportion").text(stockInfo.amplitudeProportion);
    $("#peRatio").text(stockInfo.peRatio);
}

/**
 * 股票股票信息的接口
 * @param code
 */
function getStockInfo(code){
    const info = getKFillInfo(code);
    let returnData;
    $.ajax({
        async:false,
        type:"post",
        url:STOCK_CRAWLER_INFO_URL,
        data:JSON.stringify(info),
        dataType:"json",
        contentType:"application/json;charset=utf-8",
        success:function(data){
            console.log("输出获取值:2"+JSON.stringify(data.data))
            returnData= data.data;
        }
    });
    return returnData;
}


/**
 * 股票股票信息的接口
 * @param code
 */
function getStockK(info,url){
    let returnData=null;
    $.ajax({
        async:false,
        type:"post",
        url:url,
        data:JSON.stringify(info),
        dataType:"json",
        contentType:"application/json;charset=utf-8",
        success:function(data){
            returnData= data.data;
        }
    });
    return returnData;
}


/**
 * 获取填充的用户信息
 * @param code
 * @returns 返回填充信息的对象
 */
function getKFillInfo(code,type,exchage){
    const info = {
        "code": code,
        "type":type,
        "exchange":exchage
    };
    return info;
}