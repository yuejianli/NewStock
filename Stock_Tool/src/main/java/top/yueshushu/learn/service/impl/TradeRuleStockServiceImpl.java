package top.yueshushu.learn.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import top.yueshushu.learn.enumtype.DealType;
import top.yueshushu.learn.mode.dto.StockRuleDto;
import top.yueshushu.learn.mode.dto.TradeRuleStockQueryDto;
import top.yueshushu.learn.mode.ro.TradeRuleStockRo;
import top.yueshushu.learn.mode.vo.StockRuleVo;
import top.yueshushu.learn.mode.vo.StockSelectedVo;
import top.yueshushu.learn.mode.vo.TradeRuleStockVo;
import top.yueshushu.learn.pojo.TradeRule;
import top.yueshushu.learn.pojo.TradeRuleStock;
import top.yueshushu.learn.mapper.TradeRuleStockMapper;
import top.yueshushu.learn.response.OutputResult;
import top.yueshushu.learn.response.ResultCode;
import top.yueshushu.learn.service.StockSelectedService;
import top.yueshushu.learn.service.TradeRuleService;
import top.yueshushu.learn.service.TradeRuleStockService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 规则股票对应信息表 自定义的
 * </p>
 *
 * @author 两个蝴蝶飞
 * @since 2022-01-26
 */
@Service
public class TradeRuleStockServiceImpl extends ServiceImpl<TradeRuleStockMapper, TradeRuleStock> implements TradeRuleStockService {
    @Autowired
    private StockSelectedService stockSelectedService;
    @Autowired
    private TradeRuleService tradeRuleService;
    @Autowired
    private TradeRuleStockMapper tradeRuleStockMapper;
    @Override
    public OutputResult applyList(TradeRuleStockRo tradeRuleStockRo) {
        //根据id 去查询
        if(tradeRuleStockRo.getId()==null){
            return OutputResult.success(new TradeRuleStockVo());
        }
        TradeRule dbTradeRule = tradeRuleService.getById(tradeRuleStockRo.getId());
        if(null==dbTradeRule){
            return OutputResult.alert("传入的Id规则编号不正确");
        }
        if(!dbTradeRule.getUserId().equals(tradeRuleStockRo.getUserId())){
            return OutputResult.alert("你无法配置别人的规则信息");
        }
        //查询所有的自选股票信息.
        List<StockSelectedVo> allSelectedStockList = stockSelectedService.listSelf(
                dbTradeRule.getUserId(),""
        );
        //进行对应
        if(CollectionUtils.isEmpty(allSelectedStockList)){
            return OutputResult.success();
        }
        Map<String, String> stockNameMap = allSelectedStockList.stream().collect(
                Collectors.toMap(
                        StockSelectedVo::getStockCode,
                        StockSelectedVo::getStockName
                )
        );
        //查询当前自选的股票信息
        List<TradeRuleStock> ruleStock = listByRid(tradeRuleStockRo.getId());
        //处理成相应的 Vo 形式
        List<StockSelectedVo> applyList=ruleStockToStockVo(stockNameMap, ruleStock);
        TradeRuleStockQueryDto tradeRuleStockQueryDto = new TradeRuleStockQueryDto();
        tradeRuleStockQueryDto.setUserId(tradeRuleStockRo.getUserId());
        tradeRuleStockQueryDto.setRuleId(dbTradeRule.getId());
        tradeRuleStockQueryDto.setRuleType(dbTradeRule.getRuleType());
        tradeRuleStockQueryDto.setMockType(dbTradeRule.getMockType());
        List<TradeRuleStock> other = tradeRuleStockMapper.listNoRid(
                tradeRuleStockQueryDto
        );
        List<StockSelectedVo> otherApplyList =  ruleStockToStockVo(stockNameMap, other);
        //设置信息
        TradeRuleStockVo tradeRuleStockVo = new TradeRuleStockVo();
        tradeRuleStockVo.setAllList(allSelectedStockList);
        if(!CollectionUtils.isEmpty(applyList)){
            tradeRuleStockVo.setApplyList(applyList);
        }
        if(!CollectionUtils.isEmpty(otherApplyList)){
            tradeRuleStockVo.setOtherApplyList(otherApplyList);
        }
        return OutputResult.success(tradeRuleStockVo);
    }

