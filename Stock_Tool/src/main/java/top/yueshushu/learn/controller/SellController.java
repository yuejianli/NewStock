package top.yueshushu.learn.controller;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.yueshushu.learn.mode.ro.BuyRo;
import top.yueshushu.learn.mode.ro.SellRo;
import top.yueshushu.learn.response.OutputResult;
import top.yueshushu.learn.service.BuyService;
import top.yueshushu.learn.service.SellService;

/**
 * <p>
 * 卖出股票处理
 * </p>
 *
 * @author 两个蝴蝶飞
 * @date 2022-01-03
 */
@RestController
@RequestMapping("/sell")
@ApiModel("卖出股票处理")
public class SellController extends BaseController {
    @Autowired
    private SellService sellService;
    @PostMapping("/sell")
    @ApiOperation("卖出股票信息")
    public OutputResult sell(@RequestBody SellRo sellRo){
        sellRo.setUserId(getUserId());
        return sellService.sell(sellRo);
    }


}
