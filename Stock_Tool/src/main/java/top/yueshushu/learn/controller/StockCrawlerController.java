package top.yueshushu.learn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.yueshushu.learn.model.info.StockInfo;
import top.yueshushu.learn.model.info.StockShowInfo;
import top.yueshushu.learn.response.OutputResult;
import top.yueshushu.learn.ro.stock.StockRo;
import top.yueshushu.learn.ro.stock.StockStatRo;
import top.yueshushu.learn.service.StockCrawlerService;
import top.yueshushu.learn.service.StockService;

/**
 * @ClassName:StockController
 * @Description TODO
 * @Author 岳建立
 * @Date 2021/11/12 23:03
 * @Version 1.0
 **/
@RestController
@RequestMapping("/stockCrawler")
public class StockCrawlerController {
    @Autowired
    private StockCrawlerService stockCrawlerService;
    @PostMapping("/getStockInfo")
    public OutputResult<StockShowInfo> getStockInfo(@RequestBody StockRo stockRo){
        return stockCrawlerService.getStockInfo(stockRo);
    }
    @PostMapping("/getStockKline")
    public OutputResult<String> getStockKline(@RequestBody StockRo stockRo){
        return stockCrawlerService.getStockKline(stockRo);
    }
    @PostMapping("/stockAsync")
    public OutputResult<String> stockAsync(@RequestBody StockRo stockRo){
        return stockCrawlerService.stockAsync(stockRo);
    }
    /*关于历史记录的处理*/
    @PostMapping("/stockHistoryAsync")
    public OutputResult stockHistoryAsync(@RequestBody StockRo stockRo){
        return stockCrawlerService.stockHistoryAsync(stockRo);
    }

    @PostMapping("/getWeekStat")
    public OutputResult getWeekStat(@RequestBody StockStatRo stockStatRo){
        return stockCrawlerService.getWeekStat(stockStatRo);
    }
    @PostMapping("/getCharStat")
    public OutputResult getCharStat(@RequestBody StockStatRo stockStatRo){
        return stockCrawlerService.getCharStat(stockStatRo);
    }
}