    @Override
    public OutputResult apply(TradeRuleStockRo tradeRuleStockRo) {
        //根据id 去查询
        if(tradeRuleStockRo.getId()==null){
            return OutputResult.alert(ResultCode.ALERT);
        }
        TradeRule dbTradeRule = tradeRuleService.getById(tradeRuleStockRo.getId());
        if(null==dbTradeRule){
            return OutputResult.alert("传入的Id规则编号不正确");
        }
        if(!dbTradeRule.getUserId().equals(tradeRuleStockRo.getUserId())){
            return OutputResult.alert("你无法配置别人的规则信息");
        }
        //先将以前的该规则配置的股票全部删除
        LambdaQueryWrapper<TradeRuleStock>lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(TradeRuleStock::getRuleId,tradeRuleStockRo.getId());
        tradeRuleStockMapper.delete(lambdaQueryWrapper);

        //如果有其他的移除的，就将移除的去掉.
        if(!CollectionUtils.isEmpty(tradeRuleStockRo.getRemoveCodeList())){
            //进行移除。
            tradeRuleStockMapper.removeOtherStock(
                    dbTradeRule.getUserId(),
                    dbTradeRule.getMockType(),
                    tradeRuleStockRo.getRemoveCodeList()
            );
        }
        Date now = DateUtil.date();
        //进行添加操作
        if(!CollectionUtils.isEmpty(tradeRuleStockRo.getApplyCodeList())){
           List<TradeRuleStock> tradeRuleStockList = new ArrayList<>();
           for(String stockCode:tradeRuleStockRo.getApplyCodeList()){

               TradeRuleStock tradeRuleStock = new TradeRuleStock();
               //进行设置
               tradeRuleStock.setRuleId(tradeRuleStockRo.getId());
               tradeRuleStock.setStockCode(stockCode);
               tradeRuleStock.setCreateTime(now);
               tradeRuleStockList.add(tradeRuleStock);
           }
           saveBatch(tradeRuleStockList);
        }
        return OutputResult.success();
    }

    @Override
    public List<TradeRuleStock> listByRid(Integer ruleId) {
        return this.lambdaQuery()
                .eq(TradeRuleStock::getRuleId,ruleId)
                .orderByDesc(TradeRuleStock::getId)
                .list();
    }

    @Override
    public OutputResult stockRuleList(TradeRuleStockRo tradeRuleStockRo) {
        //查询相应的股票信息信息
        List<StockSelectedVo> stockSelectedVoList = stockSelectedService.listSelf(tradeRuleStockRo.getUserId(),
                tradeRuleStockRo.getKeyword());
        if(CollectionUtils.isEmpty(stockSelectedVoList)){
            return OutputResult.success();
        }
        //每一个股票，去查询对应的信息
        List<StockRuleVo> stockRuleVoList = new ArrayList<>();
        for(StockSelectedVo stockSelectedVo:stockSelectedVoList){
            StockRuleVo  stockRuleVo = new StockRuleVo();
            stockRuleVo.setStockCode(stockSelectedVo.getStockCode());
            stockRuleVo.setStockName(stockSelectedVo.getStockName());
            //设置买入的规则
            TradeRuleStockQueryDto tradeRuleStockQueryDto = new TradeRuleStockQueryDto();
            tradeRuleStockQueryDto.setUserId(tradeRuleStockRo.getUserId());
            tradeRuleStockQueryDto.setMockType(tradeRuleStockRo.getMockType());
            tradeRuleStockQueryDto.setRuleType(DealType.BUY.getCode());
            tradeRuleStockQueryDto.setStockCode(stockSelectedVo.getStockCode());
            List<StockRuleDto> stockRuleDtoList = tradeRuleService.getRuleByQuery(tradeRuleStockQueryDto);
            //如果不为空的话，获取第一个
            if(!CollectionUtils.isEmpty(stockRuleDtoList)){
                StockRuleDto stockRuleDto = stockRuleDtoList.get(0);
                //处理信息
                stockRuleVo.setBuyRuleId(stockRuleDto.getRuleId());
                stockRuleVo.setBuyRuleName(stockRuleDto.getRuleName());
                stockRuleVo.setBuyCreateTime(stockRuleDto.getCreateTime());
                stockRuleVo.setBuyRuleStatus(stockRuleDto.getStatus());
            }
            tradeRuleStockQueryDto.setRuleType(DealType.SELL.getCode());
            stockRuleDtoList = tradeRuleService.getRuleByQuery(tradeRuleStockQueryDto);
            //如果不为空的话，获取第一个
            if(!CollectionUtils.isEmpty(stockRuleDtoList)){
                StockRuleDto stockRuleDto = stockRuleDtoList.get(0);
                //处理信息
                stockRuleVo.setSellRuleId(stockRuleDto.getRuleId());
                stockRuleVo.setSellRuleName(stockRuleDto.getRuleName());
                stockRuleVo.setSellCreateTime(stockRuleDto.getCreateTime());
                stockRuleVo.setSellRuleStatus(stockRuleDto.getStatus());
            }
            //设置卖出的规则
            stockRuleVoList.add(stockRuleVo);
        }
        return OutputResult.success(stockRuleVoList);
    }

    private List<StockSelectedVo> ruleStockToStockVo(Map<String, String> stockNameMap, List<TradeRuleStock> other) {
        List<StockSelectedVo> otherApplyList = new ArrayList<>();
        other.stream().forEach(
                n->{
                    StockSelectedVo stockSelectedVo = new StockSelectedVo();
                    stockSelectedVo.setStockCode(n.getStockCode());
                    stockSelectedVo.setStockName(
                            stockNameMap.getOrDefault(
                                    n.getStockCode(),""
                            )
                    );
                    otherApplyList.add(stockSelectedVo);
                }
        );
        return otherApplyList;
    }
}
