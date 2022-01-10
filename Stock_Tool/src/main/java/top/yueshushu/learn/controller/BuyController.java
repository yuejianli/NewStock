package top.yueshushu.learn.controller;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.yueshushu.learn.mode.ro.BuyRo;
import top.yueshushu.learn.mode.ro.TradeEntrustRo;
import top.yueshushu.learn.response.OutputResult;
import top.yueshushu.learn.service.BuyService;
import top.yueshushu.learn.service.TradeEntrustService;

/**
 * <p>
 * 买入委托
 * </p>
 *
 * @author 两个蝴蝶飞
 * @date 2022-01-03
 */
@RestController
@RequestMapping("/buy")
@ApiModel("买入股票处理")
public class BuyController extends BaseController {
    @Autowired
    private BuyService buyService;
    @PostMapping("/buy")
    @ApiOperation("买入股票信息")
    public OutputResult buy(@RequestBody BuyRo buyRo){
        buyRo.setUserId(getUserId());
        return buyService.buy(buyRo);
    }


}
