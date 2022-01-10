package top.yueshushu.learn.service;

import top.yueshushu.learn.mode.ro.TradeEntrustRo;
import top.yueshushu.learn.pojo.TradeEntrust;
import com.baomidou.mybatisplus.extension.service.IService;
import top.yueshushu.learn.response.OutputResult;

import java.util.List;

/**
 * <p>
 * 委托表 自定义的
 * </p>
 *
 * @author 两个蝴蝶飞
 * @since 2022-01-03
 */
public interface TradeEntrustService extends IService<TradeEntrust> {
    /**
     * 查询委托信息
     * @param tradeEntrustRo
     * @return
     */
    OutputResult listEntrust(TradeEntrustRo tradeEntrustRo);
    /**
     * 查看历史委托信息
     * @param tradeEntrustRo
     * @return
     */
    OutputResult history(TradeEntrustRo tradeEntrustRo);

    /**
     * 获取当前的委托单信息,进行进行中的。
     * @param userId
     * @param mockType
     * @return
     */
    List<TradeEntrust> listNowRunEntruct(Integer userId, Integer mockType);
}
