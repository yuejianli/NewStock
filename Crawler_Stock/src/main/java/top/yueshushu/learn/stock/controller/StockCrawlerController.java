package top.yueshushu.learn.stock.controller;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.yueshushu.learn.response.OutputResult;
import top.yueshushu.learn.ro.stock.StockRo;
import top.yueshushu.learn.stock.business.StockBusiness;
import top.yueshushu.learn.stock.business.StockHistoryBusiness;
import top.yueshushu.learn.stock.common.CrawlerResultCode;

import javax.annotation.Resource;

/**
 * @author yjl
 * @since 2021-11-13 22:36:35
 */
@RestController
@RequestMapping("/stock")
public class StockCrawlerController {
    @Resource
    private StockBusiness stockBusiness;
    @Resource
    private StockHistoryBusiness stockHistoryBusiness;

    @PostMapping("/getStockInfo")
    public OutputResult getStockInfo(@RequestBody StockRo stockRo) {
        if (!StringUtils.hasText(stockRo.getCode())) {
            return OutputResult.buildAlert(
                    CrawlerResultCode.STOCK_CODE_ERROR
            );
        }
        return stockBusiness.getStockInfo(stockRo.getCode());
    }

    @PostMapping("/getStockKline")
    public OutputResult getStockKline(@RequestBody StockRo stockRo) {
        if (!StringUtils.hasText(stockRo.getCode())) {
            return OutputResult.buildAlert(
                    CrawlerResultCode.STOCK_CODE_ERROR
            );
        }
        if (stockRo.getType() == null) {
            return OutputResult.buildAlert(
                    CrawlerResultCode.STOCK_KLINE_IS_EMPTY
            );
        }
        return stockBusiness.getStockKline(stockRo);
    }

    @PostMapping("/stockAsync")
    public OutputResult stockAsync(@RequestBody StockRo stockRo) {
        return stockBusiness.stockAsync(stockRo);
    }

    /***
     * 关于历史记录的处理
     */
    @PostMapping("/stockHistoryAsync")
    public OutputResult stockHistoryAsync(@RequestBody StockRo stockRo) {
        if (!StringUtils.hasText(stockRo.getCode())) {
            return OutputResult.buildAlert(
                    CrawlerResultCode.STOCK_CODE_ERROR
            );
        }
        if (stockRo.getExchange() == null) {
            return OutputResult.buildAlert(
                    CrawlerResultCode.STOCK_EXCHANGE_IS_EMPTY
            );
        }
        if (!StringUtils.hasText(stockRo.getStartDate())) {
            return OutputResult.buildAlert(
                    CrawlerResultCode.STOCK_ASYNC_NO_START_DATE
            );
        }
        if (!StringUtils.hasText(stockRo.getEndDate())) {
            return OutputResult.buildAlert(
                    CrawlerResultCode.STOCK_ASYNC_NO_END_DATE
            );
        }
        return stockHistoryBusiness.stockHistoryAsync(stockRo);
    }

    @PostMapping("/getStockPrice")
    public OutputResult getStockPrice(@RequestBody StockRo stockRo) {
        if (!StringUtils.hasText(stockRo.getCode())) {
            return OutputResult.buildAlert(
                    CrawlerResultCode.STOCK_CODE_ERROR
            );
        }
        return stockBusiness.getStockPrice(stockRo.getCode());
    }
}