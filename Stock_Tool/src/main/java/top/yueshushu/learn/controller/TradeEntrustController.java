package top.yueshushu.learn.controller;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import top.yueshushu.learn.mode.ro.TradeEntrustRo;
import top.yueshushu.learn.response.OutputResult;
import top.yueshushu.learn.service.TradeEntrustService;

/**
 * <p>
 * 委托表 我是自定义的
 * </p>
 *
 * @author 两个蝴蝶飞
 * @date 2022-01-03
 */
@RestController
@RequestMapping("/tradeEntrust")
@ApiModel("委托信息")
public class TradeEntrustController extends BaseController {
    @Autowired
    private TradeEntrustService tradeEntrustService;

    @PostMapping("/list")
    @ApiOperation("查询今日委托信息")
    public OutputResult list(@RequestBody TradeEntrustRo tradeEntrustRo){
        tradeEntrustRo.setUserId(getUserId());
        return tradeEntrustService.listEntrust(tradeEntrustRo);
    }

    @PostMapping("/history")
    @ApiOperation("查询历史委托信息")
    public OutputResult history(@RequestBody TradeEntrustRo tradeEntrustRo){
        tradeEntrustRo.setUserId(getUserId());
        return tradeEntrustService.history(tradeEntrustRo);
    }

}
