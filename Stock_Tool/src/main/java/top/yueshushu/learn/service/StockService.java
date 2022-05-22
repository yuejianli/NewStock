package top.yueshushu.learn.service;

import top.yueshushu.learn.entity.Stock;
import top.yueshushu.learn.mode.vo.StockVo;
import top.yueshushu.learn.model.info.StockInfo;
import top.yueshushu.learn.response.OutputResult;
import top.yueshushu.learn.ro.stock.StockRo;

/**
 * <p>
 * 股票信息基本表 自定义的
 * </p>
 *
 * @author 岳建立
 * @since 2022-01-02
 */
public interface StockService {
    /**
     * 分页查询股票记录
     * @param stockRo 股票查询对象
     * @return 返回分页查询股票的记录信息
     */
    OutputResult<StockInfo> pageStock(StockRo stockRo);

    /**
     * 查询股票的信息
     * @param code
     * @return
     */
    Stock selectByCode(String code);

    /**
     * 是否存在着这个股票
     * @param code
     * @return
     */
    boolean existStockCode(String code);

    /**
     * 根据股票的编码，查询股票的信息
     * @param code 股票的编码
     * @return 根据股票的编码，查询股票的信息
     */
    OutputResult<StockVo> getStockInfo(String code);
}
