package top.yueshushu.learn.service;

import top.yueshushu.learn.pojo.StockHistory;
import com.baomidou.mybatisplus.extension.service.IService;
import top.yueshushu.learn.response.OutputResult;
import top.yueshushu.learn.ro.stock.StockRo;

/**
 * <p>
 * 股票的历史交易记录表 自定义的
 * </p>
 *
 * @author 岳建立
 * @since 2022-01-02
 */
public interface StockHistoryService extends IService<StockHistory> {
    /**
     * 查询股票的历史记录
     * @param stockRo
     * @return
     */
    OutputResult history(StockRo stockRo);

}
