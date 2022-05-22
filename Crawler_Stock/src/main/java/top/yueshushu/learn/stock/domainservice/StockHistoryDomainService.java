package top.yueshushu.learn.stock.domainservice;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.extension.service.IService;
import top.yueshushu.learn.stock.domain.StockHistoryDo;

import java.util.Date;
import java.util.List;

/**
 * @Description 股票的操作
 * @Author yuejianli
 * @Date 2022/5/20 23:23
 **/
public interface StockHistoryDomainService extends IService<StockHistoryDo> {
    /**
     * 删除该股票这期间内的历史数据
     * @param code 股票编码
     * @param startDate 开始日期
     * @param endDate 结束日期
     */
    void deleteAsyncRangeDateData(String code, String startDate, String endDate);
}
