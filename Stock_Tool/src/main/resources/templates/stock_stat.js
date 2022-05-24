$(function () {
    $("#searchCode").val("");
    $("#statisticsBtn").tooltip();
    $("#stockSearch").tooltip();
})


$("#stockSearch").click(function () {
    //获取当前的股票记录值.
    let stockCode = $("#searchCode").val();
    if (!validateCode(stockCode)) {
        return;
    }
    //根据股票的代码进行查询股票的相关记录信息。
    fillStockBaseInfo(stockCode);
    //获取股票的近一月的信息
    fillStockWeekInfo(stockCode);
})


function fillStockBaseInfo(stockCode) {
    let info = {
        "code": stockCode
    }
    let stockInfo = getStockInfo(info);
    //填充信息
    $("#code").text(stockInfo.code);
    $("#name").text(stockInfo.name);
    $("#openingPrice").text(stockInfo.openingPrice);
    $("#nowPrice").text(stockInfo.nowPrice);
    $("#highestPrice").text(stockInfo.highestPrice);
    $("#lowestPrice").text(stockInfo.lowestPrice);
    $("#tradingVolume").text(stockInfo.tradingVolume);
    $("#tradingValue").text(stockInfo.tradingValue);
    $("#amplitude").text(stockInfo.amplitude);
    $("#amplitudeProportion").text(stockInfo.amplitudeProportion);

}

/**
 * 股票股票信息的接口
 * @param code
 */
function getStockInfo(info) {
    let returnData;
    $.ajax({
        async: false,
        type: "post",
        url: STOCK_CRAWLER_INFO_URL,
        data: JSON.stringify(info),
        dataType: "json",
        contentType: "application/json;charset=utf-8",
        success: function (data) {
            returnData = data.data;
        }
    });
    return returnData;
}

/**
 * 股票股票这一个月的周统计结果
 * @param info
 * @returns {undefined}
 */
function getStockWeekStaInfo(info) {
    let returnData = null;
    $.ajax({
        async: false,
        type: "post",
        url: STAT_WEEK_URL,
        data: JSON.stringify(info),
        dataType: "json",
        contentType: "application/json;charset=utf-8",
        success: function (data) {
            returnData = data.data;
        }
    });
    return returnData;
}

function fillStockWeekInfo(stockCode) {
    let info = {
        "code": stockCode
    }
    let stockWeekStaInfo = getStockWeekStaInfo(info);
    $("#weekStatBody").empty();
    //如果不为空的话
    var weekList = stockWeekStaInfo.weekStatInfoList || [];
    if (weekList.length <= 0) {
        $("#weekStatBody").html("同步出错,请联系管理 员");
        return;
    }
    for (let keyIndex in weekList) {
        let weekInfo = weekList[keyIndex];
        let tdClass = "kui";
        if (weekInfo.rangePrice > 0) {
            tdClass = "zhuan";
        }
        $("#weekStatBody").append(
            "<tr>" +
            "    <td>" + weekInfo.typeName + ":</td>\n" +
            "    <td class='" + tdClass + "'>" + weekInfo.rangePrice + ":</td>\n" +
            "    <td class='" + tdClass + "'>" + weekInfo.rangeProportion + ":</td>\n" +
            "    </tr>"
        );
    }
}


function getInfo() {
    let info = {
        "code": $("#searchCode").val()
    }
    return info;
}


/**
 * 验证股票的编号
 * @param code 股票的编号
 * @returns {boolean} 验证通过，返回 true, 验证不通过，返回false
 */
function validateCode(code) {
    //验证股票代码
    if (isEmpty(code)) {
        Flavr.falert("股票代码不能为空");
        return false;
    }
    //获取前两位的值
    if (code.length != 6 || !/^\d{6}$/.test(code)) {
        Flavr.falert("股票的代码编号必须为六位纯数字");
        return false;
    }
    return true;
}


