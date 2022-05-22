package top.yueshushu.learn.mapper;

import top.yueshushu.learn.domain.TradePositionDo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 我的持仓表 Mapper 接口
 * </p>
 *
 * @author 两个蝴蝶飞  自定义的
 * @since 2022-01-03
 */
public interface TradePositionMapper extends BaseMapper<TradePositionDo> {
    /**
     * 持仓表里面，更新可用的股票数量
     */
    void syncUseAmountByXxlJob();
}
