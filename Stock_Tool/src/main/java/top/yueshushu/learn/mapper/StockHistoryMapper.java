package top.yueshushu.learn.mapper;

import cn.hutool.core.date.DateTime;
import org.apache.ibatis.annotations.Param;
import top.yueshushu.learn.pojo.StockHistory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import top.yueshushu.learn.vo.stock.StockHistoryVo;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 股票的历史交易记录表 Mapper 接口
 * </p>
 *
 * @author 岳建立  自定义的
 * @since 2022-01-02
 */
public interface StockHistoryMapper extends BaseMapper<StockHistory> {
    /**
     * 根据股票的code 查询股票的相关信息
     *
     * @param code
     * @return
     */
    List<StockHistoryVo> getStockHistory(@Param("code") String code);

    void deleteAsyncData(@Param("code") String code, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    /**
     * 查询股票的历史交易记录，有日期
     *
     * @param code
     * @param startDate
     * @param endDate
     * @return
     */
    List<StockHistoryVo> getStockHistoryAndDate(@Param("code") String code, @Param("startDate") DateTime startDate,
                                                @Param("endDate") DateTime endDate);

    /**
     * 获取股票那一天的信息
     *
     * @param code
     * @param currDate
     * @return
     */
    StockHistoryVo getStockForDate(@Param("code") String code, @Param("currDate") DateTime currDate);
}
