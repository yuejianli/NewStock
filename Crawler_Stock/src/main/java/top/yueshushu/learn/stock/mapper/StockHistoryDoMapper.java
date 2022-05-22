package top.yueshushu.learn.stock.mapper;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import top.yueshushu.learn.stock.domain.StockHistoryDo;
import top.yueshushu.learn.vo.stock.StockHistoryVo;

import java.util.Date;
import java.util.List;

/**
 * @Description 股票历史的Mapper
 * @Author 岳建立
 * @Date 2021/11/14 11:23
 **/
public interface StockHistoryDoMapper extends BaseMapper<StockHistoryDo> {
    /**
     * 删除该股票这期间内的历史数据
     *
     * @param code      股票编码
     * @param startDate 开始日期
     * @param endDate   结束日期
     */
    void deleteAsyncRangeDateData(@Param("code") String code,
                                  @Param("startDate") Date startDate,
                                  @Param("endDate") Date endDate);

}
