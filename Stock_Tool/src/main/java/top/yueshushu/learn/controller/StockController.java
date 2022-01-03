package top.yueshushu.learn.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import top.yueshushu.learn.mode.vo.StockVo;
import top.yueshushu.learn.model.info.StockInfo;
import top.yueshushu.learn.model.info.StockShowInfo;
import top.yueshushu.learn.pojo.Stock;
import top.yueshushu.learn.response.OutputResult;
import top.yueshushu.learn.ro.stock.StockRo;
import top.yueshushu.learn.service.StockService;

/**
 * <p>
 * 股票信息基本表 我是自定义的
 * </p>
 *
 * @author 岳建立
 * @date 2022-01-02
 */
@RestController
@RequestMapping("/stock")
@Api("股票信息")
public class StockController {
    @Autowired
    private StockService stockService;
    @PostMapping("/list")
    @ApiOperation("查询所有的股票信息")
    public OutputResult<StockInfo> list(@RequestBody StockRo stockRo){
        return stockService.listStock(stockRo);
    }

    @PostMapping("/getStockInfo")
    @ApiOperation("根据股票编码,获取股票的相关信息")
    public OutputResult<StockVo> getStockInfo(@RequestBody StockRo stockRo){
        return stockService.getStockInfo(stockRo.getCode());
    }
}
