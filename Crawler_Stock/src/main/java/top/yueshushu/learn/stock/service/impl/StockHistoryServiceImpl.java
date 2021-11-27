package top.yueshushu.learn.stock.service.impl;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import top.yueshushu.learn.enumtype.CharPriceType;
import top.yueshushu.learn.enumtype.WeekStatType;
import top.yueshushu.learn.page.PageResponse;
import top.yueshushu.learn.response.OutputResult;
import top.yueshushu.learn.ro.stock.StockRo;
import top.yueshushu.learn.ro.stock.StockStatRo;
import top.yueshushu.learn.stock.mapper.StockHistoryMapper;
import top.yueshushu.learn.stock.mapper.StockMapper;
import top.yueshushu.learn.stock.pojo.Stock;
import top.yueshushu.learn.stock.pojo.StockHistory;
import top.yueshushu.learn.stock.service.CrawlerStockService;
import top.yueshushu.learn.stock.service.StockHistoryService;
import top.yueshushu.learn.stock.util.MyDateUtil;
import top.yueshushu.learn.util.BigDecimalUtil;
import top.yueshushu.learn.vo.charinfo.LineSeriesVo;
import top.yueshushu.learn.vo.charinfo.LineVo;
import top.yueshushu.learn.vo.stock.StockHistoryVo;
import top.yueshushu.learn.vo.stock.StockWeekStatInfoVo;
import top.yueshushu.learn.vo.stock.StockWeekStatVo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @ClassName:StockHistoryServiceImpl
 * @Description TODO
 * @Author 岳建立
 * @Date 2021/11/14 11:24
 * @Version 1.0
 **/
