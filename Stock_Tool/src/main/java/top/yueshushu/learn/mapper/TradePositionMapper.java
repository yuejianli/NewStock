package top.yueshushu.learn.mapper;

import cn.hutool.core.date.DateTime;
import org.apache.ibatis.annotations.Param;
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

    /**
     * 删除当天的已经保存的历史记录信息
     *
     * @param userId   用户编号
     * @param mockType 交易类型
     * @param currDate 当前时间
     */
    void deleteByUserIdAndMockTypeAndDate(@Param("userId") Integer userId, @Param("mockType") Integer mockType,
                                          @Param("currDate") DateTime currDate);
}
