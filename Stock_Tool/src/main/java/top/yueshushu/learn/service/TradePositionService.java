package top.yueshushu.learn.service;

import top.yueshushu.learn.mode.ro.TradePositionRo;
import top.yueshushu.learn.domain.TradePositionDo;
import com.baomidou.mybatisplus.extension.service.IService;
import top.yueshushu.learn.response.OutputResult;

/**
 * <p>
 * 我的持仓表 自定义的
 * </p>
 *
 * @author 两个蝴蝶飞
 * @since 2022-01-03
 */
public interface TradePositionService extends IService<TradePositionDo> {
    /**
     * 查询持仓的信息
     * @param tradePositionRo
     * @return
     */
    OutputResult listPosition(TradePositionRo tradePositionRo);

    /**
     * 获取某个股票的持仓信息
     * @return
     */
    TradePositionDo getPositionByCode(
            Integer userId,
            Integer mockType,
            String code
    );

    /**
     * 同步可用数量
     */
    void syncUseAmountByXxlJob();
}
