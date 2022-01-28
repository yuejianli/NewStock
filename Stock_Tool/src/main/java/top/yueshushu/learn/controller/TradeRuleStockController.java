package top.yueshushu.learn.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import top.yueshushu.learn.mode.ro.TradeRuleRo;
import top.yueshushu.learn.mode.ro.TradeRuleStockRo;
import top.yueshushu.learn.response.OutputResult;
import top.yueshushu.learn.service.TradeRuleStockService;

/**
 * <p>
 * 规则股票对应信息表 我是自定义的
 * </p>
 *
 * @author 两个蝴蝶飞
 * @date 2022-01-26
 */
@RestController
@RequestMapping("/tradeRuleStock")
@Api("规则配置的股票信息")
public class TradeRuleStockController extends BaseController{
    @Autowired
    private TradeRuleStockService tradeRuleStockService;
    @PostMapping("/applyList")
    @ApiOperation("查询该规则适用的股票信息")
    public OutputResult applyList(@RequestBody TradeRuleStockRo tradeRuleStockRo){
        tradeRuleStockRo.setUserId(getUserId());
        return tradeRuleStockService.applyList(tradeRuleStockRo);
    }

    @PostMapping("/apply")
    @ApiOperation("规则配置股票信息")
    public OutputResult apply(@RequestBody TradeRuleStockRo tradeRuleStockRo){
        tradeRuleStockRo.setUserId(getUserId());
        return tradeRuleStockService.apply(tradeRuleStockRo);
    }

    @PostMapping("/stockRuleList")
    @ApiOperation("查询股票配置的规则信息")
    public OutputResult stockRuleList(@RequestBody TradeRuleStockRo tradeRuleStockRo){
        tradeRuleStockRo.setUserId(getUserId());
        return tradeRuleStockService.stockRuleList(tradeRuleStockRo);
    }
}
