package top.yueshushu.learn.service;

import top.yueshushu.learn.mode.ro.TradeMoneyRo;
import top.yueshushu.learn.pojo.TradeMoney;
import com.baomidou.mybatisplus.extension.service.IService;
import top.yueshushu.learn.response.OutputResult;

/**
 * <p>
 * 资金表 自定义的
 * </p>
 *
 * @author 两个蝴蝶飞
 * @since 2022-01-03
 */
public interface TradeMoneyService extends IService<TradeMoney> {
    /**
     * 查询资产信息
     * @param tradeMoneyRo
     * @return
     */
    OutputResult listMoney(TradeMoneyRo tradeMoneyRo);
}
