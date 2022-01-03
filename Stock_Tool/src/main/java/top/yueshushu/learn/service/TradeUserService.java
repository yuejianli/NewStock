package top.yueshushu.learn.service;

import top.yueshushu.learn.mode.ro.TradeUserRo;
import top.yueshushu.learn.pojo.TradeUser;
import com.baomidou.mybatisplus.extension.service.IService;
import top.yueshushu.learn.response.OutputResult;

/**
 * <p>
 * 交易用户信息 自定义的
 * </p>
 *
 * @author 岳建立
 * @since 2022-01-02
 */
public interface TradeUserService extends IService<TradeUser> {
    /**
     * 交易用户登录信息
     * @param tradeUserRo
     * @return
     */
    OutputResult login(TradeUserRo tradeUserRo);

    /**
     * 根据登录的用户id, 获取对应的 交易用户信息
     * @param userId
     * @return
     */
    TradeUser getTradeUserById(int userId);
}
