package top.yueshushu.learn.business;

import top.yueshushu.learn.response.OutputResult;
import top.yueshushu.learn.ro.stock.StockStatRo;

/**
 * @Description 统计的编排层处理
 * @Author 岳建立
 * @Date 2022/5/20 22:33
 **/
public interface StatBusiness {
    /**
     *
     * @param stockStatRo
     * @return
     */
    OutputResult getWeekStat(StockStatRo stockStatRo);

    OutputResult getCharStat(StockStatRo stockStatRo);

}
