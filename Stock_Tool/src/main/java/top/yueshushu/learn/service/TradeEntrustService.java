package top.yueshushu.learn.service;

import top.yueshushu.learn.mode.ro.TradeEntrustRo;
import top.yueshushu.learn.pojo.TradeEntrust;
import com.baomidou.mybatisplus.extension.service.IService;
import top.yueshushu.learn.response.OutputResult;

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
}
