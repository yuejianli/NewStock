package top.yueshushu.learn.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import top.yueshushu.learn.mode.ro.TradeMoneyRo;
import top.yueshushu.learn.response.OutputResult;
import top.yueshushu.learn.service.TradeMoneyService;
import top.yueshushu.learn.service.TradePositionService;

/**
 * <p>
 * 资金表 我是自定义的
 * </p>
 *
 * @author 两个蝴蝶飞
 * @date 2022-01-03
 */
@RestController
@RequestMapping("/tradeMoney")
@Api("交易资产信息")
public class TradeMoneyController extends BaseController{
    @Autowired
    private TradeMoneyService tradeMoneyService;

    @PostMapping("/list")
    @ApiOperation("查询资金信息")
    public OutputResult list(@RequestBody TradeMoneyRo tradeMoneyRo){
        tradeMoneyRo.setUserId(getUserId());
        return tradeMoneyService.listMoney(tradeMoneyRo);
    }
}
