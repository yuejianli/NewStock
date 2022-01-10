package top.yueshushu.learn.service;

import top.yueshushu.learn.mode.ro.DealRo;
import top.yueshushu.learn.mode.ro.TradeDealRo;
import top.yueshushu.learn.pojo.TradeDeal;
import com.baomidou.mybatisplus.extension.service.IService;
import top.yueshushu.learn.pojo.TradeEntrust;
import top.yueshushu.learn.response.OutputResult;

/**
 * <p>
 * 成交表 自定义的
 * </p>
 *
 * @author 两个蝴蝶飞
 * @since 2022-01-03
 */
public interface TradeDealService extends IService<TradeDeal> {
    /**
     * 查询今日成交信息
     * @param tradeDealRo
     * @return
     */
    OutputResult listDeal(TradeDealRo tradeDealRo);

    /**
     * 添加一条成交记录到成交表里面
     * @param tradeEntrust
     */
    void addDealRecord(TradeEntrust tradeEntrust);

    /**
     * 查询历史成交记录信息
     * @param tradeDealRo
     * @return
     */
    OutputResult history(TradeDealRo tradeDealRo);

}
