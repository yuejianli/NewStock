package top.yueshushu.learn.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import top.yueshushu.learn.mode.ro.TradePositionRo;
import top.yueshushu.learn.mode.vo.StockSelectedRo;
import top.yueshushu.learn.pojo.TradePosition;
import top.yueshushu.learn.response.OutputResult;
import top.yueshushu.learn.service.StockSelectedService;
import top.yueshushu.learn.service.TradePositionService;

/**
 * <p>
 * 我的持仓表 我是自定义的
 * </p>
 *
 * @author 两个蝴蝶飞
 * @date 2022-01-03
 */
@RestController
@RequestMapping("/tradePosition")
@Api("持仓信息")
public class TradePositionController extends BaseController{
    @Autowired
    private TradePositionService tradePositionService;

    @PostMapping("/list")
    @ApiOperation("查询持仓")
    public OutputResult list(@RequestBody TradePositionRo tradePositionRo){
        tradePositionRo.setUserId(getUserId());
        return tradePositionService.listPosition(tradePositionRo);
    }
}
