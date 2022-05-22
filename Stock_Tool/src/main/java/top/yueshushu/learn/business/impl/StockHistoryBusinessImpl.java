package top.yueshushu.learn.business.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.yueshushu.learn.business.StockHistoryBusiness;
import top.yueshushu.learn.response.OutputResult;
import top.yueshushu.learn.ro.stock.StockRo;
import top.yueshushu.learn.service.StockHistoryService;

import javax.annotation.Resource;

/**
 * @Description 股票历史实现编排层
 * @Author yuejianli
 * @Date 2022/5/20 23:54
 **/
@Service
@Slf4j
public class StockHistoryBusinessImpl implements StockHistoryBusiness {
    @Resource
    private StockHistoryService stockHistoryService;

    @Override
    public OutputResult listHistory(StockRo stockRo) {
        return stockHistoryService.pageHistory(stockRo);
    }
}
