package top.yueshushu.learn.stock.business.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.yueshushu.learn.response.OutputResult;
import top.yueshushu.learn.ro.stock.StockRo;
import top.yueshushu.learn.stock.business.StockBusiness;
import top.yueshushu.learn.stock.service.StockService;

import javax.annotation.Resource;

/**
 * @Description 菜单实现编排层
 * @Author yuejianli
 * @Date 2022/5/20 23:54
 **/
@Service
@Slf4j
public class StockBusinessImpl implements StockBusiness {
    @Resource
    private StockService stockService;

    @Override
    public OutputResult getStockInfo(String stockCode) {
        return stockService.getCrawlerStockInfoByCode(stockCode);
    }

    @Override
    public OutputResult getStockKline(StockRo stockRo) {
        return stockService.getCrawlerLine(
                stockRo.getCode(),
                stockRo.getType()
        );
    }

    @Override
    public OutputResult stockAsync(StockRo stockRo) {
        return stockService.stockAsync(
                stockRo
        );
    }

    @Override
    public OutputResult getStockPrice(String code) {
        return stockService.getCrawlerPrice(
                code
        );
    }
}
