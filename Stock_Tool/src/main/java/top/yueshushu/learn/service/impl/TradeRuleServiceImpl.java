package top.yueshushu.learn.service.impl;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import top.yueshushu.learn.enumtype.ConditionType;
import top.yueshushu.learn.enumtype.DataFlagType;
import top.yueshushu.learn.enumtype.DealType;
import top.yueshushu.learn.mode.dto.StockRuleDto;
import top.yueshushu.learn.mode.dto.TradeRuleStockQueryDto;
import top.yueshushu.learn.mode.ro.TradeRuleConditionRo;
import top.yueshushu.learn.mode.ro.TradeRuleRo;
import top.yueshushu.learn.mode.vo.TradeRuleVo;
import top.yueshushu.learn.page.PageResponse;
import top.yueshushu.learn.pojo.TradeRule;
import top.yueshushu.learn.mapper.TradeRuleMapper;
import top.yueshushu.learn.pojo.TradeRuleCondition;
import top.yueshushu.learn.pojo.TradeRuleStock;
import top.yueshushu.learn.response.OutputResult;
import top.yueshushu.learn.response.ResultCode;
import top.yueshushu.learn.service.TradeRuleConditionService;
import top.yueshushu.learn.service.TradeRuleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.yueshushu.learn.service.TradeRuleStockService;
import top.yueshushu.learn.util.PageUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 交易规则表 自定义的
 * </p>
 *
 * @author 两个蝴蝶飞
 * @since 2022-01-26
 */
@Service
@Slf4j
public class TradeRuleServiceImpl extends ServiceImpl<TradeRuleMapper, TradeRule> implements TradeRuleService {
    @Autowired
    private TradeRuleStockService tradeRuleStockService;
    @Autowired
    private TradeRuleConditionService tradeRuleConditionService;
    @Autowired
    private TradeRuleMapper tradeRuleMapper;
    @Override
    public OutputResult listRule(TradeRuleRo tradeRuleRo) {
        Page<TradeRule> pageInfo =
                lambdaQuery()
                .eq(TradeRule::getUserId, tradeRuleRo.getUserId())
                .eq(TradeRule::getMockType, tradeRuleRo.getMockType())
                .eq(TradeRule::getRuleType, tradeRuleRo.getRuleType())
                .orderByDesc(
                        TradeRule::getUpdateTime
                ).page(
                new Page<>(
                        tradeRuleRo.getPageNum(),
                        tradeRuleRo.getPageSize()
                )
        );
        return OutputResult.success(
                new PageResponse<>(
                        pageInfo.getTotal(),
                       convertVo(pageInfo.getRecords())
                )
        );
    }

    private List<TradeRuleVo> convertVo(List<TradeRule> records) {
        //查询全部的规则信息.
        List<TradeRuleCondition> tradeRuleConditions = tradeRuleConditionService.listAll();
        //转换成对应的Map 形式
        Map<String, String> condtionNameMap = tradeRuleConditions.stream().collect(Collectors.toMap(
                TradeRuleCondition::getCode,
                TradeRuleCondition::getName
        ));
        List<TradeRuleVo> result = new ArrayList<>();
        records.stream().forEach(
                n->{
                    TradeRuleVo tradeRuleVo = new TradeRuleVo();
                    BeanUtils.copyProperties(n,tradeRuleVo);
                    tradeRuleVo.setConditionName(
                            condtionNameMap.getOrDefault(
                                    n.getConditionCode(),""
                            )
                    );
                    result.add(tradeRuleVo);
                }
        );
        return result;
    }

    @Override
    public OutputResult addRule(TradeRuleRo tradeRuleRo) {
        //添加规则
        TradeRule tradeRule = new TradeRule();
        BeanUtils.copyProperties(tradeRuleRo,tradeRule);
        //为禁用状态
        tradeRule.setStatus(DataFlagType.DELETE.getCode());
        //根据不同的类型，设置不同的信息.
        if(DealType.BUY.equals(tradeRuleRo.getRuleType())){
             //是买入规则
            tradeRule.setConditionType(ConditionType.LT.getCode());
        }else{
            //是卖出规则
            tradeRule.setConditionType(ConditionType.GT.getCode());
        }
        tradeRule.setCreateTime(DateUtil.date());
        tradeRule.setFlag(DataFlagType.NORMAL.getCode());
        //进行添加规则
        save(tradeRule);
        return OutputResult.success();
    }

