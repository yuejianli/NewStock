package top.yueshushu.learn.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.yueshushu.learn.business.TradePositionBusiness;
import top.yueshushu.learn.common.ResultCode;
import top.yueshushu.learn.enumtype.MockType;
import top.yueshushu.learn.mode.ro.TradePositionRo;
import top.yueshushu.learn.response.OutputResult;

import javax.annotation.Resource;

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
    @Resource
    private TradePositionBusiness tradePositionBusiness;

    @PostMapping("/list")
    @ApiOperation("查询当前我的持仓")
    public OutputResult list(@RequestBody TradePositionRo tradePositionRo){
        tradePositionRo.setUserId(getUserId());
        if (tradePositionRo.getMockType()==null){
            return OutputResult.buildFail(ResultCode.TRADE_MOCK_TYPE_IS_EMPTY);
        }
        MockType mockType = MockType.getMockType(tradePositionRo.getMockType());
        if (mockType == null){
            return OutputResult.buildFail(ResultCode.TRADE_MOCK_TYPE_NOT_EXIST);
        }
        if (MockType.MOCK.equals(mockType)){
            return tradePositionBusiness.mockList(tradePositionRo);
        }
        return tradePositionBusiness.realList(tradePositionRo);
    }
}
