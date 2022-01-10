package top.yueshushu.learn.service;

import org.apache.ibatis.annotations.Param;
import top.yueshushu.learn.mode.dto.StockPriceCacheDto;
import top.yueshushu.learn.pojo.StockHistory;
import com.baomidou.mybatisplus.extension.service.IService;
import top.yueshushu.learn.response.OutputResult;
import top.yueshushu.learn.ro.stock.StockRo;

import java.util.List;

/**
 * <p>
 * 股票的历史交易记录表 自定义的
 * </p>
 *
 * @author 岳建立
 * @since 2022-01-02
 */
public interface StockHistoryService extends IService<StockHistory> {
    /**
     * 查询股票的历史记录
     *
     * @param stockRo
     * @return
     */
    OutputResult history(StockRo stockRo);

    /**
     * 获取股票昨天对应的收盘价信息
     *
     * @param codeList
     * @return
     */
    List<StockPriceCacheDto> listClosePrice(List<String> codeList);
}
