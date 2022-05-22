package top.yueshushu.learn.stock.business;

import top.yueshushu.learn.response.OutputResult;
import top.yueshushu.learn.ro.stock.StockRo;

/**
 * @Description stock 股票 的编排层处理
 * @Author 岳建立
 * @Date 2022/5/20 22:33
 **/
public interface StockBusiness {
    /**
     * 根据股票的编码获取当前的股票信息
     *
     * @param stockCode 股票编码
     * @return 根据股票的编码获取当前的股票信息
     */
    OutputResult getStockInfo(String stockCode);

    /**
     * 根据股票的编码和K线图类型获取当前的K线图
     *
     * @param stockRo 股票对象
     * @return 根据股票的编码和K线图类型获取当前的K线图
     */
    OutputResult getStockKline(StockRo stockRo);

    /**
     * 股票信息全量同步
     *
     * @param stockRo 股票对象
     * @return 股票信息全量同步
     */
    OutputResult stockAsync(StockRo stockRo);

    /**
     * 获取股票的价格
     *
     * @param code 股票的编码
     * @return 获取股票的价格
     */
    OutputResult getStockPrice(String code);
}
