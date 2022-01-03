package top.yueshushu.learn.controller;


import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import top.yueshushu.learn.mode.ro.TradeDealRo;
import top.yueshushu.learn.mode.ro.TradePositionRo;
import top.yueshushu.learn.response.OutputResult;
import top.yueshushu.learn.service.TradeDealService;
import top.yueshushu.learn.service.TradePositionService;

/**
 * <p>
 * 成交表 我是自定义的
 * </p>
 *
 * @author 两个蝴蝶飞
 * @date 2022-01-03
 */
@RestController
@RequestMapping("/tradeDeal")
public class TradeDealController extends BaseController{
    @Autowired
    private TradeDealService tradeDealService;

    @PostMapping("/list")
    @ApiOperation("查询今日成交")
    public OutputResult list(@RequestBody TradeDealRo tradeDealRo){
        tradeDealRo.setUserId(getUserId());
        return tradeDealService.listDeal(tradeDealRo);
    }
}
