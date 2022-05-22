package top.yueshushu.learn.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import top.yueshushu.learn.mode.ro.TradeRuleRo;
import top.yueshushu.learn.response.OutputResult;
import top.yueshushu.learn.service.TradeRuleService;

/**
 * <p>
 * 交易规则表 我是自定义的
 * </p>
 *
 * @author 两个蝴蝶飞
 * @date 2022-01-26
 */
@RestController
@RequestMapping("/tradeRule")
@Api("交易规则处理")
public class TradeRuleController extends BaseController{
    @Autowired
    private TradeRuleService tradeRuleService;

    @PostMapping("/list")
    @ApiOperation("查询交易规则")
    public OutputResult list(@RequestBody TradeRuleRo tradeRuleRo){
        tradeRuleRo.setUserId(getUserId());
        return tradeRuleService.listRule(tradeRuleRo);
    }

    @PostMapping("/add")
    @ApiOperation("添加交易规则")
    public OutputResult add(@RequestBody TradeRuleRo tradeRuleRo){
        tradeRuleRo.setUserId(getUserId());
        return tradeRuleService.addRule(tradeRuleRo);
    }
    @PostMapping("/update")
    @ApiOperation("修改交易规则")
    public OutputResult update(@RequestBody TradeRuleRo tradeRuleRo){
        tradeRuleRo.setUserId(getUserId());
        return tradeRuleService.updateRule(tradeRuleRo);
    }

    @PostMapping("/delete")
    @ApiOperation("删除交易规则")
    public OutputResult delete(@RequestBody TradeRuleRo tradeRuleRo){
        tradeRuleRo.setUserId(getUserId());
        return tradeRuleService.deleteRule(tradeRuleRo);
    }

    @PostMapping("/enable")
    @ApiOperation("启用交易规则")
    public OutputResult enable(@RequestBody TradeRuleRo tradeRuleRo){
        tradeRuleRo.setUserId(getUserId());
        return tradeRuleService.enableRule(tradeRuleRo);
    }

    @PostMapping("/disable")
    @ApiOperation("禁用交易规则")
    public OutputResult disable(@RequestBody TradeRuleRo tradeRuleRo){
        tradeRuleRo.setUserId(getUserId());
        return tradeRuleService.disableRule(tradeRuleRo);
    }
}
