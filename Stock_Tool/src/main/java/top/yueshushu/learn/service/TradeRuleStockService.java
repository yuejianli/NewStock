package top.yueshushu.learn.service;

import top.yueshushu.learn.mode.ro.TradeRuleStockRo;
import top.yueshushu.learn.pojo.TradeRuleStock;
import com.baomidou.mybatisplus.extension.service.IService;
import top.yueshushu.learn.response.OutputResult;

import java.util.List;

/**
 * <p>
 * 规则股票对应信息表 自定义的
 * </p>
 *
 * @author 两个蝴蝶飞
 * @since 2022-01-26
 */
public interface TradeRuleStockService extends IService<TradeRuleStock> {
    /**
     * 查询规则适用的信息
     * @date 2022/1/27 11:46
     * @author zk_yjl
     * @param tradeRuleStockRo
     * @return top.yueshushu.learn.response.OutputResult
     */
    OutputResult applyList(TradeRuleStockRo tradeRuleStockRo);
    /**
     * 配置规则应用信息
     * @date 2022/1/27 15:43
     * @author zk_yjl
     * @param tradeRuleStockRo
     * @return top.yueshushu.learn.response.OutputResult
     */
    OutputResult apply(TradeRuleStockRo tradeRuleStockRo);
    /**
     * 根据规则id 查询适用的股票信息
     * @date 2022/1/27 20:11
     * @author zk_yjl
     * @param ruleId
     * @return java.util.List<top.yueshushu.learn.pojo.TradeRuleStock>
     */
    List<TradeRuleStock> listByRid(Integer ruleId);
    /**
     * 查询股票对应的规则信息
     * @date 2022/1/28 14:26
     * @author zk_yjl
     * @param tradeRuleStockRo
     * @return top.yueshushu.learn.response.OutputResult
     */
    OutputResult stockRuleList(TradeRuleStockRo tradeRuleStockRo);
}