@Service
public class StockHistoryServiceImpl extends ServiceImpl<StockHistoryMapper,
        StockHistory>  implements StockHistoryService {
    @Autowired
    private StockHistoryMapper stockHistoryMapper;
    @Autowired
    private CrawlerStockService crawlerStockService;
    @Autowired
    private StockMapper stockMapper;
    @Override
    public OutputResult getStockHistory(StockRo stockRo) {
        PageHelper.startPage(stockRo.getPageNum(),stockRo.getPageSize());
      // List<StockHistoryVo> stockHistoryList= stockHistoryMapper.getStockHistory(stockRo.getCode());
       List<StockHistoryVo> stockHistoryList= stockHistoryMapper.getStockHistoryAndDate(stockRo.getCode()
               , DateUtil.parse(
                       stockRo.getStartDate(),"yyyy-MM-dd"
               ),
               DateUtil.parse(
                       stockRo.getEndDate(),"yyyy-MM-dd"
               ));
       PageInfo pageInfo=new PageInfo<StockHistoryVo>(stockHistoryList);
       return OutputResult.success(new PageResponse<>(
               pageInfo.getTotal(),pageInfo.getList()
       ));
    }

    @Override
    public void deleteAsyncData(StockRo stockRo) {
        stockHistoryMapper.deleteAsyncData(stockRo.getCode(),
                DateUtil.parse(
                        stockRo.getStartDate(),"yyyyMMdd"
                ),
                DateUtil.parse(
                        stockRo.getEndDate(),"yyyyMMdd"
                ));
    }

    @Override
    public OutputResult getWeekStat(StockStatRo stockStatRo) {
        //进行处理历史记录
        //1. 查询当前股票的最大历史记录的天
       // syncLastOneMonth(stockStatRo.getCode());
        //查询当前范围内的记录信息
        int offset=-1;
        DateTime endDate=DateUtil.date().offsetNew(
                DateField.DAY_OF_YEAR,offset-0
        );

        List<DateTime> searchDateList = new ArrayList<>();
        searchDateList.add(DateUtil.date().offsetNew(
                DateField.DAY_OF_YEAR,offset-7
        ));
        searchDateList.add(DateUtil.date().offsetNew(
                DateField.DAY_OF_YEAR,offset-14
        ));
        searchDateList.add(DateUtil.date().offsetNew(
                DateField.DAY_OF_YEAR,offset-21
        ));
        searchDateList.add(DateUtil.date().offsetNew(
                DateField.MONTH,-1
        ));
        //对记录进行处理
        StockWeekStatVo stockWeekStatVo=new StockWeekStatVo();
        //先查询一下，最近的一天的值.
        StockHistoryVo lastVo=stockHistoryMapper.getStockForDate(
                stockStatRo.getCode(),endDate
        );
        List<StockWeekStatInfoVo> stockWeekStatInfoVoList=new ArrayList<>();
        int weekIndex=1;
        for(DateTime dateTime:searchDateList){
            StockHistoryVo tempVo=stockHistoryMapper.getStockForDate(
                    stockStatRo.getCode(),dateTime
            );
            //比较，放置值
            StockWeekStatInfoVo stockWeekStatInfoVo=new StockWeekStatInfoVo();
            stockWeekStatInfoVo.setType(weekIndex);
            stockWeekStatInfoVo.setTypeName(
                    WeekStatType.getExchangeType(
                            weekIndex
                    ).getDesc()
            );


            BigDecimal bigDecimal = BigDecimalUtil.subBigDecimal(
                    lastVo.getClosingPrice(),
                    tempVo.getClosingPrice()
            );
            stockWeekStatInfoVo.setRangePrice(
                    BigDecimalUtil.toString(bigDecimal)
            );
            String proportion = BigDecimalUtil.divPattern(
                    bigDecimal, tempVo.getClosingPrice()
            );
            stockWeekStatInfoVo.setRangeProportion(proportion);
            weekIndex=weekIndex+1;
            stockWeekStatInfoVoList.add(stockWeekStatInfoVo);
        }


       stockWeekStatVo.setWeekStatInfoList(stockWeekStatInfoVoList);
        return OutputResult.success(stockWeekStatVo);

    }

    @Override
    public OutputResult getCharStat(StockStatRo stockStatRo) {
        final String DATE_FORMATTER="yyyy-MM-dd";
        CharPriceType[] values = CharPriceType.values();
        List<String> legendList=new ArrayList<>();
        for(CharPriceType charPriceType:values){
            legendList.add(charPriceType.getDesc());
        }
        //获取范围
        List<String> xaxisData=new ArrayList<>();
        //获取开始日期和结束日期
        String startDate = stockStatRo.getStartDate();
        String endDate = stockStatRo.getEndDate();
        //计算开始日期和结束日期相差多少天，就是后面的计算值.
        DateTime startDateDate=DateUtil.parse(startDate,DATE_FORMATTER);
        DateTime endDateDate=DateUtil.parse(endDate,DATE_FORMATTER);
        //计算一下，相差多少天
        long day = DateUtil.betweenDay(startDateDate, endDateDate, true);
        //进行计算
        for(int i=0;i<=day;i++){
            DateTime tempDate = DateUtil.offsetDay(startDateDate, i);
            if(MyDateUtil.isWorkingDay(tempDate)){
                xaxisData.add(DateUtil.format(tempDate,DATE_FORMATTER));
            }
        }
        LineVo lineVo=new LineVo();
        lineVo.setLegend(legendList);
        lineVo.setXaxisData(xaxisData);

        //计算拼接信息.
        List<StockHistoryVo> stockHistoryVoList=stockHistoryMapper.getStockHistoryAndDate(
                stockStatRo.getCode(),
                startDateDate,
                endDateDate
        );
        if(CollectionUtils.isEmpty(stockHistoryVoList)){
            return OutputResult.success(lineVo);
        }
        //进行处理.
        List<LineSeriesVo> lineSeriesVoList=historyConvertLine(stockHistoryVoList);
        lineVo.setSeries(lineSeriesVoList);
        return OutputResult.success(lineVo);
    }

    private List<LineSeriesVo> historyConvertLine(List<StockHistoryVo> stockHistoryVoList) {
        List<LineSeriesVo> result=new ArrayList<>();

        LineSeriesVo openingPrice=new LineSeriesVo();
        openingPrice.setName(CharPriceType.OPENINGPRICE.getDesc());
        LineSeriesVo closingPrice=new LineSeriesVo();
        closingPrice.setName(CharPriceType.CLOSINGPRICE.getDesc());
        LineSeriesVo highestPrice=new LineSeriesVo();
        highestPrice.setName(CharPriceType.HIGHESTPRICE.getDesc());
        LineSeriesVo lowestPrice=new LineSeriesVo();
        lowestPrice.setName(CharPriceType.LOWESTPRICE.getDesc());
        LineSeriesVo amplitudeproportion=new LineSeriesVo();
        amplitudeproportion.setName(CharPriceType.AMPLITUDEPROPORTION.getDesc());

        LineSeriesVo amplitude=new LineSeriesVo();
        amplitude.setName(CharPriceType.AMPLITUDE.getDesc());

        //处理信息
        for(StockHistoryVo stockHistoryVo:stockHistoryVoList){
            openingPrice.getData().add(BigDecimalUtil.toDouble(stockHistoryVo.getOpeningPrice()));
            closingPrice.getData().add(BigDecimalUtil.toDouble(stockHistoryVo.getClosingPrice()));
            highestPrice.getData().add(BigDecimalUtil.toDouble(stockHistoryVo.getHighestPrice()));
            lowestPrice.getData().add(BigDecimalUtil.toDouble(stockHistoryVo.getLowestPrice()));
            amplitudeproportion.getData().add(BigDecimalUtil.toDouble(stockHistoryVo.getAmplitudeProportion()));
            amplitude.getData().add(BigDecimalUtil.toDouble(stockHistoryVo.getAmplitude()));
        }
        result.add(openingPrice);
        result.add(closingPrice);
        result.add(highestPrice);
        result.add(lowestPrice);
        result.add(amplitudeproportion);
        result.add(amplitude);
        return result;
    }

    /**
     * 将历史进行转换
     * @param stockHistoryList
     */
    private StockWeekStatInfoVo historyListConvertStatVo(List<StockHistoryVo> stockHistoryList) {
        StockWeekStatInfoVo stockWeekStatInfoVo=new StockWeekStatInfoVo();
        if(CollectionUtils.isEmpty(stockHistoryList)){
            return stockWeekStatInfoVo;
        }
        //获取第一个值.
        StockHistoryVo first=stockHistoryList.get(0);
        StockHistoryVo last=stockHistoryList.get(stockHistoryList.size()-1);

        BigDecimal bigDecimal = BigDecimalUtil.subBigDecimal(
                last.getClosingPrice(),
                first.getClosingPrice()
        );
        stockWeekStatInfoVo.setRangePrice(
                BigDecimalUtil.toString(bigDecimal)
        );
        String proportion = BigDecimalUtil.divPattern(
                bigDecimal, first.getClosingPrice()
        );
        stockWeekStatInfoVo.setRangeProportion(proportion);
        return stockWeekStatInfoVo;
    }

    private void syncLastOneMonth(String code) {
        //后期优化,目前直接同步最后一个月的记录.
        StockRo stockRo=new StockRo();
        final String Date_formatter="yyyyMMdd";
        //往前调整一天
        Date now=DateUtil.date().offsetNew(
                DateField.DAY_OF_YEAR,-1
        );
        String endDate=DateUtil.format(
                now,Date_formatter
        );;
        DateTime dateTime = DateUtil.offsetMonth(now, -1);
        String startDate=DateUtil.format(dateTime,Date_formatter);
        stockRo.setStartDate(startDate);
        stockRo.setEndDate(endDate);
        stockRo.setCode(code);
        stockRo.setExchange(1);
        //进行同步
        crawlerStockService.stockHistoryAsync(stockRo);
    }



}
