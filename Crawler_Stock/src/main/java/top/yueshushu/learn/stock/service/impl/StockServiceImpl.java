package top.yueshushu.learn.stock.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import top.yueshushu.learn.enumtype.DataFlagType;
import top.yueshushu.learn.enumtype.KType;
import top.yueshushu.learn.model.info.StockShowInfo;
import top.yueshushu.learn.page.PageResponse;
import top.yueshushu.learn.response.BaseResultCode;
import top.yueshushu.learn.response.OutputResult;
import top.yueshushu.learn.ro.stock.StockRo;
import top.yueshushu.learn.stock.assembler.StockAssembler;
import top.yueshushu.learn.stock.common.ResultCode;
import top.yueshushu.learn.stock.crawler.CrawlerService;
import top.yueshushu.learn.stock.domain.StockDo;
import top.yueshushu.learn.stock.domainservice.StockDomainService;
import top.yueshushu.learn.stock.entity.DownloadStockInfo;
import top.yueshushu.learn.stock.entity.Stock;
import top.yueshushu.learn.stock.mapper.StockDoMapper;
import top.yueshushu.learn.stock.service.StockService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @ClassName:StockBaseServiceImpl
 * @Description TODO
 * @Author 岳建立
 * @Date 2021/11/14 0:04
 * @Version 1.0
 **/
@Service
@Slf4j
public class StockServiceImpl implements StockService {
    @Resource
    private StockDomainService stockDomainService;
    @Resource
    private StockAssembler stockAssembler;

    @Resource
    private CrawlerService crawlerService;

    @Override
    public OutputResult getCrawlerStockInfoByCode(String stockCode) {
        Stock stock = stockAssembler.doToEntity(
                stockDomainService.getByCode(stockCode)
        );
        if (stock == null){
            return OutputResult.buildFail(
                    ResultCode.STOCK_CODE_ERROR
            );
        }
        StockShowInfo nowInfo = crawlerService.getNowInfo(
                stock.getFullCode()
        );
        nowInfo.setCode(stock.getCode());
        nowInfo.setFullCode(
                stock.getFullCode()
        );
        nowInfo.setExchange(stock.getExchange());
        log.info("获取当前的股票{} 对象信息是:{}",stockCode,nowInfo);
        return OutputResult.buildSucc(
            nowInfo
        );
    }

    @Override
    public OutputResult getCrawlerLine(String code, Integer type) {
        //获取类型
        KType kType = Optional.ofNullable(KType.getKType(type)).orElse(
                KType.MIN
        );
        Stock stock = stockAssembler.doToEntity(
                stockDomainService.getByFullCode(code)
        );
        if(null ==stock){
            return OutputResult.buildAlert(ResultCode.STOCK_CODE_ERROR);
        }
        String result="";
        switch (kType){
            case MIN:{
                result=crawlerService.getMinUrl(stock.getFullCode());
                break;
            }
            case DAY:{
                result=crawlerService.getDayUrl(stock.getFullCode());
                break;
            }
            case WEEK:{
                result=crawlerService.getWeekUrl(stock.getFullCode());
                break;
            }
            case MONTH:{
                result=crawlerService.getMonthUrl(stock.getFullCode());
                break;
            }
            default:{
                break;
            }
        }
        log.info("成功获取当前的股票{} 的K线类型",stock.getCode(),kType.getDesc());
        return OutputResult.buildSucc(result);
    }

    @Override
    public OutputResult stockAsync(StockRo stockRo) {
        //时间计数器
        TimeInterval timer = DateUtil.timer();
        timer.start();
        List<DownloadStockInfo> downloadStockInfoList = crawlerService.getStockList();
        if(CollectionUtils.isEmpty(downloadStockInfoList)){
            log.error("同步时未获取到股票列表信息");
            return OutputResult.buildFail(ResultCode.STOCK_ASYNC_FAIL);
        }
        log.info(">>获取网络股票信息并转换使用时间:{}",timer.intervalMs());
        //获取到当前的股票列表信息
        List<String> allStockCodeList = stockDomainService.listAllCode();
        //进行批量保存
        List<StockDo> stockList=new ArrayList<>();
        downloadStockInfoList.stream().forEach(
                n->{
                    if(!allStockCodeList.contains(n.getCode())){
                        StockDo stockDo = stockAssembler.downInfoToDO(n);
                        stockDo.setFlag(DataFlagType.NORMAL.getCode());
                        stockList.add(stockDo);
                    }
                }
        );
        if (CollectionUtils.isEmpty(stockList)){
            return OutputResult.buildSucc(ResultCode.STOCK_ASYNC_NO_CHANGE);
        }
        log.info("本次同步时增加的股票编码依次为:{}",
                stockList.stream().map(
                        StockDo::getCode
                ).collect(
                        Collectors.toList()
                ));
        stockDomainService.saveBatch(stockList,1000);
        log.info("同步信息到数据库共用时 {}",timer.intervalMs());
        return OutputResult.buildSucc(ResultCode.STOCK_ASYNC_SUCCESS);
    }

    @Override
    public OutputResult getCrawlerPrice(String fullCode) {
        return OutputResult.buildSucc(
                crawlerService.sinaGetPrice(fullCode)
        );
    }
}
