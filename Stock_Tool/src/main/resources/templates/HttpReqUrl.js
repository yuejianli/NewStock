var contextPath ="";
/*********************登录的请求接口****************************/
//用户登录
var LOGIN_URL = contextPath+"user/login";

/*********************交易用户请求接口****************************/
//获取验证码
var TRADE_USER_LOGIN_YZM_URL = contextPath +"tradeMethod/yzm";
//交易用户登录
var TRADE_USER_LOGIN_URL = contextPath+"tradeUser/login";

/*********************股票页面****************************/
//展示股票列表
var STOCK_LIST_URL =contextPath +"stock/list";
//展示股票历史列表
var STOCK_HISTORY_LIST_URL =contextPath +"stockHistory/history";
//显示股票当前的实时信息
var STOCK_CRAWLER_INFO_URL =contextPath +"stockCrawler/getStockInfo";
//显示股票信息
var STOCK_INFO_URL =contextPath +"stock/stockInfo";
//展示股票K线
var STOCK_KLINE_URL =contextPath +"stockCrawler/getStockKline";
//股票同步
var STOCK_ASYNC_URL =contextPath +"stockCrawler/stockAsync";
//股票历史同步
var STOCK_HISTORY_ASYNC_URL =contextPath +"stockCrawler/stockHistoryAsync";
//股票加入自选
var STOCK_SELECTED_ADD_URL =contextPath +"stockSelected/add";


/*********************股票自选功能***************************/
//查询自选列表
var STOCK_SELECTED_LIST_URL =contextPath +"stockSelected/list";
var STOCK_SELECTED_DELETE_URL =contextPath +"stockSelected/deleteByCode";
var STOCK_SELECTED_BATCH_DELETE_URL =contextPath +"stockSelected/delete";
var STOCK_SELECTED_ADD_URL =contextPath +"stockSelected/add";

/*********************股票查看历史功能***************************/
//展示股票历史列表
var STOCK_SELECTED_YES_LIST_URL =contextPath +"stockSelected/yesHistory";

/*********************假期功能***************************/
// 假期查询功能
var HOLIDAY_LIST_URL =contextPath +"holidayCalendar/list";
// 假期查询功能
var HOLIDAY_ASYNC_URL =contextPath +"holidayCalendar/sync";

/*********************全局配置参数功能***************************/
// 配置参数查询功能
var CONFIG_LIST_URL =contextPath +"config/list";
// 配置参数重置功能
var CONFIG_RESET_URL =contextPath +"config/reset";
// 配置参数更新功能
var CONFIG_UPDATE_URL =contextPath +"config/update";


/*********************缓存配置功能***************************/
// 配置参数查询功能
var CACHE_LIST_URL =contextPath +"cache/list";
// 配置参数重置功能
var CACHE_DELETE_URL =contextPath +"cache/delete";
// 配置参数更新功能
var CACHE_UPDATE_URL =contextPath +"cache/update";



/*********************股票小工具***************************/
// 清仓工具
var TOOL_MONEY_CLEAR =contextPath +"money/clearMoney";
//补仓
var TOOL_MONEY_COVER =contextPath +"money/coverMoney";
//减仓
var TOOL_MONEY_REDUCE =contextPath +"money/reduceMoney";

/*********************股票统计页面***************************/

//周统计
var STAT_WEEK_URL =contextPath +"stat/getWeekStat";
//图表统计
var STAT_CHAR_URL =contextPath +"stat/getCharStat";