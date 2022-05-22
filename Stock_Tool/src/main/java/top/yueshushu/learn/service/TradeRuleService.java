package top.yueshushu.learn.service;

import top.yueshushu.learn.mode.dto.StockRuleDto;
import top.yueshushu.learn.mode.dto.TradeRuleStockQueryDto;
import top.yueshushu.learn.mode.ro.TradeRuleRo;
import top.yueshushu.learn.domain.TradeRuleDo;
import com.baomidou.mybatisplus.extension.service.IService;
import top.yueshushu.learn.response.OutputResult;

import java.util.List;

/**
 * <p>
 * 交易规则表 自定义的
 * </p>
 *
 * @author 两个蝴蝶飞
 * @since 2022-01-26
 */
public interface TradeRuleService extends IService<TradeRuleDo> {
    /**
     * 查询交易的规则
     * @date 2022/1/27 10:01
     * @author zk_yjl
     * @param tradeRuleRo
     * @return top.yueshushu.learn.response.OutputResult
     */
    OutputResult listRule(TradeRuleRo tradeRuleRo);
    /**
     * 添加交易规则
     * @date 2022/1/26 15:50
     * @author zk_yjl
     * @param tradeRuleRo
     * @return top.yueshushu.learn.response.OutputResult
     */
    OutputResult addRule(TradeRuleRo tradeRuleRo);
    /**
     * 修改交易规则
     * @date 2022/1/27 9:56
     * @author zk_yjl
     * @param tradeRuleRo
     * @return top.yueshushu.learn.response.OutputResult
     */
    OutputResult updateRule(TradeRuleRo tradeRuleRo);
    /**
     * 启用交易规则
     * @date 2022/1/27 10:13
     * @author zk_yjl
     * @param tradeRuleRo
     * @return top.yueshushu.learn.response.OutputResult
     */
    OutputResult enableRule(TradeRuleRo tradeRuleRo);
    /**
     * 禁用交易规则
     * @date 2022/1/27 10:13
     * @author zk_yjl
     * @param tradeRuleRo
     * @return top.yueshushu.learn.response.OutputResult
     */
    OutputResult disableRule(TradeRuleRo tradeRuleRo);
    /**
     * 删除交易规则
     * @date 2022/1/27 20:06
     * @author zk_yjl
     * @param tradeRuleRo
     * @return top.yueshushu.learn.response.OutputResult
     */
    OutputResult deleteRule(TradeRuleRo tradeRuleRo);
    /**
     * 查询对应的规则信息
     * @date 2022/1/28 15:41
     * @author zk_yjl
     * @param tradeRuleStockQueryDto
     * @return java.util.List<top.yueshushu.learn.mode.dto.StockRuleDto>
     */
    List<StockRuleDto> getRuleByQuery(TradeRuleStockQueryDto tradeRuleStockQueryDto);
}