$("#staDateRange").daterangepicker({
        showDropdowns: true,
        autoUpdateInput: false,
        "locale": {
            format: 'YYYY-MM-DD',
            applyLabel: "应用",
            cancelLabel: "取消",
            resetLabel: "重置",
        }
    },
    function (start, end, label) {
        if (!this.startDate) {
            this.element.val('');
        } else {
            this.element.val(this.startDate.format(this.locale.format));
        }
    }
);
$('#staDateRange').on('apply.daterangepicker', function (ev, picker) {
    $("#staStartDate").val(picker.startDate.format('YYYY-MM-DD'));
    $("#staEndDate").val(picker.endDate.format('YYYY-MM-DD'));
});

//更改选取器的选定日期范围
var now = new Date(new Date().setDate(new Date().getDate() - 1));
var oneMonthBefore = new Date(new Date().setDate(new Date().getDate() - 31));
$('#staDateRange').data('daterangepicker').setStartDate(oneMonthBefore);
$('#staDateRange').data('daterangepicker').setEndDate(now);
$("#staDateRange").val(formatDate(oneMonthBefore) + "/" + formatDate(now));
$("#staStartDate").val(formatDate(oneMonthBefore));
$("#staEndDate").val(formatDate(now));


function formatDate(shijian) {
    let date = new Date(shijian)
    var y = date.getFullYear();
    var m = date.getMonth() + 1;
    m = m < 10 ? ('0' + m) : m;
    var d = date.getDate();
    d = d < 10 ? ('0' + d) : d;
    return y + '-' + m + '-' + d;
};

//判断字符是否为空的方法
function isEmpty(obj) {
    if (typeof obj == "undefined" || obj == null || obj == "") {
        return true;
    } else {
        return false;
    }
}

// 基于准备好的dom，初始化echarts实例
var chartDom = document.getElementById('stockCharts');
var myChart = echarts.init(chartDom);


$("#statisticsBtn").click(function () {
    //验证一下股票的编码信息
    //获取当前的股票记录值.
    let stockCode = $("#searchCode").val();
    if (!validateCode(stockCode)) {
        return;
    }
    //获取日期范围的信息
    let startDate = $("#staStartDate").val();
    if (isEmpty(startDate)) {
        Flavr.falert("开始日期不能为空");
    }
    let endDate = $("#staEndDate").val();
    if (isEmpty(endDate)) {
        Flavr.falert("结束日期不能为空");
    }
    let info = {
        "code": stockCode,
        "startDate": startDate,
        "endDate": endDate
    }
    //获取历史交易记录信息
    let charResult = getStockCharInfo(info);
    if (charResult != null) {
        var option;
        option = {
            title: {
                text: '价格展示图表'
            },
            tooltip: {
                trigger: 'axis'
            },
            legend: {
                data: charResult.legend
            },
            grid: {
                left: '3%',
                right: '4%',
                bottom: '3%',
                containLabel: true
            },
            toolbox: {
                show: true,
                feature: {
                    magicType: {type: ['line', 'bar']},
                    restore: {},
                }
            },
            xAxis: {
                type: 'category',
                boundaryGap: false,
                data: charResult.xaxisData
            },
            yAxis: {
                type: 'value',
                axisLabel: {
                    formatter: '{value} ￥'
                }
            },
            series: convert(charResult.series)

        };

        option && myChart.setOption(option);
    }
})

function convert(series) {
    let convertInfoArr = new Array(series.length);
    for (var i in series) {
        var tempInfo = series[i];
        let mdata = [
            {type: 'max', name: 'Max'},
            {type: 'min', name: 'Min'}
        ];
        let markPoint = new Object();
        markPoint.data = mdata;
        let adata = [{type: 'average', name: 'Avg'}];
        let markLine = new Object();
        markLine.data = adata;
        tempInfo.markPoint = markPoint;
        tempInfo.markLine = markLine;
        convertInfoArr.push(tempInfo);
    }
    return convertInfoArr;
}

/**
 * 股票股票的统计记录信息
 * @param info
 * @returns {undefined}
 */
function getStockCharInfo(info) {
    let returnData;
    $.ajax({
        async: false,
        type: "post",
        url: STAT_CHAR_URL,
        data: JSON.stringify(info),
        dataType: "json",
        contentType: "application/json;charset=utf-8",
        success: function (data) {
            returnData = data.data;
        }
    });
    return returnData;
}
