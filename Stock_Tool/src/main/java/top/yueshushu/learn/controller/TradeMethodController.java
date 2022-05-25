package top.yueshushu.learn.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import top.yueshushu.learn.business.TradeMethodBusiness;
import top.yueshushu.learn.entity.TradeMethod;
import top.yueshushu.learn.enumtype.TradeMethodType;
import top.yueshushu.learn.domain.TradeMethodDo;
import top.yueshushu.learn.mode.ro.TradeMethodRo;
import top.yueshushu.learn.page.PageRo;
import top.yueshushu.learn.response.OutputResult;
import top.yueshushu.learn.service.TradeMethodService;

import javax.annotation.Resource;

/**
 * <p>
 * 交易，包括爬虫所使用的url信息 我是自定义的
 * </p>
 *
 * @author 岳建立
 * @date 2022-01-02
 */
@RestController
@RequestMapping("/tradeMethod")
@Api("交易相关方法的处理")
public class TradeMethodController {

    @Resource
    private TradeMethodBusiness tradeMethodBusiness;

    @PostMapping("/list")
    @ApiOperation("查询提供的交易方法")
    public OutputResult list(@RequestBody TradeMethodRo tradeMethodRo){
        return tradeMethodBusiness.list(tradeMethodRo);
    }

    @GetMapping("/yzm")
    @ApiOperation("获取验证码")
    public OutputResult yzm(){
        TradeMethod tradeMethod = tradeMethodBusiness.getMethod(
                TradeMethodType.yzm
        );
        //获取方法的 url
        String url = tradeMethod.getUrl();
        return OutputResult.buildSucc(
               url
        );
    }
}
