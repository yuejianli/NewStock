package top.yueshushu.learn.stock.business;

import top.yueshushu.learn.response.OutputResult;
import top.yueshushu.learn.ro.stock.StockRo;

/**
 * @Description stock 股票历史 的编排层处理
 * @Author 岳建立
 * @Date 2022/5/20 22:33
 **/
public interface StockHistoryBusiness {
    /**
     * 同步股票的历史数据
     *
     * @param stockRo 股票对象，包括编码和开始，结束日期
     * @return 同步股票的历史数据
     */
    OutputResult stockHistoryAsync(StockRo stockRo);
}
