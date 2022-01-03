package top.yueshushu.learn.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import top.yueshushu.learn.response.OutputResult;
import top.yueshushu.learn.ro.stock.StockRo;
import top.yueshushu.learn.service.StockHistoryService;

/**
 * <p>
 * 股票的历史交易记录表 我是自定义的
 * </p>
 *
 * @author 岳建立
 * @date 2022-01-02
 */
@RestController
@RequestMapping("/stockHistory")
@Api("查询股票的历史")
public class StockHistoryController {
    @Autowired
    private StockHistoryService stockHistoryService;
    @ApiOperation("查询股票的历史记录")
    @PostMapping("/history")
    public OutputResult history(@RequestBody StockRo stockRo){
        return stockHistoryService.history(stockRo);
    }

}