    @Override
    public OutputResult updateRule(TradeRuleRo tradeRuleRo) {
        OutputResult outputResult = validRule(tradeRuleRo);
        if(!outputResult.getSuccess()){
            return outputResult;
        }
        TradeRule dbRule = (TradeRule) outputResult.getData().get("result");
        //进行更新
        dbRule.setName(tradeRuleRo.getName());
        dbRule.setConditionCode(tradeRuleRo.getConditionCode());
        dbRule.setRuleValueType(tradeRuleRo.getRuleValueType());
        dbRule.setRuleValue(tradeRuleRo.getRuleValue());
        dbRule.setTradeNum(tradeRuleRo.getTradeNum());
        dbRule.setTradeValueType(tradeRuleRo.getTradeValueType());
        dbRule.setTradePrice(tradeRuleRo.getTradePrice());
        dbRule.setUpdateTime(DateUtil.date());
        updateById(dbRule);
        return OutputResult.success();
    }

    @Override
    public OutputResult enableRule(TradeRuleRo tradeRuleRo) {
        OutputResult outputResult = validRule(tradeRuleRo);
        if(!outputResult.getSuccess()){
            return outputResult;
        }
        TradeRule dbRule = (TradeRule) outputResult.getData().get("result");
        dbRule.setStatus(DataFlagType.NORMAL.getCode());
        dbRule.setUpdateTime(DateUtil.date());
        updateById(dbRule);
        return OutputResult.success();
    }

    @Override
    public OutputResult disableRule(TradeRuleRo tradeRuleRo) {
        OutputResult outputResult = validRule(tradeRuleRo);
        if(!outputResult.getSuccess()){
            return outputResult;
        }
        TradeRule dbRule = (TradeRule) outputResult.getData().get("result");
        dbRule.setStatus(DataFlagType.DELETE.getCode());
        dbRule.setUpdateTime(DateUtil.date());
        updateById(dbRule);
        return OutputResult.success();
    }

    @Override
    public OutputResult deleteRule(TradeRuleRo tradeRuleRo) {
        OutputResult outputResult = validRule(tradeRuleRo);
        if(!outputResult.getSuccess()){
            return outputResult;
        }
        TradeRule dbRule = (TradeRule) outputResult.getData().get("result");
        if(DataFlagType.NORMAL.getCode().equals(dbRule.getStatus())){
            return OutputResult.alert("禁用状态下，才可以删除规则信息");
        }
        //看是否有对应的适用股票信息
        List<TradeRuleStock> tradeRuleStockList = tradeRuleStockService.listByRid(tradeRuleRo.getId());
        if(!CollectionUtils.isEmpty(tradeRuleStockList)){
            return OutputResult.alert("有正在配置的股票信息，无法删除");
        }
        dbRule.setStatus(DataFlagType.DELETE.getCode());
        dbRule.setFlag(DataFlagType.DELETE.getCode());
        dbRule.setUpdateTime(DateUtil.date());
        removeById(dbRule.getId());
        return OutputResult.success();
    }

    @Override
    public List<StockRuleDto> getRuleByQuery(TradeRuleStockQueryDto tradeRuleStockQueryDto) {
        return tradeRuleMapper.getRuleByQuery(tradeRuleStockQueryDto);
    }

    private OutputResult validRule(TradeRuleRo tradeRuleRo){
        if(tradeRuleRo.getId()==null){
            return OutputResult.alert(ResultCode.ALERT);
        }
        //根据id 去查询对应的信息
        TradeRule dbRule = getById(tradeRuleRo.getId());
        if(dbRule==null){
            return OutputResult.alert("传入的Id编号不正确");
        }
        if(!dbRule.getUserId().equals(tradeRuleRo.getUserId())){
            log.error("用户{} 试图修改别人 {} 的交易信息",tradeRuleRo.getUserId(),dbRule.getUserId());
            return OutputResult.alert("你无权修改别人的交易规则信息");
        }
        return OutputResult.success(dbRule);
    }
}
