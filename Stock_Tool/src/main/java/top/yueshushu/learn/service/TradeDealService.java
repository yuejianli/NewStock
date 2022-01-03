package top.yueshushu.learn.service;

import top.yueshushu.learn.mode.ro.TradeDealRo;
import top.yueshushu.learn.pojo.TradeDeal;
import com.baomidou.mybatisplus.extension.service.IService;
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
}
