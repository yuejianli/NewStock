package top.yueshushu.learn.business;
import top.yueshushu.learn.model.info.StockShowInfo;
import top.yueshushu.learn.response.OutputResult;
import top.yueshushu.learn.ro.stock.StockRo;
import top.yueshushu.learn.ro.stock.StockStatRo;

/**
 * @Description 爬虫的编排层
 * @Author 岳建立
 * @Date 2021/11/12 23:06
 **/
public interface StockCrawlerBusiness {
    /**
     * 获取股票的相关信息
     * @param stockRo
     * @return
     */
    OutputResult<StockShowInfo> getStockInfo(StockRo stockRo);

    /**
     * 查看股票的K线
     * @param stockRo
     * @return
     */
    OutputResult<String> getStockKline(StockRo stockRo);

    /**
     * 股票同步
     * @param stockRo
     * @return
     */
    OutputResult<String> stockAsync(StockRo stockRo);

    /**
     * 同步股票的历史记录
     * @param stockRo
     * @return
     */
    OutputResult<String> stockHistoryAsync(StockRo stockRo);


    /**
     * 获取股票的近一个月的相关信息
     * @param stockStatRo
     * @return
     */
    OutputResult getWeekStat(StockStatRo stockStatRo);

    /**
     * 获取股票的展示信息
     * @param stockStatRo
     * @return
     */
    OutputResult getCharStat(StockStatRo stockStatRo);

    /**
     * 更新股票的当前价格，放置到 redis 缓存里面。
     * @param code
     */
    void updateCodePrice(String code);

}
